package wizardtaven.simplematerials.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.player.SpoutPlayer;

import wizardtaven.simplematerials.SimpleMaterials;

public class Orange extends GenericCustomItem implements Listener {
	
	private final SimpleMaterials _SimpleMaterials;
	
	public final static String _name = "Orange";
	public final static String _textureURL = "http://dl.dropbox.com/u/82409066/items/orange.png";

	/**
	 * Constructor Method.
	 */
	public Orange(SimpleMaterials instance) {
		super(instance, _name, _textureURL);
		_SimpleMaterials = instance;
		SpoutManager.getFileManager().addToPreLoginCache(instance, _textureURL);
		_SimpleMaterials.sendConsoleMessage("Item '" + _name + "' Loaded with ID: " + this.getRawId() + ":" + this.getCustomId());
	}
	
	/**
	 * Override
	 * On Interacting with Item (Right Click)
	 */
	@Override
	public boolean onItemInteract(SpoutPlayer p, SpoutBlock b, BlockFace f)  {
		p.sendMessage(ChatColor.YELLOW + "You carefully peel and savor the orange. So Delicious!");
		int health = p.getHealth();
		int food = p.getFoodLevel();
		// Heal Player
		if(health + 8 >= p.getMaxHealth()) {
			p.setHealth(p.getMaxHealth());
		}
		else {
			p.setHealth(health + 8);
		}
		// Nourish Player
		if(food + 6 >= 20) {
			p.setFoodLevel(20);
		}
		else {
			p.setFoodLevel(food + 6);
		}
		p.setSaturation(p.getSaturation() + 0.5f);
		// Cure Player
		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(p.getFoodLevel());
		if(p.hasPotionEffect(PotionEffectType.HUNGER)) {
			p.removePotionEffect(PotionEffectType.HUNGER);
			p.sendMessage(ChatColor.YELLOW + "Your scurvy! It's been cured!");
		}
		// Water Breathing
		p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 300, 4));
		// Reduce Item
		int amount = p.getItemInHand().getAmount();
		if(amount == 1) {
			ItemStack air = new ItemStack(Material.AIR, 1);
			p.setItemInHand(air);
		}
		else {
			p.getItemInHand().setAmount(amount - 1);
		}
		return true;
	}
}
