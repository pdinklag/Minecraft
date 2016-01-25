package de.pdinklag.minecraft.world;

import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.ListTag;
import de.pdinklag.minecraft.nbt.NBT;
import de.pdinklag.minecraft.nbt.marshal.NBTCompoundProcessor;
import de.pdinklag.minecraft.nbt.marshal.NBTMarshal;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;

import java.util.Date;
import java.util.TreeMap;

/**
 * Represents a chunk, consisting of 16 Y sections.
 */
@NBTCompoundType
public class Chunk implements NBTCompoundProcessor {
    static final int BLOCKS = 16; //in blocks
    static final int BLOCKS_SQ = BLOCKS * BLOCKS;
    
    static final byte BIOME_DEFAULT = Biome.PLAINS;
    
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
    private final byte[] biomes = new byte[BLOCKS_SQ];;
    private final int[] heightmap = new int[BLOCKS_SQ];
    private boolean lightPopulated;
    private boolean terrainPopulated;
    private final TreeMap<Integer, Section> sections = new TreeMap<>();

    private transient boolean dirty = false;

    /**
     * Constructs an empty chunk structure, destined to be filled by unmarshalling data.
     */
    public Chunk() {
    }

    /**
     * Constructs a new chunk with basic data.
     */
    public Chunk(int x, int z) {
    	this.x = x;
    	this.z = z;
    	terrainPopulated = true;
    	lightPopulated = false;
    	//set default biome
    	for (int i = 0; i < BLOCKS_SQ; i++) {
    		biomes[i] = BIOME_DEFAULT;
    	}
    }

    @Override
    public void unmarshalCompound(CompoundTag nbt) {
    	try {
	        x = nbt.getInt("xPos");
	        z = nbt.getInt("zPos");
	        if (nbt.contains("Biomes")) {
	        	System.arraycopy(nbt.getByteArray("Biomes"), 0, biomes, 0, BLOCKS_SQ);
	        }
	        System.arraycopy(nbt.getIntArray("HeightMap"), 0, heightmap, 0, BLOCKS_SQ);
	
	        lightPopulated = nbt.getBoolean("LightPopulated");
	        if (nbt.contains("TerrainPopulated")) {
	            terrainPopulated = nbt.getBoolean("TerrainPopulated");
	        }
	        else {
	        	terrainPopulated = true;
	        }
	
	        lastUpdate = nbt.getLong("LastUpdate");
	        if (nbt.contains("InhabitedTime")) {
	        	inhabitedTime = nbt.getLong("InhabitedTime");
	        }
	        else {
	        	//chunk does not contain InhabitedTime as it should, but minecraft accept this so we do too
	        	inhabitedTime = 0;
	        }
	
	        for (NBT<?> sectionNbt : nbt.getList("Sections")) {
	            final Section section = NBTMarshal.unmarshal(Section.class, sectionNbt);
	            sections.put(section.getY(), section);
	        }
	        
	        //TODO : entity loading
	        
	        //TODO: read block entities

	        dirty = false;
    	}
    	catch (Exception e) {
	        throw new WorldException("malformed chunk", e);
    	}
    }

    @Override
    public CompoundTag marshalCompound() {
        CompoundTag root = new CompoundTag();
        root.put("xPos", x);
        root.put("zPos", z);
       	root.put("Biomes", biomes);
        root.put("HeightMap", heightmap);
        root.put("LightPopulated", lightPopulated);
        root.put("TerrainPopulated", terrainPopulated);
        //we consider the marshalling as the trigger for updating the lastUpdate field
    	Date date= new Date();
        lastUpdate = date.getTime();
        root.put("LastUpdate", lastUpdate);
        root.put("InhabitedTime", inhabitedTime);
        //create sections as NBT
        ListTag sectionListNbt = new ListTag();
        for(Section section: sections.values()) {
        	if(!section.isEmpty()) {
        		sectionListNbt.add(NBTMarshal.marshal(section));
        	}
        }
        root.put("Sections", sectionListNbt);
        //TODO: save block entities
        root.put("TileEntities", new ListTag());
        //TODO: save entities
        ListTag entityListNbt = new ListTag();
        root.put("Entities", entityListNbt);

        return root;
    }

    public Block getBlock(int x, int y, int z) {
        final Section section = sections.get(yToSection(y));
        if (section != null) {
            return section.getBlock(x, y, z);
        } else {
            return null;
        }
    }

    public void setBlock(int x, int y, int z, Block block) {
        final int secY = yToSection(y);

        Section section = sections.get(secY);
        if (section == null) {
            section = new Section(); //lazily create new section
            section.setY(secY);
            sections.put(secY, section);
        }

        if (section != null) {
            section.setBlock(x, y, z, block);

            //update heightmap
            x = Section.blockInSection(x);
            z = Section.blockInSection(z);
            heightmap[xz1D(x,z)] = Math.max(heightmap[xz1D(x,z)], y);
            
            //lightPopulated = false; //invalidate lightmap?
            dirty = true;
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void saved() {
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
