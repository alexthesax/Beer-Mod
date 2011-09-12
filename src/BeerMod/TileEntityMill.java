package net.minecraft.src.BeerMod;

import java.util.HashMap;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.mod_BeerMod;
import net.minecraft.src.BeerMod.TileEntityProcessingMachine;

/**
 * TE for Mill
 * Contains static lists+method of recipes
 * @author alexthesax
 */
public class TileEntityMill extends TileEntityProcessingMachine {

	public TileEntityMill()
	{
		super(150);
		/*
		 * 150 ticks operation Length
		 */

		/*
		 * Inventory consists of the basic 3 slots:
		 * 0 = Input
		 * 1 = Fuel
		 * 2 = Output
		 */
	}

	/**
	 * Initialize recipe-list and standard recipes.
	 * Must be called in mod_IC2 after all blocks were loaded!
	 */
	public static void initRecipes() 
    {
        recipes = new HashMap<Integer, ItemStack>();
        addRecipe(Item.wheat.shiftedIndex, new ItemStack(mod_BeerMod.itemMilledWheat));
    }
	
	@Override
	public String getInvName()
	{
		return "Mill";
	}
}
