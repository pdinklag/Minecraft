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

    private static int yToSection(int y) {
        return y / Section.BLOCKS;
    }

    private int x;
    private int z;
    private final byte[] biomes = new byte[BLOCKS * BLOCKS];
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
        System.arraycopy(nbt.getByteArray("Biomes"), 0, biomes, 0, BLOCKS * BLOCKS);

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
            dirty = true;
        }
    }

    boolean isDirty() {
        return dirty;
    }

    void saved() {
        this.dirty = false;
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
