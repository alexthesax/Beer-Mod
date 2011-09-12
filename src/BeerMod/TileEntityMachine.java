package net.minecraft.src.BeerMod;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

/**
 * Generalized, abstract TileEntity class for IC machines with Inventories, not necessaryly GUI-ones
 * Based of the vanilla Furnace
 * @author alexthesax
 */
public abstract class TileEntityMachine extends TileEntityBlock implements IInventory
{
	/**
	 * @param slotcount Number of slots for the inventory
	 */
    public TileEntityMachine(int slotcount)
    {
        inventory = new ItemStack[slotcount];
    }
    
    public ItemStack[] inventory;

    /**
     * Minecraft Interface'd Method
     * Ignore it.
     */
    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }
    
    /**
     * Minecraft Interface'd Method
     * Ignore it.
     */
    @Override
    public ItemStack getStackInSlot(int i)
    {
        return inventory[i];
    }

    /**
     * Minecraft Interface'd Method
     * Ignore it.
     */
    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if(inventory[i] != null)
        {
            if(inventory[i].stackSize <= j)
            {
                ItemStack itemstack = inventory[i];
                inventory[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = inventory[i].splitStack(j);
            if(inventory[i].stackSize == 0)
            {
                inventory[i] = null;
            }
            return itemstack1;
        } else
        {
            return null;
        }
    }

    /**
     * Minecraft Interface'd Method
     * Ignore it.
     */
    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }
    
    /**
     * Don't mess with this, anything past 255 will cause major bugs, either way.
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
    
    /**
     * Minecraft Interface'd Method for rightclick-accessing GUI's of TEs.
     * Override it with "return false" if you have a unclickable TE-Block using this.
     */
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistance((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    /**
     * Define the name of the TE here.
     * I assume this is ned for NBT-saving purpouses, thus please pick unique names.
     */
    @Override
    public abstract String getInvName();

    /**
     * Check here to see how to add more stuff to the NBT Tags
     * Per se, only does save the inventory.
     * Do only override while keeping a super-call in!
     */
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        inventory = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if(byte0 >= 0 && byte0 < inventory.length)
            {
                inventory[byte0] = new ItemStack(nbttagcompound1);
            }
        }
    }

    /**
     * Check here to see how to add more stuff to the NBT Tags
     * Per se, only does load the inventory.
     * Do only override while keeping a super-call in!
     */
    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < inventory.length; i++)
        {
            if(inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("Items", nbttaglist);
    }
    
    /**
     * This method is responsible for all actual functions of a TE.
     * It is called once every ingame-tick, which is (on sufficient CPU) 20 times / second.
     * OVERRIDE!
     * 
     * And don't forget to add a
     * IC2BlockMachine.updateActivity() to keep the metadata up-to-date
     * If your block has activity-states, that is.
     */
    @Override
    public abstract void updateEntity();
}
