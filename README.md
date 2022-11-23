# CSU Notice API

[![License](https://img.shields.io/github/license/MagicalSheep/csu-notice-api)](LICENSE)
![version](https://img.shields.io/badge/version-0.8.1--SNAPSHOT-blue)

**已停止开发，实现不优雅且具有严重性能问题，仅供学习参考**

解析学院及校内通知的原始HTML文本，将其封装成为API，供外部应用调用。此仓库不提供在线服务，启用API以获取校内通知需自行部署程序，并配置个人信息门户账号。

*客户端参考实现：[csu-notice-bot](https://github.com/j1g5awi/csu-notice-bot "csu-notice-bot")*

## 部署

1. 填写`application.properties`文件中的数据库源及账号密码。
2. 运行服务端程序，生成`settings.properties`配置文件。
3. 根据服务器的硬件配置，在`settings.properties`文件中填写合适的线程并发数。
4. 运行服务端程序

## API

### 可用接口

- `../{tag}/page/{num}`，`GET`，获取第 `num` 页的所有通知。
- `../{tag}/head`，`GET`，获取通知列表头指针。
- `../{tag}/fetch/{head}`，`GET`，获取指针`head`与通知列表头指针之间的所有通知。
- `../{tag}/notice/{id}`，`GET`，获取值为`id`的指针所指向的通知信息。
- `../{tag}/latest`，`GET`，获取最新的一条通知。
- `../{tag}/search/{title}`，`GET`，获取标题含有`title`字符串的所有通知。
- `../{tag}/content/{id}`，`GET`，获取值为`id`的指针所指向的通知内容图片（Base64编码）。
- `../{tag}/reload/{token}/{id}`，`GET`，强制重新加载值为`id`的指针所指向的通知内容图片，该操作需要校验`token`值，传入值与服务端预存值相同时才可进行加载。

### 可用标签（tag）

- 校内通知：`main`
- 计算机学院：`cse`
- 校长信箱：`mail`
- 学工网：`xgw`

## 响应格式

所有响应均为`json`格式文本，且均含有API版本号`version`、响应状态`status`和响应信息`msg`三个字段。

### 通知信息

```json
{
  "msg": "ok",
  "data": [
    {
      "id": 1,
      "title": "关于认真做好2021年教育事业统计工作的通知",
      "from": "学校办公室",
      "uri": "http://tz.its.csu.edu.cn/Home/Release_TZTG_zd/2C6CB96118B04E98AB62C2F9F22904F8",
      "createTime": "2021-10-14T12:22:37.305+00:00",
      "updateTime": "2021-10-14T12:22:37.445+00:00"
    },
    {
      "id": 2,
      "title": "关于着手准备学校第四次党代会报告的通知",
      "from": "学校办公室",
      "uri": "http://tz.its.csu.edu.cn/Home/Release_TZTG_zd/ED71487900F540DF889B7DF89A35EF21",
      "createTime": "2021-10-14T12:22:37.305+00:00",
      "updateTime": "2021-10-14T12:22:37.455+00:00"
    }
  ],
  "version": 2,
  "status": 200
}
```

### 通知内容

```json
{
  "msg": "ok",
  "data": {
    "id": 1,
    "uri": "http://tz.its.csu.edu.cn/Home/Release_TZTG_zd/2C6CB96118B04E98AB62C2F9F22904F8",
    "content": "Base64 code example",
    "createTime": "2021-10-14T12:23:36.042+00:00",
    "updateTime": "2021-10-14T12:23:40.987+00:00"
  },
  "version": 2,
  "status": 200
}
```

## 配置文件

首次运行后将在运行目录下生成`settings.properties`，该文件为配置文件。

```properties
#CSU Notice API Configuration
#Created By MagicalSheep
#Sat Oct 16 22:46:01 CST 2021
cse_notice_url=https://cse.csu.edu.cn/index/tzgg.htm
update_cse_notice=false
school_notice_url=http://tz.its.csu.edu.cn
password=example password
max_thread_nums=2
user_name=8200000000
initialization=false
update_school_notice=true
update_pages_num=5
token=example token
headmaster_mail_url=http\://oa.its.csu.edu.cn/webserver/mailbox/MailList_Pub.aspx
update_headmaster_mail=false
xgw_notice_url=https://xgw.csu.edu.cn/tzgg.htm
update_xgw_notice=false
```

- `cse_notice_url`: 计算机学院通知公告网址
- `update_cse_notice`: 更新时是否获取计算机学院通知
- `school_notice_url`: 校内通知网址
- `password`: 信息门户密码
- `max_thread_nums`: 获取通知内容时使用的最大线程数
- `user_name`: 信息门户学工号
- `initialization`: 是否在启动时更新整个数据库（获取所有通知）
- `update_school_notice`: 更新时是否获取校内通知
- `update_pages_num`: 每次更新时，从网站上获取通知的页数
- `token`: 服务端预存的`token`值
- `headmaster_mail_url`: 校长信箱网址
- `update_headmaster_mail`: 更新时是否获取校长信箱信息
- `xgw_notice_url`: 学工网网址
- `update_xgw_notice`: 更新时是否获取学工网通知
