package org.zoomdev.stock.tdx;

public enum BlockType {
    //通达信行业
    TdxIndustry(2),
    Area(3),


    //概念
    Concept(4),

    //风格
    Style(5),


    //申万行业
    SwIndustry(8),


    //指数板块
    Index(9);

    private final int type;

    BlockType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }


}
