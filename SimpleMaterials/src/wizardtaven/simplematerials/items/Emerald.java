package wizardtaven.simplematerials.items;

import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.SoundEffect;

import wizardtaven.simplematerials.SimpleMaterials;

public class Emerald extends GenericCustomItem implements Listener {
	
	private final SimpleMaterials _SimpleMaterials;
	
	public final static String _name = "Emerald";
	public final static String _textureURL = "http://dl.dropbox.com/u/82409066/items/emerald.png";

	/**
	 * Constructor Method.
	 */
	public Emerald(SimpleMaterials instance) {
		super(instance, _name, _textureURL);
		_SimpleMaterials = instance;
		SpoutManager.getFileManager().addToPreLoginCache(instance, _textureURL);
		_SimpleMaterials.getServer().getPluginManager().registerEvents(this, _SimpleMaterials);
		_SimpleMaterials.sendConsoleMessage("Item '" + _name + "' Loaded with ID: " + this.getRawId() + ":" + this.getCustomId());
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();

		if (e.getRightClicked().getType() == EntityType.VILLAGER) {
			SpoutItemStack item = new SpoutItemStack(p.getItemInHand());
			if(item.getMaterial() instanceof Emerald){
		    	Random rand = new Random();
		    	int diceroll = rand.nextInt(5);
		    	if(diceroll == 0){
					p.sendMessage("A Testificate says, 'Get that trash outta my face!'");
		    	}
		    	else if(diceroll == 1){
					p.sendMessage("A Testificate says, 'How bout ya go get some REAL money?'");
		    	}
		    	else if(diceroll == 2){
					p.sendMessage("A Testificate says, 'If it ain't a Gold Nose, I ain't tradin''");
		    	}
		    	else if(diceroll == 3){
					p.sendMessage("A Testificate says, 'What're you try'n pull, bub?'");
		    	}
		    	else {
					p.sendMessage("A Testificate says, 'What da' Unda'realm am I supposed ta do with this piece a garbage?'");
		    	}
		    	
					p.sendMessage(ChatColor.YELLOW + "The Emerald is slapped out of your hand!");
					SpoutManager.getSoundManager().playSoundEffect((SpoutPlayer) p, SoundEffect.BOW, p.getLocation(), 8, 100);
					p.getWorld().dropItemNaturally(e.getRightClicked().getLocation(), item);
					ItemStack NewStack = new ItemStack(Material.AIR);
					p.setItemInHand(NewStack);
					
			}
		}
	}
}
	