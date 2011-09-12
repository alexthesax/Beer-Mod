package net.minecraft.src.BeerMod;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiChest;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

/**
 * This is the new base-class for all IC-Machines.
 * It contains all necessary methods and basic methods ned for a TE-Block.
 * Uses the IC2Block system for compressing 16:1
 * 
 * Blocks in here:
 * 1 = Mill
 * 
 * @author alexthesax
 */
public class BlockBeerMachines extends BlockMultiID
{

	/**
	 * Merely requires the Block ID for initializing the block object.
	 */
    public BlockBeerMachines(int i)
    {
        super(i, Material.iron);
        setHardness(2.0F);
        setStepSound(soundMetalFootstep);
    }
  
    
    @Override
    public int[][] fillSpriteSheet()
    {
    	return new int[][] {{1,1,1,1,1,1,1,1}};
    }
    
    /**
     * Determine dropped ID, based on metadata.
     * @param meta Metadata
     */
    @Override
    public int idDropped(int meta, Random random)
    {
    	switch (meta)
    	{
    	default: return blockID; 
    	}
    }
    
    /**
     * Determine meta-value of dropped item, based on metadata.
     * Needs to be synced up with whatever is specified in idDropped.
     * @param meta Metadata
     */
    @Override
    protected int damageDropped(int meta)
    {
    	switch (meta)
    	{
		default: return 1;
    	}
    }
    
    @Override
    public GuiScreen getGui(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
    	switch(world.getBlockMetadata(i, j, k))
    	{
    	case 0: return new GuiMill(entityplayer.inventory, (TileEntityMill)world.getBlockTileEntity(i, j, k));
    	default: return null;
    	}
    }
    
    /**
     * Generates TileEntity from metadata.
     */
    @Override
    public TileEntityBlock getBlockEntity(int meta)
    {
    	switch(meta)
    	{
    	default: return new TileEntityMill();
    	}
    }
    
    /**
     * Randomized bonus animations
     */
    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		return;
    }
}
