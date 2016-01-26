package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.ByteTag;
import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.NBT;
import de.pdinklag.minecraft.nbt.ShortTag;
import de.pdinklag.minecraft.nbt.StringTag;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for items.
 */
@NBTCompoundType
public class Item extends Entity {
	
    @NBTProperty(upperCase = true)
    private short age;

    @NBTProperty(upperCase = true)
    private short health;

    @NBTProperty(upperCase = true)
    private short pickupDelay;

    @NBTProperty(upperCase = true, optional = true)
    private String owner;

    @NBTProperty(upperCase = true, optional = true)
    private String thrower;

    @NBTProperty(upperCase = true, listItemType = CompoundTag.class)
    private ArrayList<CompoundTag> item;

    private String item_id;
    private byte item_count;
    private short item_damage;
    
    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Item() {
    	super("Item");

        age = -32768;
        health = 5;
        pickupDelay = 0;
        owner = "";
        thrower = "";
        item_count = 1;
        item_damage = 0;
        item_id = "";
    }

    /**
     * Constructs a new item with specified values
     */
    public Item(String itemId, short damage, byte count) {
    	this();
    	this.item_id = itemId;
    	this.item_damage = damage;
    	this.item_count = count;
    }
    public Item(String itemId, byte count) {
    	this(itemId, (short) 0, count);
    }
    public Item(String itemId) {
    	this(itemId, (byte) 1);
    }
    
    /**
     * copy constructor
     */
    public Item(Item src) {
    	super(src);

        age = src.age;
        health = src.health;
        pickupDelay = src.pickupDelay;
        owner = src.owner;
        thrower = src.thrower;
        item_count = src.item_count;
        item_damage = src.item_damage;
        item_id = src.item_id;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public short getHealth() {
        return health;
    }

    public void setHealth(short health) {
        this.health = health;
    }

    public short getPickupDelay() {
        return pickupDelay;
    }

    public void setPickupDelay(short pickupDelay) {
        this.pickupDelay = pickupDelay;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getThrower() {
        return thrower;
    }

    public void setThrower(String thrower) {
        this.thrower = thrower;
    }

    public HashMap<String,NBT> getItem() {
    	HashMap<String,NBT> map = new HashMap<String,NBT>();
    	map.put("id", new StringTag(item_id));
    	map.put("Count", new ByteTag(item_count));
    	map.put("Damage", new ShortTag(item_damage));
        return map;
    }

    public void setItem(HashMap<String,NBT> item) {
        item_id = (String) item.get("id").getValue();
        item_count = (byte) item.get("Count").getValue();
        item_damage = (short) item.get("Damage").getValue();
    }


}
