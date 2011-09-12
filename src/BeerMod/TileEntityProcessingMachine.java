package net.minecraft.src.BeerMod;

import java.util.HashMap;

import net.minecraft.src.Block;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_BeerMod;

/**
 * Base abstact TE for standard 3 slot processing machine
 * @author alexthesax
 */
public abstract class TileEntityProcessingMachine extends TileEntityMachine {

	public TileEntityProcessingMachine(int operationTime)
	{
		super(3);
		/*
		 * Inventory consists of the basic 3 slots:
		 * 0 = Input
		 * 1 = Fuel
		 * 2 = Output
		 */
		 operationLength = operationTime;
	}
	
	/**
	 * Remaining amount of fuel for operations, measured in ticks
	 */
    public int fuel = 0;
	/**
	 * Maximum amount of fuel which previously was in the machine. Used to display the fuel gauge in the GUI.
	 */
    public int maxFuel = 0;
	/**
	 * Progress of the Operation, measured in ticks
	 */
    public int progress = 0;
    /**
     * Length of a single operation, kept in this variable.
     * Will be useful for adjusting it later or for C&P of this file.
     */
    public int operationLength; // Note: 200 = Normal Furnace Operation
    
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        fuel = nbttagcompound.getShort("fuel");
        maxFuel = nbttagcompound.getShort("maxFuel");
        progress = nbttagcompound.getShort("progress");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("fuel", (short)fuel);
        nbttagcompound.setShort("maxFuel", (short)maxFuel);
        nbttagcompound.setShort("progress", (short)progress);
    }
    
    /**
     * Used by the GUI for displaying the progress gauge.
     * Scales the to-display length of the gauge according to current progress.
     * @param i length of the entire gauge graphic
     */
    public int gaugeProgressScaled(int i)
    {
        return (progress * i) / operationLength;
    }

    /**
     * Used by the GUI for displaying the fuel gauge.
     * Scales the to-display length of the gauge according to current fuel.
     * @param i length of the entire gauge graphic
     */
    public int gaugeFuelScaled(int i)
    {
    	if (maxFuel == 0)
    	{
    		maxFuel=fuel;
    		if (maxFuel == 0)
    		{
    			maxFuel = operationLength;
    		}
    	}
        return (fuel * i) / maxFuel;
    }

    /**
     * Converts itemstacks from slot 0 into new stuff in slot 2, while using slot 1 as fuel
     * More comments within the method code
     */
	@Override
	public void updateEntity()
	{
		boolean wasOperating = isBurning();
		boolean needsInvUpdate = false; // To reduce update Frequency of invs, only call onInventory changed if this was set to true
		if (fuel > 0)
		{
			fuel--; // Consume fuel
		}
		
		if (worldObj.multiplayerWorld)
		{ //Notch added the SMP-check here, let's do so as well.
			return;
		}
		
		if (fuel <= 0 && canOperate())
		{ // No fuel, but machine wants to conduct an operation
			fuel = maxFuel = getFuelValueFor(inventory[1]);
			if(fuel > 0)
            { // If the fuel-item actually provided fuel, decrement it
                if(inventory[1].getItem().hasContainerItem())
                { // If the object is a bucket-like container, clear the container
                	inventory[1] = new ItemStack(inventory[1].getItem().getContainerItem());
                }
                else
                { // Else just consume 1 piece
                	inventory[1].stackSize--;
                }
                if(inventory[1].stackSize <= 0)
                {
                	inventory[1] = null;
                }
                needsInvUpdate = true;
            }
		}
		
		if(wasOperating && canOperate())
        {
            progress++;
            if(progress >= operationLength)
            {
                progress = 0;
                operate();
                needsInvUpdate = true;
            }
        }
		else
        { // If the Iron Furnace is out of fuel or not capable of performing his operation, reset progress
            progress = 0;
        }
		
		if(wasOperating != isBurning())
        {// If the state of the machine changed while running through this method
			active = isBurning(); //Update the Block's Texture state
			worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
			needsInvUpdate = true;
        }
		
		if(needsInvUpdate)
        { // If the Inventory changed, call the referring super-method. Dunno what it actually does, but i assume it's important
            onInventoryChanged();
        }
	}
	
	public void operate()
    {
        if(!canOperate())
        { // Actually an unnecessary catch... but i assume Notch had his reasons...
            return;
        }
        ItemStack itemstack = getResultFor(inventory[0]);
        
        if(inventory[2] == null)
        { // If output is empty, put the result in
            inventory[2] = itemstack.copy();
        }
        else
        { // Otherwise there's already the result in, thus increment it
            inventory[2].stackSize += itemstack.stackSize;
        }
        
        //Decrement the inout item
        if(inventory[0].getItem().hasContainerItem())
        {// If the object is a bucket-like container, clear the container
            inventory[0] = new ItemStack(inventory[0].getItem().getContainerItem());
        }
        else
        { // Else just consume 1 piece
            inventory[0].stackSize--;
        }
        if(inventory[0].stackSize <= 0)
        {
            inventory[0] = null;
        }
    }
	
    public boolean isBurning()
    {
        return fuel > 0;
    }
    
    /**
     * Checks whether the machine can operate, input/output wise.
     * Fuel is not necessary for this check.
     */
    public boolean canOperate()
    {
    	if(inventory[0] == null)
        { // Obviously, no input = no operation
            return false;
        }
    	
    	ItemStack itemstack = getResultFor(inventory[0]);
    	
        if(itemstack == null)
        { // No result = No Operation
            return false;
        }
        if(inventory[2] == null)
        { // If output slots is free for anything
            return true;
        }
        if(!inventory[2].isItemEqual(itemstack)) // Checks for ID AND damage value
        { // If output slot is occupied by something else
            return false;
        }
        return inventory[2].stackSize+itemstack.stackSize <= inventory[2].getMaxStackSize();
        // Return true if the sum of new and old output does not exceed the max stacksize
    }
    
    /**
     * Copied from TileEntityFurnace
     * Convert fuel-itemstack into the amount of fuel it can produce
     * Does NOT decrement the actual inventoryslot!
     */
    public static int getFuelValueFor(ItemStack itemstack)
    {
        if(itemstack == null)
        {
            return 0;
        }
        int i = itemstack.getItem().shiftedIndex;
        if(i < 256 && Block.blocksList[i].blockMaterial == Material.wood)
        {
            return 300;
        }
        if(i == Item.stick.shiftedIndex)
        {
            return 100;
        }
        if(i == Item.coal.shiftedIndex)
        {
            return 1600;
        }
        if(i == Item.bucketLava.shiftedIndex)
        {
            return 20000;
        }
        if(i == Block.sapling.blockID)
        {
            return 100;
        }
		if(i == Block.cactus.blockID)
        {
            return 5000;	// CHANGE, TESTING PURPOSES ONLY!!!!!!!!!
        }
        else
        {
            return ModLoader.AddAllFuel(i);
        }
    }
    
	/**
	 * Accesses it's own recipe-list
	 */
	public ItemStack getResultFor(ItemStack itemstack)
	{
		return recipes.get(itemstack.itemID);
	}
    
	/**
	 * Use this method to add new Compression recipes from outside.
	 * Must be used after the referring blocks/items were initialized
	 * @param input ID of the Input-Item
	 * @param output Itemstack of the Output-Item
	 */
	public static void addRecipe (int input, ItemStack output)
	{
		recipes.put(input, output);
	}
	
	/**
	 * Static HashMap containing the Compression recipes
	 * Key : Integer
	 * Value : ItemStack
	 */
	public static HashMap<Integer, ItemStack> recipes;
	
	@Override
	public abstract String getInvName();
}
