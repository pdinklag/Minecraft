package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTProperty;

/**
 * Base class for pigs.
 */
@NBTCompoundType
public class Bat extends Mob {
    @NBTProperty(upperCase = true)
    private boolean batFlags;

    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Bat() {
    	super("Bat");
    	batFlags = false;
    }

    /**
     * copy constructor
     */
    public Bat(Bat src) {
    	super(src);
    	id = "Bat";
    	batFlags = src.batFlags;
    }
    
    public boolean getBatFlags() {
        return batFlags;
    }

    public void setBatFlags(boolean batFlags) {
        this.batFlags = batFlags;
    }
}
