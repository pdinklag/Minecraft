package de.pdinklag.minecraft.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a Minecraft world.
 */
public class World {
    private static final Logger LOGGER = Logger.getLogger("World");

    private static long regionToKey(int x, int z) {
        return ((long) x << 32) | (long) z;
    }

    private static int blockToRegion(int b) {
        return b / Region.BLOCKS;
    }

    private final Path baseDir;
    private final Map<Long, Region> regions = new TreeMap<>();

    /**
     * Constructs a new, empty world.
     *
     * @param baseDir the world's base directory on the file system.
     */
    public World(Path baseDir) {
        this.baseDir = Objects.requireNonNull(baseDir);
    }

    private Path getRegionFile(int x, int z) {
        return baseDir.resolve("region/r." + x + "." + z + ".mca");
    }

    private Region getRegionAt(int x, int z) {
        final int regionX = blockToRegion(x);
        final int regionZ = blockToRegion(z);
        final long regionKey = regionToKey(regionX, regionZ);

        Region region = regions.get(regionKey);
        if (region == null) {
            final Path regionFile = getRegionFile(regionX, regionZ);
            if (Files.exists(regionFile)) {
                try {
                    LOGGER.log(Level.INFO, "Initializing region " + regionFile.toString());
                    region = new Region(regionFile);
                } catch (IOException ex) {
                    throw new WorldException("Failed to initialize region from " + regionFile.toString(), ex);
                }
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
     * Sets the block at the specified position.
     *
     * @param x     the block's X coordinate in the world.
     * @param y     the block's Y coordinate in the world.
     * @param z     the block's Z coordinate in the world.
     * @param block the new block for the specified positon. May be {@code null} to indicate air.
     */
    public void setBlock(int x, int y, int z, Block block) {
        if (block != null && block.getType() == BlockType.AIR) {
            block = null; //don't actually save air blocks...
        }

        getRegionAt(x, z).setBlock(x, y, z, block);
    }

    /**
     * Saves dirty regions back to the file system.
     */
    public void save() throws IOException {
        for (Region region : regions.values()) {
            if (region.isDirty()) {
                region.save();
            }
        }
    }
}
