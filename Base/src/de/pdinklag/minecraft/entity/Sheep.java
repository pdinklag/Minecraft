package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTProperty;

/**
 * Class for sheeps.
 */
@NBTCompoundType
public class Sheep extends BreedableMob {

	@NBTProperty(upperCase = true)
    private boolean sheared;

	@NBTProperty(upperCase = true)
    private byte color;

    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Sheep() {
    	super("Sheep");
    	sheared = false;
    	color = 7;//gray
    }

    /**
     * copy constructor
     */
    public Sheep(Sheep src) {
    	super(src);
    	id = "Sheep";
    	src.sheared = sheared;
    	src.color = color;
    }
    
    public boolean getSheared() {
        return sheared;
    }

    public void setSheared(boolean sheared) {
        this.sheared = sheared;
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }


}
