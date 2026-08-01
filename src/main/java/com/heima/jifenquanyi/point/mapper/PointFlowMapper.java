package com.heima.jifenquanyi.point.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.jifenquanyi.point.entity.PointFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PointFlowMapper extends BaseMapper<PointFlow> {

    @Select("SELECT COALESCE(SUM(change_point), 0) FROM t_point_flow WHERE user_id = #{userId} AND change_point > 0 AND status = 1")
    Integer sumIn(Long userId);

    @Select("SELECT COALESCE(SUM(ABS(change_point)), 0) FROM t_point_flow WHERE user_id = #{userId} AND change_point < 0 AND status = 1")
    Integer sumOut(Long userId);

    @Select("SELECT COALESCE(SUM(ABS(change_point)), 0) FROM t_point_flow WHERE user_id = #{userId} AND status = 2")
    Integer sumExpired(Long userId);
}
