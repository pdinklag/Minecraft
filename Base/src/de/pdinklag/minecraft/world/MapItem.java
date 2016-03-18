package de.pdinklag.minecraft.world;

import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.marshal.NBTCompoundProcessor;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;

/**
 * Represents a chunk section, consisting of 16x16x16 blocks.
 */
@NBTCompoundType
public class MapItem implements NBTCompoundProcessor {
	
	public final static int MAP_ITEM_SIZE = 128;

	//definition of zoom levels and the applied pixel ratio
	public final static byte MAP_SCALE_0 = 0;
	public final static byte MAP_SCALE_1 = 1;
	public final static byte MAP_SCALE_2 = 2;
	public final static byte MAP_SCALE_3 = 3;
	public final static byte MAP_SCALE_4 = 4;
	
	public static int getPixelRatio(byte scale) {
		if (scale < MAP_SCALE_0 || scale > MAP_SCALE_4) {
			throw new WorldException("trying to get map item's pixel ratio with a wrong scale");
		}
		return (int) Math.pow(2, scale);
	}

	public final static byte MAP_DIMENSION_OVERWORLD = 0;
	public final static byte MAP_DIMENSION_NETHER = -1;
	public final static byte MAP_DIMENSION_END = 1;

    private byte scale;
    private byte dimension;
    private short width = MAP_ITEM_SIZE;
    private short height = MAP_ITEM_SIZE;
    private int xCenter;
    private int zCenter;
    private final byte[] colors = new byte[MAP_ITEM_SIZE * MAP_ITEM_SIZE];
    
    public transient boolean dirty = true;

    /**
     * Constructs a new map item with default values.
     */
    public MapItem() {
    	scale = MAP_SCALE_3;
    	dimension = MAP_DIMENSION_OVERWORLD;
    	xCenter = 448;
    	zCenter = 448;
    }

    /**
     * Constructs a new map item with given values.
     */
    public MapItem(byte scale, byte dimension, int xCenter, int zCenter) {
		if (scale < MAP_SCALE_0 || scale > MAP_SCALE_4) {
			throw new WorldException("trying to create a map Item with a wrong scale");
		}
    	this.scale = scale;
		if (dimension < MAP_DIMENSION_NETHER || scale > MAP_DIMENSION_END) {
			throw new WorldException("trying to create a map Item with a wrong dimension");
		}
		this.dimension = dimension;
		int mapRealSize = MAP_ITEM_SIZE * getPixelRatio(scale);
		if ( (xCenter / mapRealSize - 64 + mapRealSize / 2) != xCenter ) {
			throw new WorldException("trying to create a map Item with a center not fitting the map alignment (x axis)");
		}
    	this.xCenter = xCenter;
		if ( (zCenter / mapRealSize - 64 + mapRealSize / 2) != zCenter ) {
			throw new WorldException("trying to create a map Item with a center not fitting the map alignment (z axis)");
		}
		this.zCenter = zCenter;
    }

    @Override
    public void unmarshalCompound(CompoundTag nbt) {
        CompoundTag data = nbt.getCompound("data");

        scale = data.getByte("scale");
		if (scale < MAP_SCALE_0 || scale > MAP_SCALE_4) {
			throw new WorldException("map Item has a wrong scale");
		}
        dimension = data.getByte("dimension");
        width = data.getShort("width");
        height = data.getShort("height");
        if (width != MAP_ITEM_SIZE && height != MAP_ITEM_SIZE) {
        	throw new WorldException("map Item has wrong dimensions");
        }
        xCenter = data.getInt("xCenter");
        zCenter = data.getInt("zCenter");
        byte[] readColors = data.getByteArray("colors");
        if (readColors.length != MAP_ITEM_SIZE * MAP_ITEM_SIZE) {
        	throw new WorldException("map Item's color data has a wrong size");
        }
        System.arraycopy(readColors, 0, colors, 0, MAP_ITEM_SIZE * MAP_ITEM_SIZE);
        
        dirty = false;
    }

    @Override
    public CompoundTag marshalCompound() {
        CompoundTag data = new CompoundTag();
        data.put("scale", scale);
        data.put("dimension", dimension);
        data.put("height", height);
        data.put("width", width);
        data.put("xCenter", xCenter);
        data.put("zCenter", zCenter);
        data.put("colors", colors);

        CompoundTag root = new CompoundTag();
        root.put("data", data);

        return root;
    }
    
	public boolean isDirty() {
        return dirty;
    }

	public void saved() {
        this.dirty = false;
    }
	
	public void setColor(byte x, byte z, byte color) {
        if (x < 0 || x >= MAP_ITEM_SIZE || z < 0 || z >= MAP_ITEM_SIZE) {
        	throw new WorldException("map Item coordinate out of bounds");
        }
        colors[z * MAP_ITEM_SIZE + x] = color;
        this.dirty = true;
	}

	public byte[] getColors() {
		return colors;
	}
}
