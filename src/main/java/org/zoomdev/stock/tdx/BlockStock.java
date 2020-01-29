package org.zoomdev.stock.tdx;

import java.util.List;

public class BlockStock {

    String code;
    String name;
    int level;
    List<String> codes;
    BlockType type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d (%d)", name, code, level, codes != null ? codes.size() : 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
