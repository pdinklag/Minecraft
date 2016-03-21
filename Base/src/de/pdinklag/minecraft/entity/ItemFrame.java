package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.ByteTag;
import de.pdinklag.minecraft.nbt.NBT;
import de.pdinklag.minecraft.nbt.ShortTag;
import de.pdinklag.minecraft.nbt.StringTag;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTProperty;

import java.util.HashMap;

/**
 * Class for items.
 */
@NBTCompoundType
public class ItemFrame extends Entity {
	
	public static final byte FACING_SOUTH = 0;
	public static final byte FACING_WEST = 1;
	public static final byte FACING_NORTH = 2;
	public static final byte FACING_EAST = 3;
	
    @NBTProperty(upperCase = true)
    private int tileX;

    @NBTProperty(upperCase = true)
    private int tileY;

    @NBTProperty(upperCase = true)
    private int tileZ;

    @NBTProperty(upperCase = true)
    private byte facing;

    @NBTProperty(upperCase = true, optional = true)
    private HashMap<String,NBT> item;//actually never used to store data, just to indicate the NBT compound
    private String item_id;
    private byte item_count;
    private short item_damage;
    
    @NBTProperty(upperCase = true)
    private float itemDropChance;

    @NBTProperty(upperCase = true)
    private byte itemRotation;

    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public ItemFrame() {
    	super("ItemFrame");
    	
    	itemDropChance = 0;
    }

    /**
     * Constructs a new item with specified values
     */
    public ItemFrame(int x, int y, int z, byte facing, byte itemRotation, String itemId, short damage) {
    	this();
    	this.tileX = x;
    	this.tileY = y;
    	this.tileZ = z;
    	this.facing = facing;
    	this.itemRotation = itemRotation;
    	this.item = new HashMap<String,NBT>();//so it will be saved
    	this.item_id = itemId;
    	this.item_damage = damage;
    	this.item_count = 1;
    }
    
    public ItemFrame(int x, int y, int z, byte facing) {
    	this();
    	this.tileX = x;
    	this.tileY = y;
    	this.tileZ = z;
    	this.facing = facing;
    	this.itemRotation = 0;
    }
    
    /**
     * copy constructor
     */
    public ItemFrame(ItemFrame src) {
    	super(src);

    	this.tileX = src.tileX;
    	this.tileY = src.tileY;
    	this.tileZ = src.tileZ;
    	this.facing = src.facing;
    	this.itemRotation = src.itemRotation;
    	this.item = src.item;
        item_count = src.item_count;
        item_damage = src.item_damage;
        item_id = src.item_id;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getTileZ() {
        return tileZ;
    }

    public void setTileZ(int tileZ) {
        this.tileZ = tileZ;
    }

    public byte getFacing() {
        return facing;
    }

    public void setFacing(byte facing) {
        this.facing = facing;
    }

    public float getItemDropChance() {
        return itemDropChance;
    }

    public void setItemDropChance(float itemDropChance) {
        this.itemDropChance = itemDropChance;
    }

    public byte getItemRotation() {
        return itemRotation;
    }

    public void setOItemRotation(byte itemRotation) {
        this.itemRotation = itemRotation;
    }

    public HashMap<String,NBT> getItem() {
    	if (item == null) {
    		return null;
    	}
    	HashMap<String,NBT> map = new HashMap<String,NBT>();
    	map.put("id", new StringTag(item_id));
    	map.put("Count", new ByteTag(item_count));
    	map.put("Damage", new ShortTag(item_damage));
        return map;
    }

    public void setItem(HashMap<String,NBT> item) {
    	if (item == null) {
    		this.item = new HashMap<String,NBT>();
    	}
        item_id = (String) item.get("id").getValue();
        item_count = (byte) item.get("Count").getValue();
        item_damage = (short) item.get("Damage").getValue();
    }


}
