# local-server

基于本地文件系统的存储服务

提供接口信息如下：

| 方法   | 接口                           | 说明                                     |
| ------ | ------------------------------ | ---------------------------------------- |
| GET    | /v1/metadatas/{id}             | 指定一个对象ID，返回对象的元数据信息     |
| GET    | /v1/metadatas                  | 指定多个对象ID，返回所有对象的元数据信息 |
| PUT    | /v1/metadatas/{id}/name        | 指定一个对象ID，修改对象元数据name的信息 |
| POST   | /v1/files/upload               | 上传文件                                 |
| GET    | /v1/files/download/{objectId}  | 下载单个文件                             |
| GET    | /v1/files/download/{objectIds} | 下载多个文件                             |
| DELETE | /v1/files/{objectId}           | 删除对象                                 |

#### 配置

修改配置文件`src/resources/application.yml`，配置数据库。

创建数据库及用户脚本：
```
CREATE DATABASE IF NOT EXISTS oss DEFAULT CHARSET utf8;
create user 'ossuser'@'%' identified by 'ossuser';
grant create,alter,drop,select,insert,update,delete on oss.* to ossuser@'%';
flush privileges; 
```

#### 打包

编译，测试，打包：

```
mvn clean install spring-boot:repackage
```

#### 运行

``` 
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```


