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

    @NBTProperty(upperCase = true)
    private HashMap<String,NBT> item;
    
    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Item() {
    	super("Item");

        age = -32768;
        health = 5;
        pickupDelay = 0;
    }

    /**
     * Constructs a new item with specified values
     */
    public Item(String itemId, short damage, byte count) {
    	this();
    	item = new HashMap<String,NBT>();
    	item.put("id", new StringTag(itemId));
    	item.put("Count", new ByteTag(count));
    	item.put("Damage", new ShortTag(damage));
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
    	item = new HashMap<String,NBT>(src.item);
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
        return item;
    }

    public void setItem(HashMap<String,NBT> item) {
    	this.item = item;
    }


}
