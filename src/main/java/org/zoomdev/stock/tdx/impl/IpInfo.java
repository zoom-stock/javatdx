package org.zoomdev.stock.tdx.impl;

class IpInfo {


    String host;
    int port;
    String name;
    //从存储中加载失败历史数据和成功历史数据
    int successCount;

    public IpInfo(String host, int port, String name) {
        this.host = host;
        this.port = port;
        this.name = name;
    }

    public IpInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
