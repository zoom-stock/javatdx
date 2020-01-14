package org.zoomdev.stock.tdx.impl;

interface IpRecord {
    //取得host连接成功数量
    IpInfo[] load(IpInfo[] infos);

    //将host连接成功数量保存起来，下次可以用此判断最佳连接路线
    void save(IpInfo[] infos);

}
