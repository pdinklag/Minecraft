package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.LongTag;
import de.pdinklag.minecraft.nbt.NBT;
import de.pdinklag.minecraft.nbt.marshal.NBTTranslator;

import java.util.UUID;

/**
 * NBT Translator for UUIDs.
 */
public class UUIDTranslator implements NBTTranslator<UUID> {
    @Override
    public UUID translateFromNBT(NBT[] nbt) {
        return new UUID(((LongTag) nbt[0]).getValue(), ((LongTag) nbt[1]).getValue());
    }

    @Override
    public NBT[] translateToNBT(UUID x) {
        return new NBT[]{
                new LongTag(x.getLeastSignificantBits()),
                new LongTag(x.getMostSignificantBits()),
        };
    }
}
