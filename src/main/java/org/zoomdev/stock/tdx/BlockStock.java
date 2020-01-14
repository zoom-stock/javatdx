package org.zoomdev.stock.tdx;

import java.util.List;

public class BlockStock {


    String blockName;
    int blockType;
    List<String> codes;

    @Override
    public String toString() {
        return String.format("%s %d (%d)", blockName, blockType, codes != null ? codes.size() : 0);
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public int getBlockType() {
        return blockType;
    }

    public void setBlockType(int blockType) {
        this.blockType = blockType;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
