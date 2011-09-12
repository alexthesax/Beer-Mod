package net.minecraft.src.BeerMod;

import net.minecraft.src.Item;
import net.minecraft.src.forge.ITextureProvider;

/**
 * Base class to implement non-functional Items with the MCForge Texture-Override
 * @author alexthesax
 */
public class ItemBeerMod extends Item implements ITextureProvider
{
	/**
	 * Imported constructor of Item.
	 * @param id ID of the Item
	 * @param index Sprite-Index of this item
	 */
	public ItemBeerMod(int id, int index)
	{
		super(id);
		setIconIndex(index);
	}

	/**
	 * MCForge's texture-override.
	 */
	@Override
	public String getTextureFile()
	{
		return "/BeerModSprites/item.png";
	}
	
}
