package chestlib.api.properties;

import net.minecraft.nbt.NBTTagCompound;

import chestlib.api.storage.INBTSaveLoad;

public interface IPropertyContainer extends INBTSaveLoad<NBTTagCompound> {

    <T> T getProperty(IPropertyType<T> prop);

    <T> T getProperty(IPropertyType<T> prop, T def);

    boolean hasProperty(IPropertyType<?> prop);

    void removeProperty(IPropertyType<?> prop);

    <T> void setProperty(IPropertyType<T> prop, T value);

    void removeAllProps();
}
