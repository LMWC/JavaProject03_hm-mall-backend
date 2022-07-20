# JavaProject03_hm-mall-backend
**黑马商城hm-mall微服务练习项目**
=========================
[**项目初始化**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/master)  
[**前端文件**](https://github.com/LMWC/JavaProject03_hm-mall-web)  
[**1.搭建运行环境**](https://github.com/LMWC/JavaProject03_hm-mall-backend/tree/1.%E5%95%86%E5%93%81%E7%AE%A1%E7%90%86%E4%B8%9A%E5%8A%A1)  
- 导入SQL文件  
- 导入Demo工程  
- 前端页面  
- 启动nacos  
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
- 从数据库导入数据到ES中  
- 搜索栏自动补全功能  
- 过滤项聚合功能
- 实现基本搜索功能  
- ES和MYSQL数据同步    
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
