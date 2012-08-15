package wizardtaven.simplematerials.items;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import com.github.Zarklord1.FurnaceApi.FurnaceRecipes;

import wizardtaven.simplematerials.SimpleMaterials;

public class UnfiredClayPot extends GenericCustomItem implements Listener {
	
	private final SimpleMaterials _SimpleMaterials;
	
	public final static String _name = "Unfired Clay Pot";
	public final static String _textureURL = "http://dl.dropbox.com/u/82409066/items/unfiredpot.png";
	public int _customID = 0;

	/**
	 * Constructor Method.
	 */
	public UnfiredClayPot(SimpleMaterials instance) {
		super(instance, _name, _textureURL);
		_SimpleMaterials = instance;
		_customID = this.getCustomId();
		createRecipes();
		SpoutManager.getFileManager().addToPreLoginCache(instance, _textureURL);
		_SimpleMaterials.sendConsoleMessage("Item '" + _name + "' Loaded with ID: " + this.getRawId() + ":" + this.getCustomId());
	}

	/**
	 * Create Recipes
	 */
	private void createRecipes() {		
		ItemStack result = new SpoutItemStack(this, 1);
		SpoutShapedRecipe recipe = new SpoutShapedRecipe(result);
		recipe.shape(
				"   ", 
				"A A", 
				" A ");
		recipe.setIngredient('A', MaterialData.clay);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
		
		// Fire Pot
		
		
		/**
		 *      ItemStack result = new SpoutItemStack(item, 1);
                int id  = block.getId();
                int data = block.getCustomId();
                FurnaceRecipes.CustomFurnaceRecipe(result, id, data);
		 */
		
 		

		ItemStack result2 = new SpoutItemStack(_SimpleMaterials._potempty, 1);
		FurnaceRecipes.CustomFurnaceRecipe(result2, 318, this.getCustomId());
		//FurnaceRecipes.CustomFurnaceRecipe(ItemStack Result/*the Result*/,int  id/*the id has to be 318 if its a spout block otherwise its just the regular block id*/, int data/*the metadata id of the block for spout type the name of your item/block variable then (without the Quotes)".getCustomId();"*/);
		//FurnaceRecipes.CustomFurnaceRecipe(result,_SimpleMaterials._unfiredclaypot.getRawId(),_SimpleMaterials._unfiredclaypot.getCustomId());
		//firerecipe = new SpoutFurnaceRecipe(new SpoutItemStack(_SimpleMaterials._unfiredclaypot), new SpoutItemStack(this));
	}
	
}