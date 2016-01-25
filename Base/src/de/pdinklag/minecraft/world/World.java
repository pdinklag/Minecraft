package de.pdinklag.minecraft.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.NBT;
import de.pdinklag.minecraft.nbt.marshal.NBTMarshal;

/**
 * Represents a Minecraft world.
 */
public class World {
    private static final Logger LOGGER = Logger.getLogger("World");
    
    private static long regionToKey(int x, int z) {
        return ((long) x << 32) | ((long) z & 0xffffffffL);
    }
    private static int regionKeyX(long key) {
        return (int) (key >>> 32);
    }
    private static int regionKeyZ(long key) {
        return (int) (key & 0xffffffffL);
    }

    private static int blockToRegion(int b) {
        return (int) Math.floorDiv(b, Region.BLOCKS);
    }

    private final Path baseDir;
    private final Map<Long, Region> regions = new TreeMap<>();
    private de.pdinklag.minecraft.world.Level level;

    /**
     * Load or construct a new, empty world.
     *
     * @param baseDir the world's base directory on the file system.
     * @throws IOException 
     */
    public World(Path baseDir) throws IOException {
        this.baseDir = Objects.requireNonNull(baseDir);
        
        if ( !loadLevelFile(baseDir.resolve("level.dat")) ) {
        	//create default level
        	level = new de.pdinklag.minecraft.world.Level();
        }
    }
    
    private void saveLevelFile(Path levelFilePath) throws IOException {
    	LOGGER.log(Level.INFO,"saving level file at " + levelFilePath.toString());
        try (	final DataOutputStream outputStream = new DataOutputStream (new FileOutputStream(levelFilePath.toString())) ) {
	        CompoundTag levelDataNbt = (CompoundTag) NBTMarshal.marshal(level);
	        CompoundTag levelNbt = new CompoundTag();
	        levelNbt.put("Data", levelDataNbt);
	        
			NBT.save(outputStream, levelNbt, "java.util.zip.GZIPOutputStream");
        } catch (ClassNotFoundException e) {
			assert false;
			return;
		}
        level.saved();
    }

    private boolean loadLevelFile(Path levelFilePath) throws IOException {
        if (!Files.exists(levelFilePath)) {
        	return false;
        }

        try (	final DataInputStream inputStream = new DataInputStream (new FileInputStream(levelFilePath.toString())) ) {
	        CompoundTag levelDataNbt = NBT.load(inputStream);
	        level = NBTMarshal.unmarshal(de.pdinklag.minecraft.world.Level.class, levelDataNbt.getCompound("Data"));
        }
        
        return true;
    }

    /**
     * Saves dirty regions back to the file system. and saves level file if needed
     */
    public void save() throws IOException {
        for (Region region : regions.values()) {
            if (region.isDirty()) {
                region.save();
            }
        }
        if (level.isDirty()) {
        	saveLevelFile(baseDir.resolve("level.dat"));
        }
    }
    
    /**
     * Saves regions to the file system at the specified Path.
     * 
     * @param path
     * @throws IOException
     */
    public void save(Path path) throws IOException {
    	int regionX, regionZ;
    	Path regionFile;
    	File regionDirectory;
    	for (Entry<Long, Region> entry : regions.entrySet()) {
            regionX = regionKeyX(entry.getKey());
            regionZ = regionKeyZ(entry.getKey());
            regionFile = getRegionFile(path, regionX, regionZ);
            regionDirectory = regionFile.getParent().toFile();
			if (!regionDirectory.exists()) {
				regionDirectory.mkdir();
		    }
            try {
            	LOGGER.log(Level.INFO, "saving region " + regionFile.toString());
            	entry.getValue().save(regionFile);
            } catch (IOException ex) {
            	throw new WorldException("Failed to save region at " + regionFile.toString(), ex);
            }
        }
    	
       	saveLevelFile(path.resolve("level.dat"));
    }
    
    public de.pdinklag.minecraft.world.Level getLevel() {
    	return level;
    }
        
    
    private Path getRegionFile(int x, int z) {
        return getRegionFile(baseDir,x,z);
    }
    private Path getRegionFile(Path worldDir, int x, int z) {
        return worldDir.resolve("region/r." + x + "." + z + ".mca");
    }

    public Region getRegionAt(int x, int z) {
        final int regionX = blockToRegion(x);
        final int regionZ = blockToRegion(z);
        final long regionKey = regionToKey(regionX, regionZ);

        Region region = regions.get(regionKey);
        if (region == null) {
            final Path regionFile = getRegionFile(regionX, regionZ);
            try {
                LOGGER.log(Level.INFO, "Initializing region " + regionFile.toString());
                region = new Region(regionFile);
            } catch (IOException ex) {
                throw new WorldException("Failed to initialize region from " + regionFile.toString(), ex);
            }

            regions.put(regionKey, region);
        }

        return region;
    }

    /**
     * Gets the block at the specified position.
     *
     * @param x the block's X coordinate in the world.
     * @param y the block's Y coordinate in the world.
     * @param z the block's Z coordinate in the world.
     * @return the block at the specified positon, guaranteed to be non-null.
     */
    public Block getBlock(int x, int y, int z) {
        final Block block = getRegionAt(x, z).getBlock(x, y, z);
        return (block != null) ? block : Block.AIR_BLOCK;
    }

    /**
     * tells if a block is defined at the specified position.
     *
     * @param x the block's X coordinate in the world.
     * @param y the block's Y coordinate in the world.
     * @param z the block's Z coordinate in the world.
     * @return true if a block is defined.
     */
    public boolean hasBlock(int x, int y, int z) {
        return getRegionAt(x, z).getBlock(x, y, z) != null;
    }

    /**
     * Sets the block at the specified position.
     *
     * @param x     the block's X coordinate in the world.
     * @param y     the block's Y coordinate in the world.
     * @param z     the block's Z coordinate in the world.
     * @param block the new block for the specified position. May be {@code null} to indicate air.
     */
    public void setBlock(int x, int y, int z, Block block) {
        if (block != null && block.getType() == BlockType.AIR) {
            block = null; //don't actually save air blocks...
        }

        getRegionAt(x, z).setBlock(x, y, z, block);
    }
    
    /**
     * Unload regions from memory
     */
	public void unloadRegions() {
		for (Entry<Long, Region> entry : regions.entrySet()) {
			assert !entry.getValue().isDirty();
		}
		regions.clear();
	}
}
