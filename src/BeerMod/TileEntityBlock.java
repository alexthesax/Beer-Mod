package net.minecraft.src.BeerMod;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

/**
 * Basic TE containing the various Block-infos to compress 16 blocks into a single ID.
 * NOTE: This class is NOT abstract and CAN be created, by blocks which wouldn't have A TE, except for Compression purpouses.
 * @author alexthesax
 */
public class TileEntityBlock extends TileEntity
{
	/**
	 * Empty
	 */
    public TileEntityBlock()
    {
    }
    
    public boolean active = false;
    public int facing = 0;

    /**
     * Loads the corressponding TileEntity, based on Metadata.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        active = nbttagcompound.getBoolean("active");
        facing = nbttagcompound.getShort("facing");
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
        nbttagcompound.setBoolean("active", active);
        nbttagcompound.setShort("facing", (short)facing);
    }
    
    /**
     * This method is responsible for all actual functions of a TE.
     * It is called once every ingame-tick, which is (on sufficient CPU) 20 times / second.
     * Override if you need more then a buffer-TE.
     */
    @Override
    public void updateEntity()
    {
    	
    }
}
