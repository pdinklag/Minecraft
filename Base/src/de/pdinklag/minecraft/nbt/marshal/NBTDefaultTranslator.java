package de.pdinklag.minecraft.nbt.marshal;

import de.pdinklag.minecraft.nbt.*;

/**
 * The default NBT translator.
 */
public class NBTDefaultTranslator implements NBTTranslator<Object> {
    @Override
    public Object translateFromNBT(NBT[] nbt) {
        if (nbt.length > 0) {
            return nbt[0].getValue();
        } else {
            return null;
        }
    }

    @Override
    public NBT[] translateToNBT(Object x) {
        final NBT nbt;
        if (x instanceof Byte) {
            nbt = new ByteTag((byte) x);
        } else if (x instanceof Boolean) {
            nbt = new ByteTag((boolean) x ? (byte) 1 : (byte) 0);
        } else if (x instanceof Short) {
            nbt = new ShortTag((short) x);
        } else if (x instanceof Integer) {
            nbt = new IntTag((int) x);
        } else if (x instanceof Long) {
            nbt = new LongTag((long) x);
        } else if (x instanceof String) {
            nbt = new StringTag((String) x);
        } else if (x instanceof byte[]) {
            nbt = new ByteArrayTag((byte[]) x);
        } else if (x instanceof int[]) {
            nbt = new IntArrayTag((int[]) x);
        } else {
            return new NBT[0];
        }

        return new NBT[]{nbt};
    }
}
