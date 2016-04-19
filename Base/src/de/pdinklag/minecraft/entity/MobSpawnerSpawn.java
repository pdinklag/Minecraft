package de.pdinklag.minecraft.entity;

import java.util.HashMap;

import de.pdinklag.minecraft.nbt.NBT;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTCompoundType;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTProperty;

@NBTCompoundType
public class MobSpawnerSpawn {
    @NBTProperty(upperCase = true)
    private HashMap<String, NBT> entity;

    @NBTProperty(upperCase = true)
    private int weight;
    
    public MobSpawnerSpawn() {
    }

    public MobSpawnerSpawn(HashMap<String, NBT> entity) {
    	this.entity = entity;
    	weight = 1;
    }

    public MobSpawnerSpawn(MobSpawnerSpawn src) {
    	entity = src.entity;
    	weight = src.weight;
    }
    
    public HashMap<String, NBT> getEntity() {
        return entity;
    }
    public void setEntity(HashMap<String, NBT> entity) {
        this.entity = entity;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
}