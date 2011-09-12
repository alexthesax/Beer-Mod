package net.minecraft.src.BeerMod;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

/**
 * Special ItemBlock Override
 * Causes Meta-Placing and custom names on 1 ID.
 * @author alexthesax
 *
 */
public class ItemBeerMachines extends ItemBlock
{
    public ItemBeerMachines(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    /**
     * Causes Blocks to be placed with their damage/meta-value
     */
    @Override
    public int getPlacedBlockMetadata(int i)
    {
        return i;
    }

	/**
	 * Returns custom string-titles based on the meta-value
	 */
    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
    	int meta = itemstack.getItemDamage();
        switch(meta)
        {
        case 0: return "blockMill";
        default: return null; // Shouldn't be happening
        }
    }
}
