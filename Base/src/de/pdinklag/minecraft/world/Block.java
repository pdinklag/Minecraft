package de.pdinklag.minecraft.world;

/**
 * Represents a single block.
 */
public class Block {
    static final Block AIR_BLOCK = new Block();

    private BlockType type = BlockType.AIR;
    private byte data = 0;

    private byte blockLight = 0;
    private byte skyLight = 0;

    public Block() {
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public byte getBlockLight() {
        return blockLight;
    }

    public void setBlockLight(byte blockLight) {
        this.blockLight = blockLight;
    }

    public byte getSkyLight() {
        return skyLight;
    }

    public void setSkyLight(byte skyLight) {
        this.skyLight = skyLight;
    }

    @Override
    public String toString() {
        return type.toString().toLowerCase() + ":" + data ;
    }
}
