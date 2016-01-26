package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;

/**
 * Class for villagers.
 */
@NBTCompoundType
public class Villager extends BreedableMob {

    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Villager() {
    	super("Villager");
    }

    /**
     * copy constructor
     */
    public Villager(Villager src) {
    	super(src);
    	id = "Villager";
    }
}
