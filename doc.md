## 技术方案

1. 采用webDAV 作为云端存储文件，（测试坚果云）
	类似git版本管理，采用最新版本号同步文档

[apache/jackrabbit/webdav 模块](https://github.com/apache/jackrabbit/tree/trunk/jackrabbit-webdav)

[代码实例](https://blog.csdn.net/heraptor/article/details/102983257)

[类似开源库gitgub](https://github.com/tuacy/WebDav)

2. UI仿小米备忘录；

3. 采用 Room 数据库保存 数据

4. 图标采用[阿里巴巴图标素材库](https://www.iconfont.cn/)

5. **使用MVVM 开发**

6. 测试地址：https://dav.jianguoyun.com/dav/davTest
7. 账户：lingluo511@gmail.com
8. 密码：aq9he4arf3ttmve7



## 详细需求

### 首页

首页列表，按日期分类，日期悬浮，右下角【加号按钮】新增笔记

列表条目内容为三行，第一行标题，第二行，内容，过长显示省略，第三行 年月日，时间

点击条目进入笔记输入页面


### 笔记输入页

左上角 后退按钮+ 日期 + 时间

第一行为标题

后面为内容



### 设置页面

设置 webDAV 服务器地址，账号，密钥


每次新建文本 自动同步

下拉刷新，自动比对服务器



