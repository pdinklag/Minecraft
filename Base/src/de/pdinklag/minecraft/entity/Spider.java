package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;

/**
 * Class for spider entities.
 */
@NBTCompoundType
public class Spider extends Mob {
    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Spider() {
    	super("Spider");
    }

    /**
     * copy constructor
     */
    public Spider(Spider src) {
    	super(src);
    	id = "Spider";
    }

}
