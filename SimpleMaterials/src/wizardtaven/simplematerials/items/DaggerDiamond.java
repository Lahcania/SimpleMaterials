package wizardtaven.simplematerials.items;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.item.GenericCustomTool;

import wizardtaven.simplematerials.SimpleMaterials;

public class DaggerDiamond extends GenericCustomTool implements Listener {
	
	private final SimpleMaterials _SimpleMaterials;
	public final static String _name = "Diamond Dagger";
	public final static String _textureURL = "http://dl.dropbox.com/u/82409066/items/dagger_diamond.png";
	
	public DaggerDiamond(SimpleMaterials instance) {
		super(instance, _name, _textureURL);
		_SimpleMaterials = instance;
		this.setStackable(false);
		this.setMaxDurability((short) 1562);
		//createRecpies();	
		SpoutManager.getFileManager().addToPreLoginCache(instance, _textureURL);
		_SimpleMaterials.getServer().getPluginManager().registerEvents(this, _SimpleMaterials);
		_SimpleMaterials.sendConsoleMessage("Item '" + "EnderCharm" + "' Loaded with ID: " + this.getRawId() + ":" + this.getCustomId());
	}
	
	 @EventHandler(priority = EventPriority.HIGHEST)
	  public void onStrike(EntityDamageByEntityEvent e) {
		 if(e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
			 Player p = (Player) e.getDamager();
		      if (p.getItemInHand() != null) {
		    	  SpoutItemStack item = new SpoutItemStack(p.getItemInHand());
		    	  if (item.getMaterial() instanceof DaggerDiamond) {
		    		  LivingEntity v = (LivingEntity) e.getEntity();
		    		  DaggerDiamond dagger = (DaggerDiamond)item.getMaterial();
		    		  Location ploc = p.getLocation();
		    		  Location vloc = v.getLocation();
		    		   
		    		  if(ploc.getBlockX() < (vloc.getBlockX() + 1) && ploc.getBlockX() > (vloc.getBlockX() - 1)) {
							if(ploc.getBlockZ() < (vloc.getBlockZ() + 1) && ploc.getBlockZ() > (vloc.getBlockZ() - 1)) {
								if(ploc.getBlockY() < (vloc.getBlockY() + 1) && ploc.getBlockY() > (vloc.getBlockY() - 1)) {
									e.setDamage(24);
								}
							}
		    		  }
		    		  else if(ploc.getBlockX() < (vloc.getBlockX() + 2) && ploc.getBlockX() > (vloc.getBlockX() - 2)) {
							if(ploc.getBlockZ() < (vloc.getBlockZ() + 2) && ploc.getBlockZ() > (vloc.getBlockZ() - 2)) {
								if(ploc.getBlockY() < (vloc.getBlockY() + 2) && ploc.getBlockY() > (vloc.getBlockY() - 2)) {
									e.setDamage(18);
								}
							}
		    		  }
		    		  else if(ploc.getBlockX() < (vloc.getBlockX() + 3) && ploc.getBlockX() > (vloc.getBlockX() - 3)) {
							if(ploc.getBlockZ() < (vloc.getBlockZ() + 3) && ploc.getBlockZ() > (vloc.getBlockZ() - 3)) {
								if(ploc.getBlockY() < (vloc.getBlockY() + 3) && ploc.getBlockY() > (vloc.getBlockY() - 3)) {
									e.setDamage(6);
								}
							}
		    		  }  
		    		  else {
		    			  e.setDamage(0);
		    			  e.setCancelled(true);
		    		  }
		    	  }      
		      }
		 }
	 }
}
