# Points-Benefit-System 积分权益运营系统
## 项目介绍
基于SpringBoot开发的后端积分管理系统，实现用户积分增减、积分兑换、秒杀、权限管理等业务功能。

## 技术栈
后端：SpringBoot、MyBatis、MySQL
前端：Vue
构建工具：Maven

## 环境要求
JDK 8、MySQL 5.7+、Maven 3.6+

## 启动步骤
1. 执行sql文件夹内数据库脚本，创建库表
2. 修改yml数据库账号密码
3. 运行启动类 JifenQuanyiApplication.java
4. 访问前端页面

## 项目模块
admin：后台管理员
user：普通用户
interceptor：拦截器
seckill：积分秒杀
exchange：积分兑换
common：公共工具类
