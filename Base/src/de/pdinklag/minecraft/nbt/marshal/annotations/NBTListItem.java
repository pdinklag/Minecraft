package de.pdinklag.minecraft.nbt.marshal.annotations;

import de.pdinklag.minecraft.nbt.marshal.NBTDefaultTranslator;
import de.pdinklag.minecraft.nbt.marshal.NBTTranslator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for items within an NBT list.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NBTListItem {
    /**
     * @return the list item index.
     */
    public int value();

    /**
     * @return the translator class to use.
     */
    public Class<? extends NBTTranslator> translator() default NBTDefaultTranslator.class;

    /**
     * @return for properties that derive from {@link java.util.List}, this must define the item class.
     */
    public Class<?> listItemType() default Object.class;
}
