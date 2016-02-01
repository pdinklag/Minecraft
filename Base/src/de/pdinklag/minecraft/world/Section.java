package de.pdinklag.minecraft.world;

import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.NBTException;
import de.pdinklag.minecraft.nbt.marshal.NBTCompoundProcessor;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;

/**
 * Represents a chunk section, consisting of 16x16x16 blocks.
 */
@NBTCompoundType
public class Section implements NBTCompoundProcessor {
    private static final int AIR = 0;

    public static final int BLOCKS = Chunk.BLOCKS;
    private static final int BLOCKS_SQ = Chunk.BLOCKS * Chunk.BLOCKS;

    private static byte nib4(byte[] arr, int i) {
        final int x = arr[i / 2] & 0xff;
		//careful that lower index data (even one) will be on the right half-byte
        return (i % 2 != 0) ? (byte) (x >>> 4) : (byte) (x & 0x0F);
    }
    private static byte[] byteArrayToHalfByteArray(byte[] byteArr) {
    	byte [] halfByteArr = new byte[byteArr.length/2];
    	int halfi;
    	for(int i = 0; i < byteArr.length; i++)
    	{
    		halfi = i / 2;
    		assert (byteArr[i] & 0x0F) == byteArr[i];
    		//careful that lower index data (even one) will be on the right half-byte
    		halfByteArr[halfi] |= (i % 2 != 0) ? byteArr[i] << 4 : byteArr[i] ;
    	}
    	return halfByteArr;
    }

    public static int blockInSection(int b) {
        b %= BLOCKS;
        if (b < 0) {
            b += BLOCKS;
        }

        return b;
    }

    private byte y;
    private final Block[][][] blocks = new Block[BLOCKS][BLOCKS][BLOCKS];

    private transient int numNonAir = 0;

    /**
     * Constructs a new, empty section.
     */
    public Section() {
    }

    @Override
    public void unmarshalCompound(CompoundTag nbt) {
        y = nbt.getByte("Y");

        final byte[] data = nbt.getByteArray("Data");
        final byte[] skyLight = nbt.getByteArray("SkyLight");
        final byte[] blockLight = nbt.getByteArray("BlockLight");
        final byte[] blocks = nbt.getByteArray("Blocks");
        
        if ( (data.length != BLOCKS * BLOCKS * BLOCKS / 2)
        		|| (skyLight.length != BLOCKS * BLOCKS * BLOCKS / 2) 
        		|| (blockLight.length != BLOCKS * BLOCKS * BLOCKS / 2) 
        		|| (blocks.length != BLOCKS * BLOCKS * BLOCKS) ) {
        	throw new NBTException("Section has an incorrect byte length");
        }

        final byte[] add;
        if (nbt.contains("Add")) {
            add = nbt.getByteArray("Add");
            if (add.length != BLOCKS * BLOCKS * BLOCKS / 2) {
            	throw new NBTException("Section an incorrect byte length");
            }
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
                        id += 256;
                    }

                    int addValue;
                    if ( (add != null) && ((addValue = nib4(add, i)) > 0 ) ) {
                        id += addValue << 8;
                    }
                    
                    if(i == 3245)
                    {
                    	x = x + 1;
                    	x = x - 1;
                    }

                    if (id != AIR) {
                        final Block block = new Block();
                        block.setType(BlockType.values()[id]);
                        block.setData(nib4(data, i));
                        block.setBlockLight(nib4(blockLight, i));
                        block.setSkyLight(nib4(skyLight, i));
                        this.blocks[x][y][z] = block;

                        numNonAir++;
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag marshalCompound() {
    	assert !isEmpty();
    	
        CompoundTag root = new CompoundTag();

        // transform Block data into section structure data as it is in a region file
        final byte[] data = new byte[BLOCKS*BLOCKS*BLOCKS];
        final byte[] skyLight = new byte[BLOCKS*BLOCKS*BLOCKS];
        final byte[] blockLight = new byte[BLOCKS*BLOCKS*BLOCKS];
        final byte[] byteBlocks = new byte[BLOCKS*BLOCKS*BLOCKS];
        final byte[] add = new byte[BLOCKS*BLOCKS*BLOCKS];
        boolean needAddData = false;

        for (int y = 0; y < BLOCKS; y++) {
            for (int z = 0; z < BLOCKS; z++) {
                for (int x = 0; x < BLOCKS; x++) {
                    final int i = y * BLOCKS_SQ + z * BLOCKS + x;

                    Block block = blocks[x][y][z];
                    
                    if(block != null)
                    {
                    	data[i] = (byte) (block.getData() & 0x0F);
                    	skyLight[i] = block.getSkyLight();
                    	blockLight[i] = block.getBlockLight();

                    	int blockId = block.getType().ordinal();
                    	if((blockId >> 8) > 0) {
                    		add[i] = (byte) (blockId >> 8);
                        	needAddData = true;
                        }
                    	byteBlocks[i] = (byte) (blockId & 0xff);
                    }
                    else {
                    	skyLight[i] = Block.MAX_LIGHT;
                    }

                }
            }
        }
        
        root.put("Blocks", byteBlocks);
        root.put("SkyLight", byteArrayToHalfByteArray(skyLight));
        root.put("Y", y);
        root.put("BlockLight", byteArrayToHalfByteArray(blockLight));
        root.put("Data", byteArrayToHalfByteArray(data));
        if(needAddData) {
            root.put("Add", byteArrayToHalfByteArray(add));
        }

        return root;
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

        if (wasAir && (block != null)) {
            numNonAir++;
        } else if (!wasAir && (block == null)) {
            numNonAir--;
        }
    }

    public byte getY() {
        return y;
    }

    public void setY(byte y) {
        this.y = y;
    }
}
