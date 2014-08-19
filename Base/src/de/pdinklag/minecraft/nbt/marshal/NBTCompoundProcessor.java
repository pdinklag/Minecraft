package de.pdinklag.minecraft.nbt.marshal;

import de.pdinklag.minecraft.nbt.CompoundTag;

/**
 * Interface for classes annotated with {@link de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType}, which do not have
 * a tight  property binding.
 * <p/>
 * These can freely process the input compound in order to be unmarshalled or marshalled.
 */
public interface NBTCompoundProcessor {
    /**
     * Unmarshals an NBT compound.
     *
     * @param nbt the NBT compound.
     */
    public void unmarshalCompound(CompoundTag nbt);

    /**
     * Marshals this object into an NBT compound.
     *
     * @return the NBT compound.
     */
    public CompoundTag marshalCompound();
}
