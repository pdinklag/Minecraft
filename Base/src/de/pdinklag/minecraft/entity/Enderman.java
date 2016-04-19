package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTProperty;

/**
 * Class for enderman entities.
 */
@NBTCompoundType
public class Enderman extends Mob {
    @NBTProperty()
    private short carried;

    @NBTProperty()
    private short carriedData;

    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Enderman() {
    	super("Enderman");
    	carried = 0;
    	carriedData = 0;
    }

    /**
     * copy constructor
     */
    public Enderman(Enderman src) {
    	super(src);
    	id = "Enderman";
    	carried = src.carried;
    	carriedData = src.carriedData;
    }
    
    public short getCarried() {
        return carried;
    }

    public void setCarried(short carried) {
        this.carried = carried;
    }

    public short getCarriedData() {
        return carriedData;
    }

    public void setCarriedData(short carriedData) {
        this.carriedData = carriedData;
    }

}
