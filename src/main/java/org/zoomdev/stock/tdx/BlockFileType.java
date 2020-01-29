package org.zoomdev.stock.tdx;

public enum BlockFileType {


    BLOCK_ZS("block_zs.dat"),
    BLOCK_FG("block_fg.dat"),
    BLOCK_GN("block_gn.dat"),
    BLOCK_DEFAULT("block.dat");


    private final String name;

    BlockFileType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

