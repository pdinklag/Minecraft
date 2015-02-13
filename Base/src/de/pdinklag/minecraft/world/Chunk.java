package de.pdinklag.minecraft.world;

import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.NBT;

import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a chunk, consisting of 16 Y sections.
 */
class Chunk {
    static final int BLOCKS = 16; //in blocks
    static final int BLOCKS_SQ = BLOCKS * BLOCKS;

    private static int yToSection(int y) {
        return y / Section.BLOCKS;
    }

    private static int xz1D(int x, int z) {
        return z * BLOCKS + x;
    }

    private int x;
    private int z;
    private long lastUpdate;
    private long inhabitedTime;
    private final byte[] biomes = new byte[BLOCKS_SQ];
    private final int[] heightmap = new int[BLOCKS_SQ];
    private boolean lightPopulated;
    private boolean terrainPopulated;
    private final Map<Integer, Section> sections = new TreeMap<>();

    private transient boolean dirty = false;

    /**
     * Constructs a new, empty chunk.
     */
    Chunk() {
    }

    void readNbt(CompoundTag nbt) {
        nbt = nbt.getCompound("Level");

        x = nbt.getInt("xPos");
        z = nbt.getInt("zPos");
        System.arraycopy(nbt.getByteArray("Biomes"), 0, biomes, 0, BLOCKS_SQ);
        System.arraycopy(nbt.getIntArray("HeightMap"), 0, heightmap, 0, BLOCKS_SQ);

        lightPopulated = nbt.getBoolean("LightPopulated");
        if (nbt.contains("TerrainPopulated")) {
            terrainPopulated = nbt.getBoolean("TerrainPopulated");
        }

        lastUpdate = nbt.getLong("LastUpdate");
        inhabitedTime = nbt.getLong("InhabitedTime");

        for (NBT sectionNbt : nbt.getList("Sections")) {
            final Section section = new Section();
            section.readNbt((CompoundTag) sectionNbt);
            sections.put(section.getY(), section);
        }

        dirty = false;
    }

    Block getBlock(int x, int y, int z) {
        final Section section = sections.get(yToSection(y));
        if (section != null) {
            return section.getBlock(x, y, z);
        } else {
            return null;
        }
    }

    void setBlock(int x, int y, int z, Block block) {
        final int secY = yToSection(y);

        Section section = sections.get(secY);
        if (section == null && block != null) {
            section = new Section(); //lazily create new section
            section.setY(secY);
        }

        if (section != null) {
            section.setBlock(x, y, z, block);

            //TODO update heightmap?
            lightPopulated = false; //invalidate lightmap?
            dirty = true;
        }
    }

    boolean isDirty() {
        return dirty;
    }

    void saved() {
        this.dirty = false;
    }

    public boolean isLightPopulated() {
        return lightPopulated;
    }

    public void setLightPopulated(boolean lightPopulated) {
        this.lightPopulated = lightPopulated;
    }

    public boolean isTerrainPopulated() {
        return terrainPopulated;
    }

    public void setTerrainPopulated(boolean terrainPopulated) {
        this.terrainPopulated = terrainPopulated;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getInhabitedTime() {
        return inhabitedTime;
    }

    public void setInhabitedTime(long inhabitedTime) {
        this.inhabitedTime = inhabitedTime;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
