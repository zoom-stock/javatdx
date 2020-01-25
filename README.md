
# 通达信客户端java版本

目前仅支持基本行情查询，之后有时间再搞出扩展和交易接口


pythone项目: https://github.com/rainx/pytdx
感谢作者的贡献



# 功能列表

## 基本行情

    + [x] 查询交易品种
    + [x] 查询板块信息
    + [x] 查询k线


## 使用说明


目前有两种使用方式，一种是非多线程非线程安全的方式，直接初始化TxdClient，并以此来运行所有功能，缺点是需要自行维护连接。
另一中是可在多线程环境下使用的方式，本方式可自行维护最快服务器的选择、自动连接、心跳、重连等

#### 单线程方式


```
TdxClient client = new TdxClient();
client.setSocketAddress(new InetSocketAddress("119.147.212.81", 7709));
client.connect();

List<BlockStock> blocks= client.getBlockInfo(BlockType.BLOCK_DEFAULT);
System.out.println(blocks);
```


#### 多线程方式


````
TdxClientService service = new TdxClientService();
service.start();

Future<List<StockInfo>>  list = service.getStockList(Market.sh,0);
List<StockInfo> result = list.get();
System.out.println(result);

service.stop();
````

所有api在多线程模式下均返回Future，用户可酌情处理


# API列表

#### 1、查询交易品种

#### 2、查询板块信息

#### 3、查询k线行情




