package de.pdinklag.minecraft.world;

import de.pdinklag.minecraft.nbt.CompoundTag;

/**
 * Represents a chunk section, consisting of 16x16x16 blocks.
 */
class Section {
    private static final int AIR = 0;

    static final int BLOCKS = Chunk.BLOCKS;
    private static final int BLOCKS_SQ = Chunk.BLOCKS * Chunk.BLOCKS;

    private static byte nib4(byte[] arr, int i) {
        final int x = arr[i / 2];
        return (i % 2 == 0) ? (byte) (x >> 4) : (byte) (x & 0x3F);
    }

    private static int blockInSection(int b) {
        b %= BLOCKS;
        if (b < 0) {
            b += BLOCKS;
        }

        return b;
    }

    private int y;
    private final Block[][][] blocks = new Block[BLOCKS][BLOCKS][BLOCKS];

    private transient int numNonAir = 0;

    public Section() {
    }

    void readNbt(CompoundTag nbt) {
        y = nbt.getByte("Y");

        final byte[] data = nbt.getByteArray("Data");
        final byte[] skyLight = nbt.getByteArray("SkyLight");
        final byte[] blockLight = nbt.getByteArray("BlockLight");
        final byte[] blocks = nbt.getByteArray("Blocks");

        final byte[] add;
        if (nbt.contains("Add")) {
            add = nbt.getByteArray("Add");
        } else {
            add = null;
        }

        numNonAir = 0;
        for (int y = 0; y < BLOCKS; y++) {
            for (int z = 0; z < BLOCKS; z++) {
                for (int x = 0; x < BLOCKS; x++) {
                    final int i = y * BLOCKS_SQ + z * BLOCKS + x;

                    int id = blocks[i];
                    if (id < 0) {
                        id += 128;
                    }

                    if (add != null) {
                        id += nib4(add, i) << 8;
                    }

                    if (id != AIR) {
                        final Block block = new Block();
                        block.setType(BlockType.values()[id]);
                        block.setData(nib4(data, i));
                        block.setBlockLight(nib4(blockLight, i));
                        block.setSkyLight(nib4(skyLight, i));
                        this.blocks[x][y][z] = block;
                    } else {
                        numNonAir++;
                    }
                }
            }
        }
    }

    boolean isEmpty() {
        return (numNonAir == 0);
    }

    Block getBlock(int x, int y, int z) {
        x = blockInSection(x);
        y = blockInSection(y);
        z = blockInSection(z);
        return blocks[x][y][z];
    }

    void setBlock(int x, int y, int z, Block block) {
        x = blockInSection(x);
        y = blockInSection(y);
        z = blockInSection(z);

        boolean wasAir = (blocks[x][y][z] == null);
        blocks[x][y][z] = block;

        if (wasAir && block != null) {
            numNonAir++;
        } else if (!wasAir && block == null) {
            numNonAir--;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
