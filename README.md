# DatabaseCourseDesign

湖南科技大学(HNUST)数据库系统课程设计项目

完成时间：2024.11.22

这是Kitra的数据库系统课程设计项目，现将其开源，以供其他同学们参考。

本项目依据MIT许可证开源，若对该项目任何有疑问或改进建议，请自由[提交Issue](https://github.com/KitraMGP/DatabaseCourseDesign/issues)或[提交Pull Request](https://github.com/KitraMGP/DatabaseCourseDesign/pulls)。

## 课程设计项目简介

企业的不断学习能够帮助企业更快地适应市场环境的飞速变化，通过有效地培训企业员工，赋予员工学习专业技能的机会与能力，企业可以迅速根据市场需求变化，调整分配企业组织的人力资源分布，形成高效的企业组织单元，更好地完成企业运作任务。培训已经成为企业现代化的重要标志。

本课程设计项目的目的就是基于以上的现状，开发一款员工培训管理系统软件，实现员工培训的管理。

- 本系统的功能需求：[员工培训管理题目要求.pdf](./docs/员工培训管理系统题目要求.pdf)。
- 系统完整功能描述和设计思路：[课程设计报告（功能说明、设计思路）.pdf](./docs/课程设计报告（功能说明、设计思路）.pdf)。
- 完整的课程设计指导书：[本部_2024-2025-1数据库系统课程设计指导书.pdf](./docs/本部_2024-2025-1数据库系统课程设计指导书.pdf)

为了让不熟悉相关技术的新手能够成功运行这个系统，本文档将尽量详细描述相关步骤，并提供相关教程。读者可根据自身情况，在阅读本文时适当跳读。

## 本项目用到的主要技术

本系统用到的主要技术有：
- 构建工具：使用[Gradle](https://gradle.org/)进行依赖管理和自动构建
- 用户界面实现：使用Java Swing进行开发（参考[Swing教程](https://www.xinbaoku.com/archive/04cEFxC6.html)）
- 数据库：使用MySQL 8.4（[MySQL下载地址](https://dev.mysql.com/downloads/mysql/)）
- 数据库访问：使用[MyBatis](https://mybatis.net.cn/)来访问数据库（参考[MyBatis教程](https://zhuanlan.zhihu.com/p/351830443)）

要进行开发和调试，你可能需要以下工具或软件：
- JDK：Java 17+，推荐使用[Azul Zulu OpenJDK](https://www.azul.com/downloads/?version=java-17-lts&architecture=x86-64-bit&package=jdk#zulu)
- IDE：IntelliJ IDEA
- 数据库图形化访问工具，例如[Navicat Premium Lite（免费）](https://www.navicat.com.cn/download/navicat-premium-lite)

关于本项目的详细介绍、功能特性、功能演示等，请查阅上文提到的“课程设计报告”PDF文档。

## 构建和运行

### 必要的准备工作

- 导入项目：在 IntelliJ IDEA中将该文件夹作为Gradle项目打开，等待Gradle项目导入完成。（若网络原因导致Gradle项目导入速度太慢，请设置代理服务器，或上网搜索Gradle Wrapper和Gradle Maven仓库更换国内源的方法）
- 初始化数据库：启动MySQL服务，然后在Navicat中连接到你的MySQL服务，并新建一个数据库供本系统使用。
- 建立初始数据表：在Navicat中右键刚刚创建的数据库，点击“运行SQL文件...”，选择项目根目录的“初始数据.sql”文件，将初始数据导入到数据库中。

### 设置数据库参数

要让客户端程序正确连接到数据库，还要对MySQL数据库端口、名称、用户名和密码进行设置。

打开`./src/main/resources/db.properties`文件，编辑其内容：

```
driver=com.mysql.cj.jdbc.Driver
# 这里可以修改数据库端口和数据库的名称
url=jdbc:mysql://localhost:3306/database_course_design?useUnicode=true&characterEncoding=UTF-8
# 这里设置数据库连接用户名和密码
username=root
password=123456
```

### 运行

运行主类Main中的主函数，启动软件。

第一次启动软件时，登录用户名为root，密码留空，系统会引导你重新设置密码，设置完成后即可进入系统。

### 打包和发布

你可以将这个系统的客户端程序及其依赖项进行打包，以便于将程序分享给其他人。

在IDEA界面右侧的Gradle面板中运行Tasks -> distribution -> distZip任务，或者是在项目根目录执行`gradlew distZip`命令。任务执行完毕后，在`./build/distributions/`目录中可以看到生成好的程序压缩包。

该压缩包解压后，运行bin文件夹中的DatabaseCourseDesign.bat批处理文件即可启动软件。

## 忘记管理员密码的解决方法

该软件中用户的登录密码以加密形式存储，无法直接找回用户密码。

普通用户的密码可以通过管理员界面直接修改，若无法登录管理员账户，可用删除密码的方式来重置密码。

用Navicat打开数据库，找到`person`表，将要重置密码的用户（`authority`值为0的用户为管理员用户）的`passwd`内容清空。

完成操作后，打开登录界面，密码留空，登录该用户，系统会让你设置新密码，然后即可进入系统。
