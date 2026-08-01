CREATE DATABASE IF NOT EXISTS jifen_quanyi DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE jifen_quanyi;

CREATE TABLE IF NOT EXISTS t_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    phone       VARCHAR(20)  NOT NULL COMMENT '手机号',
    password    VARCHAR(64)  NOT NULL COMMENT 'md5密码',
    nickname    VARCHAR(64)           COMMENT '昵称',
    avatar      VARCHAR(255)          COMMENT '头像',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1正常 0禁用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_phone (phone)
) ENGINE=InnoDB COMMENT='用户表';

CREATE TABLE IF NOT EXISTS t_user_session (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id     BIGINT      NOT NULL COMMENT '用户id',
    token       VARCHAR(64) NOT NULL COMMENT '会话token',
    expire_time DATETIME             COMMENT '过期时间',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY idx_token (token),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB COMMENT='用户会话表';

CREATE TABLE IF NOT EXISTS t_point_account (
    id            BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id       BIGINT   NOT NULL COMMENT '用户id',
    total_point   INT      NOT NULL DEFAULT 0 COMMENT '可用积分',
    frozen_point  INT      NOT NULL DEFAULT 0 COMMENT '冻结积分',
    expired_point INT      NOT NULL DEFAULT 0 COMMENT '累计过期积分',
    version       INT      NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    create_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB COMMENT='积分账户表';

CREATE TABLE IF NOT EXISTS t_point_flow (
    id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    flow_no       VARCHAR(64) NOT NULL COMMENT '流水号',
    user_id       BIGINT      NOT NULL COMMENT '用户id',
    biz_type      TINYINT     NOT NULL COMMENT '1签到 2任务 3兑换 4秒杀',
    change_point  INT         NOT NULL COMMENT '变动积分 正加负减',
    balance_after INT         NOT NULL COMMENT '变动后余额',
    source_type   TINYINT     NOT NULL DEFAULT 1 COMMENT '1普通 2秒杀',
    source_id     BIGINT      NOT NULL COMMENT '来源id',
    expire_time   DATETIME             COMMENT '本笔积分过期时间',
    status        TINYINT     NOT NULL DEFAULT 1 COMMENT '1有效 2已回收',
    create_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_flow_no (flow_no),
    KEY idx_user_time (user_id, create_time DESC),
    KEY idx_expire (expire_time, status)
) ENGINE=InnoDB COMMENT='积分流水表';

CREATE TABLE IF NOT EXISTS t_sign_in_record (
    id          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id     BIGINT    NOT NULL COMMENT '用户id',
    sign_date   DATE      NOT NULL COMMENT '签到日期',
    point       INT       NOT NULL DEFAULT 0 COMMENT '奖励积分',
    create_time DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_date (user_id, sign_date)
) ENGINE=InnoDB COMMENT='签到记录表';

CREATE TABLE IF NOT EXISTS t_task (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    task_code   VARCHAR(64)  NOT NULL COMMENT '任务编码',
    task_name   VARCHAR(128) NOT NULL COMMENT '任务名称',
    point       INT          NOT NULL DEFAULT 0 COMMENT '奖励积分',
    daily_limit INT          NOT NULL DEFAULT 1 COMMENT '每日可领次数',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1上架 0下架',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_task_code (task_code)
) ENGINE=InnoDB COMMENT='任务配置表';

CREATE TABLE IF NOT EXISTS t_task_record (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id     BIGINT   NOT NULL COMMENT '用户id',
    task_id     BIGINT   NOT NULL COMMENT '任务id',
    task_date   DATE     NOT NULL COMMENT '领取日期',
    point       INT      NOT NULL DEFAULT 0 COMMENT '奖励积分',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_task_date (user_id, task_id, task_date)
) ENGINE=InnoDB COMMENT='任务领取记录表';

CREATE TABLE IF NOT EXISTS t_point_expire_record (
    id          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id     BIGINT    NOT NULL COMMENT '用户id',
    flow_no     VARCHAR(64) NOT NULL COMMENT '原流水号',
    point       INT       NOT NULL COMMENT '回收积分',
    expire_time DATETIME  NOT NULL COMMENT '过期时间',
    status      TINYINT   NOT NULL DEFAULT 1 COMMENT '1已回收',
    create_time DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_expire_status (expire_time, status),
    KEY idx_flow_no (flow_no)
) ENGINE=InnoDB COMMENT='积分过期回收记录表';

CREATE TABLE IF NOT EXISTS t_product (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    product_code VARCHAR(64)  NOT NULL COMMENT '商品编码',
    name         VARCHAR(128) NOT NULL COMMENT '商品名称',
    cover        VARCHAR(255)          COMMENT '封面图',
    point_price  INT          NOT NULL DEFAULT 0 COMMENT '兑换所需积分',
    stock        INT          NOT NULL DEFAULT 0 COMMENT '库存',
    status       TINYINT      NOT NULL DEFAULT 1 COMMENT '1上架 0下架',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_status (status, point_price)
) ENGINE=InnoDB COMMENT='兑换商品表';

CREATE TABLE IF NOT EXISTS t_exchange_record (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    exchange_no  VARCHAR(64)  NOT NULL COMMENT '兑换单号',
    user_id      BIGINT       NOT NULL COMMENT '用户id',
    product_id   BIGINT       NOT NULL COMMENT '商品id',
    product_name VARCHAR(128) NOT NULL COMMENT '商品名称',
    point_cost   INT          NOT NULL COMMENT '消耗积分',
    status       TINYINT      NOT NULL DEFAULT 1 COMMENT '1成功 0失败',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_exchange_no (exchange_no),
    KEY idx_user_time (user_id, create_time DESC)
) ENGINE=InnoDB COMMENT='兑换记录表';

CREATE TABLE IF NOT EXISTS t_seckill_activity (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    activity_name VARCHAR(128) NOT NULL COMMENT '活动名称',
    product_id    BIGINT       NOT NULL COMMENT '商品id',
    point_cost    INT          NOT NULL COMMENT '秒杀消耗积分',
    total_stock   INT          NOT NULL COMMENT '总库存',
    start_time    DATETIME     NOT NULL COMMENT '开始时间',
    end_time      DATETIME     NOT NULL COMMENT '结束时间',
    status        TINYINT      NOT NULL DEFAULT 1 COMMENT '1启用 0停用',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_start_end (start_time, end_time)
) ENGINE=InnoDB COMMENT='秒杀活动表';

CREATE TABLE IF NOT EXISTS t_seckill_order (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no    VARCHAR(64)  NOT NULL COMMENT '订单号',
    user_id     BIGINT       NOT NULL COMMENT '用户id',
    activity_id BIGINT       NOT NULL COMMENT '活动id',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1已创建',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    UNIQUE KEY uk_user_activity (user_id, activity_id)
) ENGINE=InnoDB COMMENT='秒杀订单表';

CREATE TABLE IF NOT EXISTS t_stock (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    biz_type    TINYINT  NOT NULL COMMENT '1秒杀',
    biz_id      BIGINT   NOT NULL COMMENT '业务id(活动id)',
    total       INT      NOT NULL DEFAULT 0 COMMENT '总库存',
    available   INT      NOT NULL DEFAULT 0 COMMENT '可用库存',
    version     INT      NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_biz (biz_type, biz_id)
) ENGINE=InnoDB COMMENT='库存表';

CREATE TABLE IF NOT EXISTS t_reconcile_record (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    batch_no    VARCHAR(64)  NOT NULL COMMENT '对账批次号',
    user_id     BIGINT       NOT NULL COMMENT '用户id',
    diff        INT          NOT NULL COMMENT '差异值',
    status      TINYINT      NOT NULL DEFAULT 0 COMMENT '0待处理 1已处理',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_batch (batch_no)
) ENGINE=InnoDB COMMENT='对账差异记录表';

CREATE TABLE IF NOT EXISTS t_admin_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username    VARCHAR(64)  NOT NULL COMMENT '登录账号',
    password    VARCHAR(64)  NOT NULL COMMENT 'md5密码',
    nickname    VARCHAR(64)           COMMENT '昵称',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1正常 0禁用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB COMMENT='管理员表';

INSERT INTO t_admin_user (username, password, nickname)
SELECT 'admin', '21232f297a57a5a743894a0e4a801fc3', '超级管理员'
WHERE NOT EXISTS (SELECT 1 FROM t_admin_user WHERE username = 'admin');
