package com.heima.jifenquanyi.point.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.jifenquanyi.common.constants.PointStatus;
import com.heima.jifenquanyi.common.constants.RedisKeyConstant;
import com.heima.jifenquanyi.common.exception.BizException;
import com.heima.jifenquanyi.common.util.UserContext;
import com.heima.jifenquanyi.point.dto.TaskDTO;
import com.heima.jifenquanyi.point.entity.Task;
import com.heima.jifenquanyi.point.entity.TaskRecord;
import com.heima.jifenquanyi.point.mapper.TaskMapper;
import com.heima.jifenquanyi.point.mapper.TaskRecordMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRecordMapper taskRecordMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final PointService pointService;
    private final DefaultRedisScript<Long> signScript;

    public TaskService(TaskMapper taskMapper, TaskRecordMapper taskRecordMapper,
                       StringRedisTemplate stringRedisTemplate, RedissonClient redissonClient,
                       PointService pointService, DefaultRedisScript<Long> signScript) {
        this.taskMapper = taskMapper;
        this.taskRecordMapper = taskRecordMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
        this.pointService = pointService;
        this.signScript = signScript;
    }

    public List<TaskDTO> list() {
        List<Task> tasks = taskMapper.selectList(
                new LambdaQueryWrapper<Task>().eq(Task::getStatus, 1));
        return BeanUtil.copyToList(tasks, TaskDTO.class);
    }

    public void claim(Long taskId) {
        Long userId = UserContext.getUserId();
        String date = LocalDate.now().toString();
        Long first = stringRedisTemplate.execute(
                signScript, List.of(RedisKeyConstant.taskClaim(userId, taskId, date)), "172800");
        if (first == null || first == 0) {
            throw new BizException("今日任务已领取");
        }
        RLock lock = redissonClient.getLock(RedisKeyConstant.taskLock(userId, taskId, date));
        try {
            lock.lock();
            Task task = taskMapper.selectById(taskId);
            if (task == null || task.getStatus() == null || task.getStatus() != 1) {
                throw new BizException("任务不存在或已下架");
            }
            TaskRecord record = new TaskRecord();
            record.setUserId(userId);
            record.setTaskId(taskId);
            record.setTaskDate(LocalDate.now());
            record.setPoint(task.getPoint());
            try {
                taskRecordMapper.insert(record);
            } catch (DuplicateKeyException e) {
                throw new BizException("今日任务已领取");
            }
            pointService.addPoint(userId, task.getPoint(), PointStatus.TASK, record.getId(),
                    LocalDateTime.now().plusYears(1));
        } finally {
            lock.unlock();
        }
    }
}
