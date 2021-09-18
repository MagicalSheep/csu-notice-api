# csu-notice-api

![version](https://img.shields.io/badge/version-0.3.0--SNAPSHOT-blue)

**开发中，响应格式不稳定**

解析学院及校内通知的原始HTML文本，将其封装成为API，供外部应用调用。此仓库不提供在线服务，启用API以获取校内通知需自行部署程序，并配置个人信息门户账号。

## API

### 校内通知
- `../main/page`，`POST`，参数: `num`，获取第 `num` 页的所有通知。
- `../main/head`，`GET`，获取通知列表头指针。
- `../main`，`POST`，参数: `head`，获取指针`head`与通知列表头指针之间的所有通知。
- `../main/notice`，`POST`，参数: `id`，获取值为`id`的指针所指向的通知内容。
- `../main/latest`，`GET`，获取最新的一条通知。

### 计算机院通知
- `../cse/page`，`POST`，参数: `num`，获取第 `num` 页的所有通知。
- `../cse/head`，`GET`，获取通知列表头指针。
- `../cse`，`POST`，参数: `head`，获取指针`head`与通知列表头指针之间的所有通知。
- `../cse/notice`，`POST`，参数: `id`，获取值为`id`的指针所指向的通知内容。
- `../cse/latest`，`GET`，获取最新的一条通知。

## 响应格式

所有响应均为`json`格式文本，且均含有API版本号`version`、响应状态`status`和响应信息`msg`三个字段。

通知信息的响应格式待稳定，暂定如下：

```json
{
  "status": 1,
  "msg": "ok",
  "notices": [
    {
      "id": 1,
      "title": "中南大学关于做好2021年下半年全国大学英语四、六级考试报名工作的通知",
      "from": "本科生院",
      "uri": "http://tz.its.csu.edu.cn/Home/Release_TZTG_zd/6B4225064B3448C783EE3F3DA60CF9C0",
      "content": null
    },
    {
      "id": 2,
      "title": "关于2021年中秋节放假安排的通知",
      "from": "学校办公室",
      "uri": "http://tz.its.csu.edu.cn/Home/Release_TZTG_zd/305E554CBFCE428E8A4355ED0831C5A9",
      "content": null
    }
  ],
  "version": 1
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
- `update_num_per_pages`: 每次更新时，从网站上爬取通知的页数（启动时将进行一次更新）
- `init_db`: 是否在启动时更新整个数据库（爬取所有通知）
- `scholl`: 更新时是否爬取校内通知
- `cse`: 更新时是否爬取计算机学院通知