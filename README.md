# JavaProject03_hm-mall-backend
**黑马商城hm-mall微服务练习项目**
=========================
[**项目初始化**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/master)  
[**前端文件**](https://github.com/LMWC/JavaProject03_hm-mall-web)  
[**数据库文件**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/4.%E4%B8%8B%E5%8D%95%E4%B8%9A%E5%8A%A1/MySQL) 

[**1.搭建运行环境**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/1.%E5%95%86%E5%93%81%E7%AE%A1%E7%90%86%E4%B8%9A%E5%8A%A1)  
- 导入SQL文件  
hmall数据库包含tb_address,tb_item,tb_order,tb_order_detail,tb_order_logistics,tb_user,undo_log;seata数据库包含branch_table,global_table,lock_table
- 导入Demo工程  
- 前端页面  
- 启动nacos  
编辑startup.cmd设置set MODE="standalone"，双击startup.cmd启动，输入http://127.0.0.1:8848/nacos访问，默认的账号和密码都是nacos
- 配置网关  

[**2.商品管理业务**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/1.%E5%95%86%E5%93%81%E7%AE%A1%E7%90%86%E4%B8%9A%E5%8A%A1)  
- 分页查询商品  
- 根据id查询商品  
- 新增商品  
- 商品上架、下架   
- 修改商品   
- 根据id删除商品  

[**3.搜索业务**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/2.%E6%90%9C%E7%B4%A2%E4%B8%9A%E5%8A%A1)  
- 创建搜索服务    
- 设计索引库的映射  
下载分词器https://github.com/medcl/elasticsearch-analysis-ik，将文件放到es的plugins目录，并解压之后改名称为IK，并删除zip文件。双击elasticsearch-7.12.1\bin\elasticsearch.bat和kibana-7.12.1-windows-x86_64\bin\kibana.bat启动，输入http://localhost:5601访问，选择右侧按钮点击explore on my own，进入DevTools界面
```bash
# 创建索引和映射，在DevTools界面输入以下内容并运行
PUT /item
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id":{
        "type": "long"
      },
      "name":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart",
        "copy_to": "all"
      },
      "price":{
        "type": "integer"
      },
      "stock":{
        "type": "integer"
      },
      "image":{
        "type": "text"
      },
       "category":{
        "type": "keyword",
        "copy_to": "all"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "spec":{
        "type": "text"
      },
      "sold":{
        "type": "integer"
      },
      "comment_count":{
        "type": "integer"
      },
      "status":{
        "type": "integer"
      },
      "create_time":{
        "type": "text"
      },
      "update_time":{
        "type": "text"
      },
      "all":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart"
      },
      "suggestion":{
          "type": "completion",
          "analyzer": "completion_analyzer",
          "search_analyzer": "ik_smart"
      }
    }
  }
}
```
- 从数据库导入数据到ES中  
```bash
# 导入数据库
在search-service模块中test的SearchApplicationTests测试文件中运行test和importDataToItemIndex（别运行createIndex）
```
- 搜索栏自动补全功能  
- 过滤项聚合功能
- 实现基本搜索功能  
- ES和MYSQL数据同步  
基于RabbitMQ实现数据库、elasticsearch的数据同步  
在VMware Workstation Pro虚拟机centos7中运行docker：systemctl start docker，并在docker中运行rabbitMQ：docker start mq，输入http://192.168.211.132:15672访问，在queue中新建队列item.delete.queue和iitem.delete.queue

[**4.登录用户信息获取**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/3.%E7%99%BB%E5%BD%95%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF%E8%8E%B7%E5%8F%96%26%E7%94%A8%E6%88%B7%E7%9B%B8%E5%85%B3%E4%B8%9A%E5%8A%A1)  
- 给所有请求添加用户身份    
- 微服务获取用户身份进行权限判断   
- Feign请求添加请求头   
[**5.用户相关业务**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/3.%E7%99%BB%E5%BD%95%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF%E8%8E%B7%E5%8F%96%26%E7%94%A8%E6%88%B7%E7%9B%B8%E5%85%B3%E4%B8%9A%E5%8A%A1)  
- 数据结构  
- 根据用户id查询地址列表  
- 根据addressId查询Address    

[**6.下单业务**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/4.%E4%B8%8B%E5%8D%95%E4%B8%9A%E5%8A%A1)  
- 数据结构    
- 提交订单 
在seata-server-1.4.2\conf中编辑registry.conf
```bash
# 编辑registry.conf
registry {
  # tc服务的注册中心类，这里选择nacos，也可以是eureka、zookeeper等
  type = "nacos"

  nacos {
    # seata tc 服务注册到 nacos的服务名称，可以自定义
    application = "seata-server"
    serverAddr = "127.0.0.1:8848"
    group = "DEFAULT_GROUP"
    namespace = ""
    cluster = "DEFAULT"
    username = "nacos"
    password = "nacos"
  }
}

config {
  # 读取tc服务端的配置文件的方式，这里是从nacos配置中心读取，这样如果tc是集群，可以共享配置
  type = "nacos"
  # 配置nacos地址等信息
  nacos {
    serverAddr = "127.0.0.1:8848"
    namespace = ""
    group = "SEATA_GROUP"
    username = "nacos"
    password = "nacos"
    dataId = "seataServer.properties"
  }
}
```
在nacos中新建配置  
```bash
# Data ID
seataServer.properties

# Group
SEATA_GROUP

# 配置内容（以下全部内容）
# 数据存储方式，db代表数据库
store.mode=db
store.db.datasource=druid
store.db.dbType=mysql
store.db.driverClassName=com.mysql.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&rewriteBatchedStatements=true
store.db.user=root
store.db.password=root
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
# 事务、日志等配置
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000

# 客户端与服务端传输方式
transport.serialization=seata
transport.compressor=none
# 关闭metrics功能，提高性能
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898
```
- 清理超时未支付订单（下单了 30分钟还没支付，需要恢复库存，取消订单）    



**运行方式**
=========================
- 打开并运行前端文件中的nginx.exe
- 启动运行nacos  
- 启动运行elasticsearch和kibana  
- 启动运行VMware workstation中的docker和rabbitMQ  
- 启动运行seata  
- 在浏览器中输入前端网址进入黑马商城  


**参考环境**
=========================
- IntelliJ IDEA 2020.1.3 (Ultimate Edition)  
  Non-Bundled Plugins: JBLJavaToWeb, Lombook Plugin, mobi.hsz.idea.gitignore, MavenRunHelper,        com.baomidou.plugin.idea.mybatisx
- maven-3.5.3  
- jdk 1.8.0_162  
- mysql-5.7.29-winx64  
- nginx-1.20.2  
- nacos-server-1.4.1  
- elasticsearch-7.12.1  
- kibana-7.12.1-windows-x86_64  
- seata-server-1.4.2  
- VMware® Workstation 16 Pro 16.1.1  
- Docker version 20.10.12    
- rabbitmq:3.8-management  
