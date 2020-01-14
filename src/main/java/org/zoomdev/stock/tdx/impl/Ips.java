package org.zoomdev.stock.tdx.impl;

import java.util.Arrays;
import java.util.Comparator;

class Ips {


    private static final IpInfo[] IP_INFOS = new IpInfo[]{
            new IpInfo("106.120.74.86", 7711, "北京行情主站1"),
            new IpInfo("113.105.73.88", 7709, "深圳行情主站"),
            new IpInfo("113.105.73.88", 7711, "深圳行情主站"),
            new IpInfo("114.80.80.222", 7711, "上海行情主站"),
            new IpInfo("117.184.140.156", 7711, "移动行情主站"),
            new IpInfo("119.147.171.206", 443, "广州行情主站"),
            new IpInfo("119.147.171.206", 80, "广州行情主站"),
            new IpInfo("218.108.50.178", 7711, "杭州行情主站"),
            new IpInfo("221.194.181.176", 7711, "北京行情主站2"),
            new IpInfo("106.120.74.86", 7709),
            new IpInfo("112.95.140.74", 7709),
            new IpInfo("112.95.140.92", 7709),
            new IpInfo("112.95.140.93", 7709),
            new IpInfo("113.05.73.88", 7709),
            new IpInfo("114.67.61.70", 7709),
            new IpInfo("114.80.149.19", 7709),
            new IpInfo("114.80.149.22", 7709),
            new IpInfo("114.80.149.84", 7709),
            new IpInfo("114.80.80.222", 7709),
            new IpInfo("115.238.56.198", 7709),
            new IpInfo("115.238.90.165", 7709),
            new IpInfo("117.184.140.156", 7709),
            new IpInfo("119.147.164.60", 7709),
            new IpInfo("119.147.171.206", 7709),
            new IpInfo("119.29.51.30", 7709),
            new IpInfo("121.14.104.70", 7709),
            new IpInfo("121.14.104.72", 7709),
            new IpInfo("121.14.110.194", 7709),
            new IpInfo("121.14.2.7", 7709),
            new IpInfo("123.125.108.23", 7709),
            new IpInfo("123.125.108.24", 7709),
            new IpInfo("124.160.88.183", 7709),
            new IpInfo("180.153.18.17", 7709),
            new IpInfo("180.153.18.170", 7709),
            new IpInfo("180.153.18.171", 7709),
            new IpInfo("180.153.39.51", 7709),
            new IpInfo("218.108.47.69", 7709),
            new IpInfo("218.108.50.178", 7709),
            new IpInfo("218.108.98.244", 7709),
            new IpInfo("218.75.126.9", 7709),
            new IpInfo("218.9.148.108", 7709),
            new IpInfo("221.194.181.176", 7709),
            new IpInfo("59.173.18.69", 7709),
            new IpInfo("60.12.136.250", 7709),
            new IpInfo("60.191.117.167", 7709),
            new IpInfo("60.28.29.69", 7709),
            new IpInfo("61.135.142.73", 7709),
            new IpInfo("61.135.142.88", 7709),
            new IpInfo("61.152.107.168", 7721),
            new IpInfo("61.152.249.56", 7709),
            new IpInfo("61.153.144.179", 7709),
            new IpInfo("61.153.209.138", 7709),
            new IpInfo("61.153.209.139", 7709),
            new IpInfo("hq.cjis.cn", 7709),
            new IpInfo("hq1.daton.com.cn", 7709),
            new IpInfo("jstdx.gtjas.com", 7709),
            new IpInfo("shtdx.gtjas.com", 7709),
            new IpInfo("sztdx.gtjas.com", 7709),
            new IpInfo("113.105.142.162", 7721),
            new IpInfo("23.129.245.199", 7721)
    };

    public static IpInfo[] getIpInfos(IpRecord record) {

        IpInfo[] infos = new IpInfo[IP_INFOS.length];
        for (int i = 0; i < infos.length; ++i) {
            infos[i] = IP_INFOS[i];
        }
        record.load(infos);
        Arrays.sort(infos, new Comparator<IpInfo>() {
            @Override
            public int compare(IpInfo o1, IpInfo o2) {
                if (o1.successCount > o2.successCount) {
                    return -1;
                }
                if (o1.successCount < o2.successCount) {
                    return 1;
                }
                return 0;
            }
        });

        return infos;
    }


}
