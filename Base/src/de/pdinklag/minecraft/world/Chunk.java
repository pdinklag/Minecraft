package de.pdinklag.minecraft.world;

import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.NBT;
import de.pdinklag.minecraft.nbt.marshal.NBTCompoundProcessor;
import de.pdinklag.minecraft.nbt.marshal.NBTMarshal;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;

import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a chunk, consisting of 16 Y sections.
 */
@NBTCompoundType
public class Chunk implements NBTCompoundProcessor {
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
    public Chunk() {
    }

    @Override
    public void unmarshalCompound(CompoundTag nbt) {
        x = nbt.getInt("xPos");
        z = nbt.getInt("zPos");
        System.arraycopy(nbt.getByteArray("Biomes"), 0, biomes, 0, BLOCKS * BLOCKS);

        for (NBT sectionNbt : nbt.getList("Sections")) {
            final Section section = NBTMarshal.unmarshal(Section.class, sectionNbt);
            sections.put(section.getY(), section);
        }

        dirty = false;
    }

    @Override
    public CompoundTag marshalCompound() {
        throw new UnsupportedOperationException("not yet implemented");
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

            //TODO check heightmap, invalidate light information
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
