# csu-notice-api

![version](https://img.shields.io/badge/version-0.6.5--SNAPSHOT-blue)

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

首次运行后将在运行目录下生成`settings.json`，该文件为配置文件。

```json
{
  "user": "exampleUser",
  "pwd": "examplePassword",
  "root_uri": "http://tz.its.csu.edu.cn",
  "cse_uri": "https://cse.csu.edu.cn/index/tzgg.htm",
  "chrome_path": "exampleChromePath",
  "update_num_per_pages": 5,
  "init_db": false,
  "school": true,
  "cse": false
}
```

- `user`: 信息门户学工号
- `pwd`: 信息门户密码
- `root_uri`: 校内通知官网地址
- `cse_uri`: 计算机学院官网地址
- `chrome_path`: Chrome浏览器地址（用于截取网页图片）
- `update_num_per_pages`: 每次更新时，从网站上爬取通知的页数（启动时将进行一次更新）
- `init_db`: 是否在启动时更新整个数据库（爬取所有通知）
- `school`: 更新时是否爬取校内通知
- `cse`: 更新时是否爬取计算机学院通知
