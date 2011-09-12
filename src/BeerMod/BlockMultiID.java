package net.minecraft.src.BeerMod;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.ITextureProvider;


/**
 * The new multi-functional SUPER-Block class for IC.
 * Coded to containt 16 actual TE-Blocks by saving the tape of block into metadata.
 * Is abstract, override ned methods accordingly.
 * @author alexthesax
 */
public abstract class BlockMultiID extends BlockContainer implements ITextureProvider
{

	/**
	 * Needs BlockID and Material.
	 * Will call the abstract fillSpriteSheet() method.
	 */
    protected BlockMultiID(int i, Material mat)
    {
        super(i, mat);
        sprites = fillSpriteSheet();
        blockIndexInTexture = sprites[0][0];
    }
    
    /**
     * MCForge's texture-override.
	 * All blocks draw their sprites from the same 256er sheet, thus the generalization here.
     */
    @Override
    public String getTextureFile()
    {
    	return "/BeerModSprites/block.png";
    }
    
    /**
     * Undefined int[][], used to store the sprite icons.
     * First number defined the block type, second the sides.
     */
    public int[][] sprites;
    
    /**
     * Override this method to define the filling for the sprites-int[] (which will be created from this method) 
     * 
     * Per se, following sprites will be used
     *  sprites[*][0] = Down 
     *  sprites[*][1] = Up
     *  sprites[*][2] = Side
     *  sprites[*][3] = Front
     *  sprites[*][4] = Down Active
     *  sprites[*][5] = Up Active
     *  sprites[*][6] = Side Active
     *  sprites[*][7] = Front Active
     */
    public abstract int[][] fillSpriteSheet();

    /**
     *  Texture-method.
     *  side is the side that is meant to be rendered.
     *  0 = Down
     *  1 = Up
     *  2,3,4,5 = Sides
     *  meta = Front
     *  !meta = everything else
     */
    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side)
    {
    	int meta = iblockaccess.getBlockMetadata(i, j, k);
    	int active = 0; // Used to easyly increment the sprite-reading to the active states.
    	if (isActive(iblockaccess,i,j,k))
    	{
    		active = 4;
    	}
    	
        if(side < 2)
        {
            return sprites[meta][side+active];
        }
		if(side != getFacing(iblockaccess,i,j,k))
		{
			return sprites[meta][2+active];
		}
		else
		{
			return sprites[meta][3+active];
		}
    }

    /**
     * Texture Method to render Blocks within the Inventorys.
     * side is the side that is meant to be rendered.
     *  0 = Down
     *  1 = Up
     *  3 = Front
     *  2,4,5 = Sides
     *  
     *  Per se, it will use the following
     *  sprites[*][0] = Down 
     *  sprites[*][1] = Up
     *  sprites[*][2] = Side
     *  sprites[*][3] = Front
     */
    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
    	if (meta >= sprites.length)
    	{
    		System.err.println(this+": invalid meta "+meta+" provided to getBlockTextureFromSideAndMetadata");
    		return 0;
    	}
        if(side < 2)
        {
            return sprites[meta][side];
        }
        if(side == 3)
        {
            return sprites[meta][3];
        }
        else
        {
            return sprites[meta][2];
        }
    }

    /**
     * Method called if this block is clicked by a player.
     * Will automatically detect wrenching attempts.
     * Otherwise it will call the abstract getGUI method.
     */
    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
    	GuiScreen gui = getGui(world,i,j,k,entityplayer);
    	if (gui == null)
    	{
    		return false;
    	}
    	
        if(world.multiplayerWorld)
        {
            return true; // Do nothing, the GUI will be sent by the server and opened via different means.
        }
        else
        {
            ModLoader.OpenGUI(entityplayer, gui);
            return true;
        }
    }

    /**
     * Abstract GUI-method.
     * Override to create a GUI from the given parameters for SSP uses.
     * Return null if this block doesn't have a GUI, the engine will be able to handle it.
     */
    public abstract GuiScreen getGui (World world, int i, int j, int k, EntityPlayer entityplayer);
    
    /**
     * Casually drops the inventory of any destroyed IInventory-TE
     */
    public void dropInventory (World world, int i, int j, int k)
    {
    	TileEntity te = world.getBlockTileEntity(i, j, k);
    	if (!(te instanceof IInventory))
    	{
    		return;
    	}
    	IInventory inv = (IInventory)te;
    	for (int n = 0; n < inv.getSizeInventory(); n++)
    	{
    		ItemStack stack = inv.getStackInSlot(n);
    		if (stack!= null)
    		{
    			dropStack(world, i, j, k, stack);
    		}
    	}
    	
    }
    
    /**
     * Drops the given itemstack with some motion (randomized) on the given coordinate
     * Catches drop == null
     */
    public static void dropStack(World world, int i, int j, int k, ItemStack drop)
    {
    	if (drop == null)
    	{
    		return;
    	}
    	float f1 = 0.7F;
	    double d = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
	    double d1 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
	    double d2 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
	    EntityItem entityitem = new EntityItem(world, (double)i + d, (double)j + d1, (double)k + d2, drop);
	    entityitem.delayBeforeCanPickup = 10;
	    world.entityJoinedWorld(entityitem);
    }

    /**
     * Won't be called, but needs to be overridden.
     */
    @Override
    protected TileEntity getBlockEntity()
    {
    	return null;
    }
    
    /**
     * Get's a new IC2TileEntityBlock, based on the metadata of the block.
     * Abstract method! Override with the according TileEntitys!
     */
    public abstract TileEntityBlock getBlockEntity(int meta);

    
    /**
     * Override of the TE-Creation mechanism, includes Meta-Data
     * C&P from BlockContainer
     * super is not necessary, the super of BlockContainer is empty.
     */
    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
    	TileEntity te = getBlockEntity(world.getBlockMetadata(i, j, k));
        world.setBlockTileEntity(i, j, k, te);
    }
    
    /**
     * Override of the TE-Removing code
     * C&P from BlockContainer
     * super is not necessary, the super of BlockContainer is empty.
     */
    @Override
    public void onBlockRemoval(World world, int i, int j, int k)
    {
    	TileEntity te = getBlockEntity(world.getBlockMetadata(i, j, k));
        world.removeBlockTileEntity(i, j, k);
    }

    /**
     * Imported method from BlockFurnace. Will automatically set facing value of the relate TileEntity.
     */
    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if(l == 0)
        {
            setFacing(world,i,j,k,2); // 2 := z-, East
        }
        if(l == 1)
        {
        	setFacing(world,i,j,k,5); // 5 := x+, South
        }
        if(l == 2)
        {
        	setFacing(world,i,j,k,3); // 3 := z+, West
        }
        if(l == 3)
        {
        	setFacing(world,i,j,k,4); // 4 := x-, North
        }
    }
    
    /**
     * This method determines whether a machine block is active, by accessing the Block-TE.
     * The value will be false by default, unless your machine did something to change this.
     */
    public static boolean isActive(IBlockAccess iblockaccess, int i, int j, int k)
    {
    	return ((TileEntityBlock)iblockaccess.getBlockTileEntity(i, j, k)).active;
    }
    
    /**
     * This method returns the facing of the block, by accessing the IC2Block-TE.
     * The value should be 2-5.
     */
    public static int getFacing(IBlockAccess iblockaccess, int i, int j, int k)
    {
    	return ((TileEntityBlock)iblockaccess.getBlockTileEntity(i, j, k)).facing;
    }
    
    /**
     * Sets the facing of the referring TileEntity.
     * @param f New Facing value
     */
    public static void setFacing(IBlockAccess iblockaccess, int i, int j, int k, int f)
    {
    	((TileEntityBlock)iblockaccess.getBlockTileEntity(i, j, k)).facing = f;
    }
}
