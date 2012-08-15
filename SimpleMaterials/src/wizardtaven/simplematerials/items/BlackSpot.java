package wizardtaven.simplematerials.items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import wizardtaven.simplematerials.SimpleMaterials;

public class BlackSpot extends GenericCustomItem implements Listener {
	
	private final SimpleMaterials _SimpleMaterials;
	
	public final static String _name = "The Black Spot";
	public final static String _textureURL = "http://dl.dropbox.com/u/82409066/Textures/ice.png";
	public int _customID = 0;
	
	private Map<String,Integer> _cursedPlayers = new HashMap<String,Integer>();

	/**
	 * Constructor Method.
	 */
	public BlackSpot(SimpleMaterials instance) {
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
				" A ", 
				" B ");
		recipe.setIngredient('A', MaterialData.inkSac);
		recipe.setIngredient('B', MaterialData.paper);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
	}
	
	/**
	 * Event: On Picking up a Black Spot
	 */
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if(e.getItem() instanceof BlackSpot && e.getPlayer() instanceof Player) {
			String pname = e.getPlayer().getName();
			if(_cursedPlayers.containsKey(pname)) {
				int count = _cursedPlayers.get(pname);
				_cursedPlayers.put(pname, count + 1);
			}
			else {
				_cursedPlayers.put(pname, 1);
			}
		}
	}
	
	/**
	 * Event: On Dropping a Black Spot.
	 */
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(e.getItemDrop() instanceof BlackSpot) {
			
		}
	}

}
