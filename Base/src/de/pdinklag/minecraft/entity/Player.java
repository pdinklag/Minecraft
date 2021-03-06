package de.pdinklag.minecraft.entity;

import de.pdinklag.minecraft.nbt.CompoundTag;
import de.pdinklag.minecraft.nbt.marshal.annotations.NBTProperty;
import de.pdinklag.minecraft.world.BlockPos;

import java.util.ArrayList;

/**
 * Represents a player entity.
 * <p/>
 * Mapped according to http://minecraft.gamepedia.com/Player.dat_format#NBT_structure.
 */
public class Player extends Mob {
    //TODO: Make enumeration
    @NBTProperty
    private int playerGameType;

    @NBTProperty(upperCase = true)
    private int score;

    @NBTProperty(upperCase = true)
    private int selectedItemSlot;

    //TODO: Map to Item class
    @NBTProperty(upperCase = true, optional = true)
    private CompoundTag selectedItem;

    @NBTProperty(value = {"SpawnX", "SpawnY", "SpawnZ"}, translator = BlockPos.Translator.class, optional = true)
    private BlockPos spawn;

    @NBTProperty(upperCase = true)
    private boolean spawnForced;

    @NBTProperty(upperCase = true)
    private boolean sleeping;

    @NBTProperty(upperCase = true)
    private short sleepTimer;

    @NBTProperty
    private int foodLevel;

    @NBTProperty
    private float foodExhaustionLevel;

    @NBTProperty
    private float foodSaturationLevel;

    @NBTProperty
    private int foodTickTimer;

    @NBTProperty(upperCase = true)
    private int xpLevel;

    @NBTProperty(value = "XpP", upperCase = true)
    private float xpProgress;

    @NBTProperty(upperCase = true)
    private int xpTotal;

    @NBTProperty(upperCase = true)
    private int xpSeed;

    //TODO: Map to Item class
    @NBTProperty(upperCase = true, listItemType = CompoundTag.class)
    private ArrayList<CompoundTag> inventory;

    //TODO: Map to Item class
    @NBTProperty(upperCase = true, listItemType = CompoundTag.class)
    private ArrayList<CompoundTag> enderItems;

    //TODO: Map to PlayerAbility class
    @NBTProperty
    private CompoundTag abilities;

    public int getPlayerGameType() {
        return playerGameType;
    }

    public void setPlayerGameType(int playerGameType) {
        this.playerGameType = playerGameType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSelectedItemSlot() {
        return selectedItemSlot;
    }

    public void setSelectedItemSlot(int selectedItemSlot) {
        this.selectedItemSlot = selectedItemSlot;
    }

    public CompoundTag getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(CompoundTag selectedItem) {
        this.selectedItem = selectedItem;
    }

    public BlockPos getSpawn() {
        return spawn;
    }

    public void setSpawn(BlockPos spawn) {
        this.spawn = spawn;
    }

    public boolean isSpawnForced() {
        return spawnForced;
    }

    public void setSpawnForced(boolean spawnForced) {
        this.spawnForced = spawnForced;
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public short getSleepTimer() {
        return sleepTimer;
    }

    public void setSleepTimer(short sleepTimer) {
        this.sleepTimer = sleepTimer;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public float getFoodExhaustionLevel() {
        return foodExhaustionLevel;
    }

    public void setFoodExhaustionLevel(float foodExhaustionLevel) {
        this.foodExhaustionLevel = foodExhaustionLevel;
    }

    public float getFoodSaturationLevel() {
        return foodSaturationLevel;
    }

    public void setFoodSaturationLevel(float foodSaturationLevel) {
        this.foodSaturationLevel = foodSaturationLevel;
    }

    public int getFoodTickTimer() {
        return foodTickTimer;
    }

    public void setFoodTickTimer(int foodTickTimer) {
        this.foodTickTimer = foodTickTimer;
    }

    public int getXpLevel() {
        return xpLevel;
    }

    public void setXpLevel(int xpLevel) {
        this.xpLevel = xpLevel;
    }

    public float getXpProgress() {
        return xpProgress;
    }

    public void setXpProgress(float xpProgress) {
        this.xpProgress = xpProgress;
    }

    public int getXpTotal() {
        return xpTotal;
    }

    public void setXpTotal(int xpTotal) {
        this.xpTotal = xpTotal;
    }

    public int getXpSeed() {
        return xpSeed;
    }

    public void setXpSeed(int xpSeed) {
        this.xpSeed = xpSeed;
    }

    public ArrayList<CompoundTag> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<CompoundTag> inventory) {
        this.inventory = inventory;
    }

    public ArrayList<CompoundTag> getEnderItems() {
        return enderItems;
    }

    public void setEnderItems(ArrayList<CompoundTag> enderItems) {
        this.enderItems = enderItems;
    }

    public CompoundTag getAbilities() {
        return abilities;
    }

    public void setAbilities(CompoundTag abilities) {
        this.abilities = abilities;
    }
}
