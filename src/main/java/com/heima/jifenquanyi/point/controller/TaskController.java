package com.heima.jifenquanyi.point.controller;

import com.heima.jifenquanyi.common.result.R;
import com.heima.jifenquanyi.point.dto.TaskDTO;
import com.heima.jifenquanyi.point.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/list")
    public R<List<TaskDTO>> list() {
        return R.ok(taskService.list());
    }

    @PostMapping("/claim/{taskId}")
    public R<Void> claim(@PathVariable Long taskId) {
        taskService.claim(taskId);
        return R.ok();
    }
}
