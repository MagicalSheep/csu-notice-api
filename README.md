# csu-notice-api

![version](https://img.shields.io/badge/version-0.7.0--SNAPSHOT-blue)

**开发中，响应格式不稳定**

解析学院及校内通知的原始HTML文本，将其封装成为API，供外部应用调用。此仓库不提供在线服务，启用API以获取校内通知需自行部署程序，并配置个人信息门户账号。

## API

### 可用接口

- `../{tag}/page`，`GET`，参数: `num`，获取第 `num` 页的所有通知。
- `../{tag}/head`，`GET`，获取通知列表头指针。
- `../{tag}`，`GET`，参数: `head`，获取指针`head`与通知列表头指针之间的所有通知。
- `../{tag}/notice`，`GET`，参数: `id`，获取值为`id`的指针所指向的通知信息。
- `../{tag}/latest`，`GET`，获取最新的一条通知。
- `../{tag}/search`，`GET`，参数：`title`，获取标题含有`title`字符串的所有通知。
- `../{tag}/content`，`GET`，参数：`id`，获取值为`id`的指针所指向的通知内容图片（Base64编码）。
- `../{tag}/reload`，`GET`，参数：`id`、`token`，强制重新加载值为`id`的指针所指向的通知内容图片，该操作需要校验`token`值，传入值与服务端预存值相同时才可进行加载。

### 可用标签（tag）

- 校内通知：`main`
- 计算机院通知：`cse`

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
chrome_path=C:/Program Files/Google/Chrome/Application/chrome.exe
initialization=false
update_school_notice=true
update_pages_num=5
token=example token
```

- `cse_notice_url`: 计算机学院通知公告网址
- `update_cse_notice`: 更新时是否获取计算机学院通知
- `school_notice_url`: 校内通知网址
- `password`: 信息门户密码
- `max_thread_nums`: 获取通知内容时使用的最大线程数
- `user_name`: 信息门户学工号
- `chrome_path`: Chrome浏览器路径（用于截取网页图片）
- `initialization`: 是否在启动时更新整个数据库（获取所有通知）
- `update_school_notice`: 更新时是否获取校内通知
- `update_pages_num`: 每次更新时，从网站上获取通知的页数
- `token`: 服务端预存的`token`值
