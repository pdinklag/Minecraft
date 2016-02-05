package de.pdinklag.minecraft.world;

import de.pdinklag.minecraft.entity.Entity;
import de.pdinklag.minecraft.math.Vec3d;
import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.ListTag;
import de.pdinklag.minecraft.nbt.NBT;
import de.pdinklag.minecraft.nbt.NBT.Type;
import de.pdinklag.minecraft.nbt.marshal.NBTCompoundProcessor;
import de.pdinklag.minecraft.nbt.marshal.NBTMarshal;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a chunk, consisting of 16 Y sections.
 */
@NBTCompoundType
public class Chunk implements NBTCompoundProcessor {
    private static final Logger LOGGER = Logger.getLogger("Chunk");
    
    public static final int BLOCKS = 16; //in blocks
    static final int BLOCKS_SQ = BLOCKS * BLOCKS;
    
    static final byte BIOME_DEFAULT = Biome.PLAINS;
    static final byte MAX_LIGHT = 0x0f;
    
    private static byte yToSection(int y) {
        return (byte)(y / Section.BLOCKS);
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
    private final TreeMap<Byte, Section> sections = new TreeMap<>();
    private final ArrayList<Entity> entities = new ArrayList<>();

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
	        
	        for (NBT<?> entityNbt : nbt.getList("Entities")) {
	            Entity entity = NBTMarshal.unmarshal(Entity.class, entityNbt);
            	try {
	            	entity = (Entity) NBTMarshal.unmarshal(Class.forName("de.pdinklag.minecraft.entity."+entity.getId()), entityNbt);
            	} catch (ClassNotFoundException e) {
	            	//not implemented yet
                	LOGGER.log(Level.INFO,"failed to load entity of type " + entity.getId() + " because it is not implemented yet");

	            	//TODO: some mobs, projectiles, xpOrbs, vehicles, dynamicTiles, Other
	            	entity = null;
            	}
	            if (entity != null) {
	            	entities.add(entity);
	            }
	        }
	        
	        //TODO: read block entities

	        dirty = false;
    	}
    	catch (Exception e) {
	        throw new WorldException("malformed chunk", e);
    	}
    }

    @Override
    public CompoundTag marshalCompound() {
    	//compute the lighting if needed before marshalling the data
    	computeLighting();    	
    	
        CompoundTag root = new CompoundTag();
        root.put("xPos", x);
        root.put("zPos", z);
        //we consider the marshalling as the trigger for updating the lastUpdate field
    	Date date= new Date();
        lastUpdate = date.getTime();
        root.put("LastUpdate", lastUpdate);
        root.put("LightPopulated", lightPopulated);
        root.put("TerrainPopulated", terrainPopulated);
        root.put("V", (byte)1);
        root.put("InhabitedTime", inhabitedTime);
       	root.put("Biomes", biomes);
        root.put("HeightMap", heightmap);
        //create sections as NBT
        ListTag sectionListNbt = new ListTag();
        for(Section section: sections.values()) {
        	if(!section.isEmpty()) {
        		sectionListNbt.add(NBTMarshal.marshal(section));
        	}
        }
        root.put("Sections", sectionListNbt);
        
        ListTag nbtList;
        
        nbtList = new ListTag();
        if (entities.size() == 0) {
        	nbtList.setType(Type.BYTE);//because list is empty
        } else {
	        for(Entity entity: entities) {
	        	nbtList.add(NBTMarshal.marshal(entity));
	        }
        }
        root.put("Entities", nbtList);
        
        //TODO: save block entities
        nbtList = new ListTag();
        nbtList.setType(Type.BYTE);//because list is empty
        root.put("TileEntities", nbtList);

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
        final byte secY = yToSection(y);

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
            
            lightPopulated = false; //invalidate lightmap
            dirty = true;
        }
    }

    public void addEntity(double x, double y, double z, Entity entity) {
    	entity.setPos(new Vec3d(x,y,z));
    	entities.add(entity);
    	
        dirty = true;
    }
    
    public void computeLighting () {
    	//only compute if it is out of date
    	if(lightPopulated) {
    		return;
    	}
    	
    	//simplistic method : set skylight to full between top and surface block
    	for (int xInChunk = 0; xInChunk < BLOCKS; xInChunk++) {
    		for (int zInChunk = 0; zInChunk < BLOCKS; zInChunk++) {
    			int curHeight = heightmap[xz1D(xInChunk,zInChunk)];
    			for (int y = 255 ; y >= 0; y--) {
    				byte sectionY = yToSection(y);
    				Section section = sections.get(sectionY);
    				if (section == null) {
    					//go straight to next section
    					y -= Section.BLOCKS - 1;
    				} else {
    					if (y >= curHeight) {
    						section.setSkyLight(xInChunk, y, zInChunk, MAX_LIGHT);
    					}
    					else {
    						//TODO: check if there is a transparent block
    						//no need to go to lower blocks
    						break;
    					}
    				}
    			}
        	}	
    	}
    	
    	lightPopulated = true;
    }

    @SuppressWarnings("unchecked")
	public ArrayList<Entity> getEntities() {
    	return (ArrayList<Entity>) entities.clone();
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
