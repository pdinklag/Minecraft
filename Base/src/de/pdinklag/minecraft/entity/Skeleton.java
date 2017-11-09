package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTProperty;

/**
 * Base class for skeleton entities.
 */
@NBTCompoundType
public class Skeleton extends Mob {
	static final byte TYPE_NORMAL = 0;
	static final byte TYPE_WITHER = 1;
	
	
    @NBTProperty(upperCase = true)
    private byte skeletonType;


    /**
     * Constructs a new blank item (to use when loading from file)
     */
    public Skeleton() {
    	super("Skeleton");
    	skeletonType = TYPE_NORMAL;
    }

    /**
     * copy constructor
     */
    public Skeleton(Skeleton src) {
    	super(src);
    	id = "Skeleton";
    	skeletonType = src.skeletonType;
    }
    
    public byte getSkeletonType() {
        return skeletonType;
    }

    public void setSkeletonType(byte skeletonType) {
        this.skeletonType = skeletonType;
    }
}
