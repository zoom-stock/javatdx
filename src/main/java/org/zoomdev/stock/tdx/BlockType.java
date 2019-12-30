package org.zoomdev.stock.tdx;

public enum BlockType {



    BLOCK_SZ ("block_zs.dat"),
    BLOCK_FG ("block_fg.dat"),

    BLOCK_GN ( "block_gn.dat"),
    BLOCK_DEFAULT ( "block.dat");


    private final String name;

    BlockType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}

