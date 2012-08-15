package wizardtaven.simplematerials.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.item.GenericCustomItem;
import org.getspout.spoutapi.player.SpoutPlayer;

import wizardtaven.simplematerials.SimpleMaterials;

public class EnderCharm extends GenericCustomItem implements Listener {
	
	private final SimpleMaterials _SimpleMaterials;
	
	public final static String _name = ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "charm";
	public final static String _textureURL = "http://dl.dropbox.com/u/82409066/items/endercharm.png";
	
	private List<String> _markedPlayers = new ArrayList<String>();
	private Map<String,Location> _loadedPortalStones = new HashMap<String,Location>();
	private Map<String,Location> _recentPortalStones = new HashMap<String,Location>();
	private File _portalsFile;
	private YamlConfiguration _portalsYAML;
	private int _idCount = 0;
	
	/**
	 * Constructor Method.
	 */
	public EnderCharm(SimpleMaterials instance) {
		super(instance, _name, _textureURL);
		_SimpleMaterials = instance;
		this.setStackable(false);
		createRecpies();	
		SpoutManager.getFileManager().addToPreLoginCache(instance, _textureURL);
		_SimpleMaterials.getServer().getPluginManager().registerEvents(this, _SimpleMaterials);
		_SimpleMaterials.sendConsoleMessage("Item '" + "EnderCharm" + "' Loaded with ID: " + this.getRawId() + ":" + this.getCustomId());
		loadPortalEffect();
	}
	
	/**
	 * Create the Recipes for EnderCharms and EnderPortals.
	 */
	public void createRecpies() {
		// Ender Charm
		ItemStack result = new SpoutItemStack(this, 1);
		SpoutShapedRecipe recipe = new SpoutShapedRecipe(result);
		recipe.shape(
				" I ", 
				"SPS", 
				" S ");
		recipe.setIngredient('I', MaterialData.ironIngot);
		recipe.setIngredient('S', MaterialData.stone);
		recipe.setIngredient('P', MaterialData.enderPearl);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
		
		// Ender Frame
		ItemStack result2 =  new ItemStack(Material.ENDER_PORTAL_FRAME, 1);
		SpoutShapedRecipe recipe2 = new SpoutShapedRecipe(result2);
		recipe2.shape(
				"EDE", 
				"L L", 
				"EDE");
		recipe2.setIngredient('E', MaterialData.endStone);
		recipe2.setIngredient('D', MaterialData.diamond);
		recipe2.setIngredient('L', MaterialData.lapisBlock);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe2);
	}
	
	/**
	 * On Using the Ender Charm
	 */
	@Override
	public boolean onItemInteract(SpoutPlayer p, SpoutBlock b, BlockFace f)  {
		if(!_markedPlayers.isEmpty() && _markedPlayers.size() > 0) {
			Location ploc = p.getLocation();
			// Distance Check
			if(!_loadedPortalStones.isEmpty()) {
				for(String id : _loadedPortalStones.keySet()) {
					Location sloc = _loadedPortalStones.get(id);
					if(ploc.getBlockX() < (sloc.getBlockX() + 40) && ploc.getBlockX() > (sloc.getBlockX() - 40)) {
						if(ploc.getBlockZ() < (sloc.getBlockZ() + 40) && ploc.getBlockZ() > (sloc.getBlockZ() - 40)) {
							p.sendMessage(ChatColor.YELLOW + "The charm fizzles on the energy near the Ancient Ender Stone.");
							return false;
						}
					}
				}
			}
			if(!_recentPortalStones.isEmpty()) {
				for(String id : _recentPortalStones.keySet()) {
					Location sloc = _recentPortalStones.get(id);
					if(ploc.getBlockX() < (sloc.getBlockX() + 40) && ploc.getBlockX() > (sloc.getBlockX() - 40)) {
						if(ploc.getBlockZ() < (sloc.getBlockZ() + 40) && ploc.getBlockZ() > (sloc.getBlockZ() - 40)) {
							p.sendMessage(ChatColor.YELLOW + "The charm fizzles on the energy near the Ancient Ender Stone.");
							return false;
						}
					}
				}
			}
			Random rand = new Random();
			int randomIndex = rand.nextInt(_markedPlayers.size());
			Player sump = _SimpleMaterials.getServer().getPlayer(_markedPlayers.get(randomIndex));
			if(sump == p) {
				p.sendMessage(ChatColor.YELLOW + "The charm fizzles on the energy around you.");
				return false;
			}
			Location teleloc;
			int skychance = rand.nextInt(100);
			int boomchance = rand.nextInt(100);
			if(skychance < 5) {
				teleloc = new Location(p.getWorld(), ploc.getBlockX(), ploc.getBlockY() + 7272,ploc.getBlockZ());
				sump.sendMessage(ChatColor.YELLOW + "The energies around you crackle and arc! You have been pulled through the void!");
				p.sendMessage(ChatColor.YELLOW + "The charm resonates wildly!");
				_SimpleMaterials.sendConsoleMessage(ChatColor.GREEN + "Player " + sump.getDisplayName() + ChatColor.GREEN + " did not pull to " + p.getDisplayName() + ChatColor.GREEN + " safely.");
			}
			else {
				teleloc = new Location(p.getWorld(), ploc.getBlockX()+1, ploc.getBlockY(),ploc.getBlockZ()+1); 
				sump.sendMessage(ChatColor.YELLOW + "You have been pulled through the void by " + p.getDisplayName() + ChatColor.YELLOW + "!");
				p.sendMessage(ChatColor.YELLOW + "The charm resonates wildly! You pull " + sump.getDisplayName() + ChatColor.YELLOW + " from the void!");
				sump.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 400, 3));
				_SimpleMaterials.sendConsoleMessage(ChatColor.GREEN + "Player " + sump.getDisplayName() + ChatColor.GREEN + " was pulled to " + p.getDisplayName() + ChatColor.GREEN + ".");
			}
			// Teleport the Player
			teleloc.getWorld().strikeLightning(teleloc);
			sump.teleport(teleloc);
			if(boomchance < 20) {
				p.getWorld().strikeLightning(ploc);
				p.getWorld().createExplosion(ploc, 3);
				p.sendMessage(ChatColor.RED + "The charm cracks in an explosion of energy!");
			}
			else {
				p.sendMessage(ChatColor.YELLOW + "The charm melts into the void.");
			}
			// Break the Charm
			int amount = p.getItemInHand().getAmount();
			if(amount == 1) {
				ItemStack air = new ItemStack(Material.AIR, 1);
				p.setItemInHand(air);
			}
			else {
				p.getItemInHand().setAmount(amount - 1);
			}
			// The Final Effect
			p.playEffect(ploc, Effect.ENDER_SIGNAL, 5);
			sump.playEffect(sump.getLocation(), Effect.ENDER_SIGNAL, 5);
		}
		else {
			p.sendMessage(ChatColor.YELLOW + "The charm is silent.");
		}
		return true;
	}
		
	/**
	 * Event: 
	 * Handles clicking on an active Ender Portal
	 * Handles clicking on inactive ender Portal with eye.
	 */
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(e.getClickedBlock() != null) {
			if(e.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME && e.getPlayer() instanceof Player) {
				final Player p = e.getPlayer();
				final String pname = p.getName();
				// Clicks on Active Portal.
				if(e.getClickedBlock().getData() >= 4) {
					if(e.getAction() instanceof Action && e.getAction().name().contains("RIGHT_CLICK")) {
						// Has not been marked.
						if(!_markedPlayers.contains(pname)) {
							_markedPlayers.add(pname);
							p.sendMessage(ChatColor.YELLOW + "Your skin tingles with other-wordly energy.");
							messageCharmHolders(p, true);
							_SimpleMaterials.getServer().getScheduler().scheduleSyncDelayedTask(_SimpleMaterials, new Runnable() {
	            				public void run() {
	            					// Remove mark after 15 seconds.
	        			        	if(_markedPlayers.contains(pname)) {
	        			        		_markedPlayers.remove(pname);
	        			        		if(p.isOnline()) {
	        			        			p.sendMessage(ChatColor.YELLOW + "The sensation fades.");
	        			        			messageCharmHolders(p, false);
	        			        		}
	        			            }
	        			        }
	            			}, 300);
						}
						// Has already been marked.
						else {
							_markedPlayers.remove(pname);
							p.sendMessage(ChatColor.YELLOW + "Upon touching the stone again, the sensation fades.");
							
						}
					}
				}
				else if(e.getItem() != null) {
					if(e.getItem().getType() == Material.EYE_OF_ENDER) {
						p.sendMessage(ChatColor.YELLOW + "The portal awakens.");
						Block b = (Block) e.getClickedBlock();
						final Location loc = new Location(b.getWorld(), b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ());
						final World w = b.getWorld();
						savePortalLocation(w,loc, (Integer.toString(_idCount+1)));
						_recentPortalStones.put(Integer.toString(_idCount+1), loc);
						// Show Glow Effect Until Server Restarts
						_SimpleMaterials.getServer().getScheduler().scheduleSyncRepeatingTask(_SimpleMaterials, new Runnable() {
				            public void run() {
				            	w.playEffect(loc, Effect.ENDER_SIGNAL, 5);
				            }
						}, 0L, 20L);
					}
				}
				else {
					p.sendMessage(ChatColor.YELLOW + "The portal is quiet.");
				}
			}
		}
	}
	
	/**
	 * Messages those holding the charm.
	 */
	private void messageCharmHolders(Player p, boolean sense) {
		for(SpoutPlayer sp : SpoutManager.getOnlinePlayers()) {
			if(sp.getItemInHand().getDurability() == this.getCustomId()) {
				if(!sp.getName().equalsIgnoreCase(p.getName())) {
					if(sense) {
						sp.sendMessage(ChatColor.YELLOW + "You sense " + p.getDisplayName() + ChatColor.YELLOW + " through the charm.");
					}
					else {
						sp.sendMessage(ChatColor.YELLOW + "You no longer sense " + p.getDisplayName() + ChatColor.YELLOW + " through the charm.");
					}
				}
			}
		}
	}
	
	/**
	 * Load and Create Portal Effects
	 */
	private void loadPortalEffect() {
		_portalsFile = new File(_SimpleMaterials.getDataFolder(), "portals.yml");
		checkFilesExist();
		_portalsYAML = YamlConfiguration.loadConfiguration(_portalsFile);
		loadPortalLocations();
		runPortalEffect();
	}
	
	/**
	 * Saves a Portal Location to File.
	 */
	private void savePortalLocation(World w, Location loc, String id) {
		if(w != null && loc != null) {
			_portalsYAML.set("PortalStones." + w.getName() + "." + id + ".x", loc.getBlockX());
			_portalsYAML.set("PortalStones." + w.getName() + "." + id + ".y", loc.getBlockY());
			_portalsYAML.set("PortalStones." + w.getName() + "." + id + ".z", loc.getBlockZ());
			saveYAML();
			_idCount++;
		}
	}

	/**
	 * Load Portal Locations from File.
	 */
	private void loadPortalLocations() {
		if(_portalsYAML.getConfigurationSection("PortalStones") != null) {
			for(String worldname : _portalsYAML.getConfigurationSection("PortalStones").getKeys(false)) {
				World w = _SimpleMaterials.getServer().getWorld(worldname);
				if(w != null) {
					for(String id : _portalsYAML.getConfigurationSection("PortalStones." + worldname).getKeys(false)) {
						int x = _portalsYAML.getInt("PortalStones." + worldname + "." + id + ".x");
						int y = _portalsYAML.getInt("PortalStones." + worldname + "." + id + ".y");
						int z = _portalsYAML.getInt("PortalStones." + worldname + "." + id + ".z");
						_loadedPortalStones.put(id, new Location(w, x, y, z));
						_idCount++;
					}
				}
			}
		}
		else {
			_portalsYAML.createSection("PortalStones");
		}
		saveYAML();
	}
	
	/**
	 * Displays a cool portal effect around portal stones.
	 */
	private void runPortalEffect() {
		_SimpleMaterials.getServer().getScheduler().scheduleSyncRepeatingTask(_SimpleMaterials, new Runnable() {
            public void run() {
            	for(String id : _loadedPortalStones.keySet()) {
            		Location loc = _loadedPortalStones.get(id);
            		loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 5);
            	}
            }
		}, 0L, 20);
	}
	
	/**
	 * Saves the YAML File
	 */
	private void saveYAML() {
		try { _portalsYAML.save(_portalsFile);}
		catch(IOException ex) { ex.printStackTrace(); _SimpleMaterials.sendConsoleMessage(ChatColor.RED + "Failed to Load Name Manager on Enable!"); }
	}
	
	/**
	 * Check Portal File Exists
	 */
	private void checkFilesExist() {
		try {
			if(!_SimpleMaterials.getDataFolder().exists()) {
				_SimpleMaterials.getDataFolder().mkdirs();
			}
			if (!_portalsFile.exists()) {
				_portalsFile.createNewFile();
			}
		}
		catch(IOException ex) { ex.printStackTrace(); _SimpleMaterials.sendConsoleMessage(ChatColor.RED + "Failed to check files for existence!"); }
	}
}