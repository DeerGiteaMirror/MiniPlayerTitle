<div align="center">

<img src="https://ssl.lunadeer.cn:14437/i/2024/03/28/6604de7db2e6f.png" alt="" width="70%">

### [开源地址](https://ssl.lunadeer.cn:14446/zhangyuheng/MiniPlayerTitle) | [文档地址](https://ssl.lunadeer.cn:14448/doc/2/)

### [下载页面](https://ssl.lunadeer.cn:14446/zhangyuheng/MiniPlayerTitle/releases)

### [统计页面](https://bstats.org/plugin/bukkit/MiniPlayerTitle/21444) | [Hangar](https://hangar.papermc.io/zhangyuheng/MiniPlayerTitle)

</div>

## 简易玩家称号插件

[PlayerTitle](https://ricedoc.handyplus.cn/wiki/PlayerTitle/) 青春版

![image-20240111093304074](https://ssl.lunadeer.cn:14437/i/2024/01/11/659f455088092.png)

## 说明

- 本插件为 [PlayerTitle](https://ricedoc.handyplus.cn/wiki/PlayerTitle/) 的简易版，基本实现了其大部分功能
- 由于历史原因 PlayerTitle 使用玩家名称而非 UUID 作为数据库主键，因此玩家改名后称号会丢失，本插件使用 UUID 作为主键，不会出现该问题

## 功能介绍

- 本插件支持 PlayerTitle 的 [新版RGB颜色格式](https://ricedoc.handyplus.cn/wiki/PlayerTitle/rgb/#新版本格式) （详情见下文）
- 支持 PostgresSQL 和 Sqlite 数据库
- 本插件使用 **TUI** 作为简易的交互方式，非 PlayerTitle 的箱子UI
- 支持设置称号**限量销售、限时销售**
- 支持玩家使用称号币自定义称号
- 支持外部经济系统（需要 Vault 前置支持）（3.0.6+）
- 支持 PlaceholderAPI (4.0.0+)

## 支持版本

- 1.20.1+ (Bukkit、Spigot、Paper、Folia)

## TODO

## 建议与反馈

Mail: [zhangyuheng@lunadeer.cn](mailto:zhangyuheng@lunadeer.cn)

QQ群：309428300

## 统计

![bstats](https://bstats.org/signatures/bukkit/MiniPlayerTitle.svg)