package net.minecraft.src;

import net.minecraft.client.Minecraft;

import net.minecraft.src.BeerMod.*;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Block;

public class mod_BeerMod extends BaseModMp
{
	/*
	 * Declare new Blocks/Items here.
	 * Naming convention:
	 * block* if it's a block
	 * item* if it's an item
	 * itemXYZ* if there are multiple similar item types, f.e. Dusts.
	 */
	
	// Declare Block's here
	
	public static Block blockBeerMachines = (new BlockBeerMachines(200));
	
	// Declare Item's here
	
	public static Item itemMilledWheat = (new ItemBeerMod(23000, 0)).setItemName("itemMilledWheat");

	
	public mod_BeerMod()
	{
		// Registers this BaseMod for Ingame-ticking, real-time
		ModLoader.SetInGameHook(this, true, true);
		
		
		// Register Block's here. Keep in mind, Item's don't need to be registered
		
		ModLoader.RegisterBlock(blockBeerMachines, ItemBeerMachines.class); // Special Block registering, linking a ItemBlock type to the Block
    	ModLoader.AddLocalization("blockMill.name", "Mill Block");
		
		// Register Name's below.  Keep in mind Item's have to be registered, and multi-blocks dont need named here
		
		// Blocks
		
		
		
		// Items
		
		ModLoader.AddName(itemMilledWheat, "Milled Wheat");
		
		// Register Tile Entitys
		
		ModLoader.RegisterTileEntity(TileEntityBlock.class, "Ultra Basic Machine Block TileEntity");
		ModLoader.RegisterTileEntity(TileEntityMachine.class, "Empty Management TileEntity");
		ModLoader.RegisterTileEntity(TileEntityProcessingMachine.class, "Base Class for Material Processing TileEntity");
		ModLoader.RegisterTileEntity(TileEntityMill.class, "Mill");
		
		// Initialize Processing recipes
		
		TileEntityMill.initRecipes();
	}
	
	/**
	 * Override of BaseModMP method, receives the ID of a GUI and creates a new one accordingly.
	 * Don't ask me how it works, it plainly does.
	 */
	@Override
	public GuiScreen HandleGUI(int screenID)
    {
    /*	player = ModLoader.getMinecraftInstance().thePlayer;
    	if (player==null)
    	{
    		return null;
    	}
    	*/
    	return null;
    }
	
	static 
	{
		/**
		 * Cheating test recipes!
		 */
		
		ModLoader.AddRecipe(new ItemStack(itemMilledWheat, 64), new Object[] {
			"D", Character.valueOf('D'), Block.dirt
        });
		ModLoader.AddRecipe(new ItemStack(Item.wheat, 64), new Object[] {
			"D", Character.valueOf('D'), itemMilledWheat
        });
		 
		/**
		 * Try to keep recipies in chronological order
		 * For the sake of some sort of organization
		 * Also try to keep them in groups of related objects
		 * And comment the groups accordingly
		 */
			 
	}
	
	/**
	 * @return Always return true!
	 */
	public boolean OnTickInGUI(Minecraft minecraft, GuiScreen guiscreen)
	{
		return true;
	}
	/**
	 * @return Always return true!
	 */
	public boolean OnTickInGame(Minecraft minecraft)
	{
		return true;
	}
	
	@Override
	public String Version()
	{
		return "0.00";
		/**
		 * Basic rules regarding version numbers:
		 * +0.01 for each minor fix
		 * rounded up to x.x0 in updates
		 * rounded up to x.00 in big updates
		 */
	}
}