package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTProperty;

/**
 * Base class for pigs.
 */
@NBTCompoundType
public class MinecartTNT extends Minecart {
    @NBTProperty(upperCase = true)
    private int tNTFuse;

    /**
     * Constructs a new blank entity (to use when loading from file)
     */
    public MinecartTNT() {
    	super("MinecartTNT");
    	tNTFuse = -1;
    }

    /**
     * copy constructor
     */
    public MinecartTNT(MinecartTNT src) {
    	super(src);
    	tNTFuse = src.tNTFuse;
    }
    
    public int getTNTFuse() {
        return tNTFuse;
    }

    public void setTNTFuse(int tNTFuse) {
        this.tNTFuse = tNTFuse;
    }
}
