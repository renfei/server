# 涉密系统 “三员” 管理

## 三员设计

系统设计时采用了系统管理员、安全保密管理员、安全审计管理员三员分立，分别负责系统的运行、安全保密和安全审计工作。
三员权限划分如下：

> *warning*  
> 三员相关的账号、角色均为内置，不会在管理界面中显示，也禁止任何用户编辑，只能由软件开发厂商进行修改相关配置。

### 系统管理员

> System Manage Officer  
> 账号：SMO；角色名：SYSTEM_MANAGE_OFFICER

* 负责系统的日常运行维护工作
* 负责系统用户创建、用户删除

### 安全保密员

> System Security Officer  
> 账号：SSO；角色名：SYSTEM_SECURITY_OFFICER

* 负责系统的日常安全保密管理工作
* 负责系统用户修改、用户密码重置、停启、定密
* 负责系统用户的角色分配、角色的功能资源分配
* 负责管理与审查系统用户及安全审计管理员的操作日志

### 安全审计员

> System Security Auditor  
> 账号：SSA；角色名：SYSTEM_SECURITY_AUDITOR

* 负责对系统管理员和安全保密管理员的日常操作行为进行审计跟踪分析和监督检查
* 审计管理员禁止访问管理平台安装的系统文件和直接访问数据库
* 禁止执行其它项目管理平台管理工作
