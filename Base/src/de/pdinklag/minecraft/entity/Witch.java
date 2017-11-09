package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;

/**
 * Class for witch entities.
 */
@NBTCompoundType
public class Witch extends Mob {

    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Witch() {
    	super("Witch");
    }

    /**
     * copy constructor
     */
    public Witch(Witch src) {
    	super(src);
    	id = "Witch";
    }
}
