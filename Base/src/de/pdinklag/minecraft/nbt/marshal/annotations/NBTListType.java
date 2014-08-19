package de.pdinklag.minecraft.nbt.marshal.annotations;

import java.lang.annotation.*;

/**
 * Annotation for classes that can be read from and written to NBT lists.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NBTListType {
}
