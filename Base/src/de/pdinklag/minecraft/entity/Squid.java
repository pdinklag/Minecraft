package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;

/**
 * Class for villagers.
 */
@NBTCompoundType
public class Squid extends Mob {
    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Squid() {
    	super("Squid");
    }

    /**
     * copy constructor
     */
    public Squid(Squid src) {
    	super(src);
    	id = "Squid";
    }

}
