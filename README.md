
<div style="text-align: center;">

<img src="https://ssl.lunadeer.cn:14437/i/2024/03/28/6604de7db2e6f.png" alt="" width="70%">

### [开源地址](https://ssl.lunadeer.cn:14446/zhangyuheng/MiniPlayerTitle) | [文档地址](https://ssl.lunadeer.cn:14448/doc/2/)

</div>

## 简易玩家称号插件

[PlayerTitle](https://ricedoc.handyplus.cn/wiki/PlayerTitle/) 青春版

![image-20240111093304074](https://ssl.lunadeer.cn:14437/i/2024/01/11/659f455088092.png)

## 说明

- 本插件为 [PlayerTitle](https://ricedoc.handyplus.cn/wiki/PlayerTitle/) 的简易版，基本实现了其大部分功能
- 由于历史原因 PlayerTitle 使用玩家名称而非 UUID 作为数据库主键，因此玩家改名后称号会丢失，本插件使用 UUID 作为主键，不会出现该问题

## 功能介绍

- 本插件支持 PlayerTitle 的 [新版RGB颜色格式](https://ricedoc.handyplus.cn/wiki/PlayerTitle/rgb/#新版本格式) （详情见下文）
- 本插件仅支持 PostgresSQL 数据库，不支持 MySQL、Sqlite 等
- 本插件使用 **TUI** 作为简易的交互方式，非 PlayerTitle 的箱子UI
- 支持设置称号**限量销售、限时销售**
- 支持玩家使用称号币自定义称号

## 支持版本

- 1.20.1+ (Paper、Folia)

## 安装方法

1. 将插件放入服务器的 `plugins` 目录下
2. 重启服务器
3. 在 `plugins/MiniPlayerTitle/config.yml` 中配置
4. 重启服务器

## 使用方法

使用 `/mplt` 指令打开 TUI 界面

### 主页

- 点击【称号背包】可查看自己拥有的所有称号；
- 点击【称号商店】可查看商店中出售的称号；
- 点击【自定义称号】可查看自定义称号的相关帮助信息；

![image-20240110164757834](https://ssl.lunadeer.cn:14437/i/2024/01/10/659e59bfc8f7b.png)

### 称号背包

- 称号背包中会显示当前拥有的所有称号以及相应到到期时间。
- 点击【卸下】可以取消当前正在使用的称号，点击【使用】会装备对应称号或替换当前称号。
- 每页最多显示5条信息，可以点击【下一页】向后翻页。
- 鼠标移动到称号上可以显示相应称号的描述信息。
- 称号有效期只能精确到天，例如：20240115表示此称号将在2024年1月16日开始无法使用。

![image-20240110170535331](https://ssl.lunadeer.cn:14437/i/2024/01/10/659e5ddfc16dd.png)

### 称号商店

- 称号商店会显示称号销售列表、拥有的称号币余额、出售称号的价格、可购买天数、销售截止日期、可购买余量信息。

- 每页最多显示5条信息，可以点击【下一页】向后翻页。

- 点击【购买】即可购买相应的称号，称号购买后不会自动装备，需要前往背包手动使用。

- 在一个称号没有过期时无法再次购买此称号。
- 售卖截止日期只能精确到天。

![image-20240110170621073](https://ssl.lunadeer.cn:14437/i/2024/01/10/659e5e0d772ce.png)

### 自定义称号

- 在自定义称号帮助页面会显示当前管理员配置的自定义称号规则。
- 如果自定义称号处于【开启】状态，那么玩家可以消耗称号币自定义喜欢的称号。
- 自定义称号支持RGB彩色字，需要使用 `&#`为开头的十六进制RGB颜色代码表示颜色，例如`&#000000`表示黑色。可以使用 [Minecraft 渐变颜色生成器](https://ssl.lunadeer.cn:14440/) 来生成具有渐变效果的称号。
- 创建成功后会自动扣除所需的花费。

![image-20240111093331929](https://ssl.lunadeer.cn:14437/i/2024/01/11/659f456c4a88b.png)

## 管理员指南

### 1. 创建称号

使用 `/mplt create [称号名称] [称号描述]` 创建称号，称号支持RGB彩色字，需要使用 `&#`为开头的十六进制RGB颜色代码表示颜色，例如`&#000000`表示黑色。可以使用 [Minecraft 渐变颜色生成器](https://ssl.lunadeer.cn:14440/) 来生成具有渐变效果的称号。

![image-20240110172102245](https://ssl.lunadeer.cn:14437/i/2024/01/10/659e617ea7351.png)

创建成功后会显示创建称号的 ID 以及称号名。

### 2. 添加到商店

使用`/mplt addshop [称号ID]` 添加称号到商店，添加成功后会显示添加的商品ID。

添加到商店的默认销售信息为：**0元购买0天、常驻销售、0库存**，需要使用相应指令设置称号的销售信息，此默认配置是无法购买的（因为库存和销售天数都是0）。

### 3. 设置销售信息

首先使用`/mplt setprice [商品ID] [价格] [天数]` 设置称号的价格信息，例如可以设置成 1 个称号币购买 7 天`/mplt setprice [商品ID] 1 7` 。如果你希望这是一个永久称号，那么你可以将天数设置为-1。

接着需要使用`/mplt setamount [商品ID] [数量]` 设置称号出售数量。同理，如果你希望这是一个无限供应的称号，那么可以将数量设置为-1则表示无限量销售。

假如你希望这个称号是一个限时销售的称号可以使用`/mplt setendat [商品ID] [结束时间]` 设置称号结束销售时间，时间格式为 `YYYYMMDD` ，在超过这个日期后玩家将无法购买此称号（但是仍然会显示在商店列表里）。

将销售结束时间设置为-1可以将其重新修改为常驻销售。

### 4. 其他操作

其他操作详见下方的管理员指令说明。

提示：

- 你可以安全的删除一个称号，此操作会自动删除玩家拥有的此称号记录以及商店的相关数据。
- 修改称号的名称、描述也会同步变动玩家已经购买的称号信息。
- 修改商店的购买有效期不会改变玩家已购买的时间。
- 一个称号可以多次被添加到商店，因此你可以将一个称号以多种不同的销售方式添加到商店，例如：1币购买1天，5币购买7天，100币购买永久。

## 指令

### 玩家指令

`/mplt` 打开 TUI 界面

`/mplt use [称号ID]` 使用称号

`/mplt list [页数(可选)]` 查看已拥有的称号

`/mplt shop [页数(可选)]` 查看商店

`/mplt buy [商品ID]` 购买称号

`/mplt custom [称号名称]` 自定义称号

`/mplt custominfo` 查看自定义称号帮助

### 管理员指令

`/mplt create [称号名称] [称号描述]` 创建称号

`/mplt delete [称号ID]` 删除称号

`/mplt setdesc [称号ID] [描述]` 设置称号描述

`/mplt setname [称号ID] [名称]` 设置称号名称

`/mplt addshop [称号ID]` 添加称号到商店

`/mplt removeshop [商品ID]` 从商店移除称号

`/mplt setprice [商品ID] [价格] [天数(-1为永久)]` 设置称号价格

`/mplt setamount [商品ID] [数量(-1为无限)]` 设置称号出售数量

`/mplt setendat [商品ID] [结束时间(格式：YYYYMMDD -1为永久)]` 设置称号销售结束时间

`/mplt listall [页数(可选)]` 查看所有称号

`/mplt addcoin [玩家名称] [数量]` 给玩家添加称号币

`/mplt setcoin [玩家名称] [数量]` 设置玩家称号币数量

## 配置文件参考

```yaml
# 数据库配置
Database:
  Host: localhost
  Port: 5432
  Name: miniplayertitle
  User: miniplayertitle
  Pass: miniplayertitle

# 称号前缀后缀
Prefix: "["
Suffix: "]"

# 自定义称号配置
CustomCost:
  Enabled: true
  Cost: 1000
  MaxLength: 8

# 玩家称号币初始值
DefaultCoin: 0

Debug: false
```

## TODO

## 建议与反馈

Mail: [zhangyuheng@lunadeer.cn](mailto:zhangyuheng@lunadeer.cn)

QQ: 2751268851
