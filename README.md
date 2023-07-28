# Renfei.net的服务器端程序

[![License](https://img.shields.io/github/license/renfei/server)](https://github.com/renfei/server/blob/master/LICENSE)

## 模块

微服务流行的当下，我的小网站并不适合微服务，所以我依然选用单机版本，但又希望支持微服务的改造，
所以我拆分成了不同的模块，在其他项目中需要微服务架构时，方便改造，其中均考虑到分布式的情况。
以下为模块的说明：

| 模块             | 描述                                   |
|----------------|--------------------------------------|
| server-bom     | 🗒️ Bill of Material - 物料清单 - 记录依赖版本 |
| server-core    | ⚙️ 核心服务 - 基础功能的支持                    |
| server-main    | 🏁 程序启动入口                            |
| server-member  | 👤 会员服务 - 普通会员和系统管理员分开管理             |
| server-private | 🔒 私有模块 - 闭源私有服务                     |
