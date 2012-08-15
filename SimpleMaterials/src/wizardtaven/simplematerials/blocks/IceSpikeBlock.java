package wizardtaven.simplematerials.blocks;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.block.design.GenericBlockDesign;
import org.getspout.spoutapi.block.design.Quad;
import org.getspout.spoutapi.block.design.SubTexture;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.block.GenericCustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.SoundEffect;

import wizardtaven.simplematerials.SimpleMaterials;
import wizardtaven.simplematerials.util.ShapeManager;

/**
 * A Block that damages players.
 * - Does more damage than cactus
 * - Slows players on hit
 * - Can be used to craft Ice Arrows
 * - Can be placed next to each other.
 * - Melts in Hot Biomes
 * 
 * @author Taven
 *
 */
public class IceSpikeBlock extends GenericCustomBlock implements Listener {

	private final SimpleMaterials _SimpleMaterials;
	
	public final static String _name = "Ice Spike";
	public final String _shapeFileName = "icespike";
	public final String _textureURL = "http://dl.dropbox.com/u/82409066/Textures/ice.png";
	
	/**
	 * Block Constructor
	 */
	public IceSpikeBlock(SimpleMaterials instance) {
		super(instance, _name, 81);
		_SimpleMaterials = instance;
		setProperties();
		createRecipe();
		createDesign();
		SpoutManager.getFileManager().addToPreLoginCache(instance, _textureURL);
		_SimpleMaterials.getServer().getPluginManager().registerEvents(this, _SimpleMaterials);
		_SimpleMaterials.sendConsoleMessage("Block '" + _name + "' Loaded with SpoutID: " + Integer.toString(this.getCustomId()));
	}
	
	/**
	 * Creates the Properties of the Block.
	 */
	public void setProperties() {
		this.setName(_name);
		this.setOpaque(false);
		this.setRotate(false);
		this.setFriction(0.7f);
		this.setHardness(0.8f);
		this.setLightLevel(0);
		this.setItemDrop(null);
		this.setStepSound(SoundEffect.FALL_1);
	}
	
	/**
	 * Creates Crafting Recipe for this Block.
	 */
	public void createRecipe() {
		// Ice Spike
		ItemStack result = new SpoutItemStack(this, 6);
		SpoutShapedRecipe recipe = new SpoutShapedRecipe(result);
		recipe.shape(
				" B ", 
				"BAB", 
				" B ");
		recipe.setIngredient('A', MaterialData.shears);
		recipe.setIngredient('B', MaterialData.ice);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
		
		// Ice
		ItemStack result2 = new ItemStack(Material.ICE, 16);
		SpoutShapedRecipe recipe2 = new SpoutShapedRecipe(result2);
		recipe2.shape(
				"   ", 
				" A ", 
				" B ");
		recipe2.setIngredient('A', MaterialData.ghastTear);
		recipe2.setIngredient('B', MaterialData.waterBucket);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe2);
	}
	
	/**
	 * Creates the Full Design of the block.
	 */
	public void createDesign() {
		 GenericBlockDesign design = new GenericBlockDesign();
		 
		 // Create Textures
		 Texture texture = new Texture(_SimpleMaterials, _textureURL, 32, 32, 32);
		 SubTexture subTextures[] = new SubTexture[1];
		 subTextures[0] = new SubTexture(texture, 0, 0, 32, 32);

		// Load Shape and Texture
		ShapeManager shape = new ShapeManager(_SimpleMaterials, _shapeFileName, subTextures);
		if(!shape.loadShapeAndTexture()) { return; }
		
		// Set Texture and Look
		design.setTexture(_SimpleMaterials, texture);
		
		// Set Bounding Box (Does not work)
		design.setBoundingBox(shape._bb[0], shape._bb[1], shape._bb[2], shape._bb[3], shape._bb[4], shape._bb[5]);
		design.setBoundingBox(0, 0, 0, 0, 0, 0);
		
		// Set Quads
		int i = 0;
		design.setQuadNumber(shape._quadCount);
		for(Quad quad : shape._quads) {
			design.setLightSource(i, 0, 1, 0);
			design.setQuad(quad);
			i++;
		}
		
		design.setRenderPass(1);
		design.setMinBrightness(0.0f);
		design.setMaxBrightness(1.0f);
		
		this.setBlockDesign(design);
	}
	
	/** 
	 * Melt the block after a short delay.
	 */
	public void meltBlock(final SpoutBlock bl, boolean instant) {
		if(instant) {
			bl.setCustomBlock(null);
			bl.setType(Material.WATER);
		}
		else {
			_SimpleMaterials.getServer().getScheduler().scheduleSyncDelayedTask(_SimpleMaterials, new Runnable() {
				public void run() {
					bl.setCustomBlock(null);
					bl.setType(Material.WATER);
		        }
			}, 60);
		}
	}
	
	/**
	 * Custom Block Events
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z, int changedId) { 
    	Location loc = new Location(world,x,y,z);
    	SpoutBlock bl = (SpoutBlock) world.getBlockAt(loc);
    	if(changedId == 10 // Lava still
    			|| changedId == 11 // Lava flow
    			|| changedId == 50 // Torch
    			|| changedId == 89 // GlowStone
    			|| changedId == 51) { // Fire
    		meltBlock(bl, false);
    	}
    	else if(changedId == 33	|| changedId == 29) { // Piston. Change right away.
    		meltBlock(bl, true);
    	}
    	else if(changedId == 81) {
    		Block b = world.getBlockAt(loc.add(0, 1, 0));
    		if(b.getTypeId() == 81) {
    			b.setType(Material.AIR);
    		}
    		
    		
    	}
	}
	 
    public void onBlockPlace(World world, int x, int y, int z, LivingEntity entity) {  }
 
    /**
     * Melt when placed in hot climates.
     */
    public void onBlockPlace(World world, int x, int y, int z) { 
    	Location loc = new Location(world,x,y,z);
    	SpoutBlock bl = (SpoutBlock) world.getBlockAt(loc);
    	Biome b = world.getBlockAt(loc).getBiome();
    	if(b == Biome.DESERT || b == Biome.DESERT_HILLS
				|| b == Biome.HELL || b == Biome.JUNGLE
				|| b == Biome.JUNGLE_HILLS || b == Biome.SWAMPLAND) {
    		meltBlock(bl, false);
    	}
    }
 
    public void onBlockDestroyed(World world, int x, int y, int z, org.bukkit.entity.LivingEntity living) {   }
    
    public void onBlockDestroyed(World world, int x, int y, int z) {  }
 
    public boolean onBlockInteract(World world, int x, int y, int z, SpoutPlayer player) {
        return true;
    }
 
    public void onEntityMoveAt(World world, int x, int y, int z, Entity entity) {
    	if(entity instanceof LivingEntity) {
    		LivingEntity le = (LivingEntity) entity;
    		le.damage(10);
    		le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4));
    	}
    }
 
    public void onBlockClicked(World world, int x, int y, int z, SpoutPlayer player) { }
 
    public boolean isProvidingPowerTo(World world, int x, int y, int z, BlockFace face) { return false; }
    
    public boolean isIndirectlyProvidingPowerTo(World world, int x, int y, int z, BlockFace face) { return false; }
    
    /**
	 * Play a sound when the player crafts Ice 
	 * @param e
	 */
	@EventHandler
	public void onCraftIce(CraftItemEvent e) {
		if(e.getCurrentItem() != null && e.getWhoClicked() instanceof Player) {
			if(e.getCurrentItem().getType() == Material.ICE) {
				Player p = (Player) e.getWhoClicked();
				World w = p.getWorld();
				w.playEffect(p.getLocation(), Effect.EXTINGUISH, 8);
				w.playEffect(p.getLocation(), Effect.GHAST_SHRIEK, 8);
			}
		}
	}
	
	/**
	 * Ice Spike Bonus Damage, Effect
	 */
	@EventHandler
	public void onIceSpikeDamage(EntityDamageByBlockEvent e) {
		if(e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Block) {
			LivingEntity ent = (LivingEntity) e.getEntity();
			SpoutBlock b = (SpoutBlock) e.getDamager();
			CustomBlock cb =  b.getCustomBlock();
			if(cb != null) {
				if(cb.getName().equals(IceSpikeBlock._name)) {
					ent.damage(3);
					ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4));
				}
			}
		}
	}
	
	/**
	 * Ice Spike Drop
	 */
	@EventHandler
	public void onIceSpikeDrop(BlockBreakEvent e) {
		if(e.getBlock() instanceof Block) {
			SpoutBlock sb = (SpoutBlock) e.getBlock();
			CustomBlock cb = sb.getCustomBlock();
			if(cb != null) {
				if(cb.getName().equals(IceSpikeBlock._name)) {
					Location loc = e.getBlock().getLocation();
					World w = e.getBlock().getWorld();
					sb.setCustomBlock(null);
					sb.setType(Material.AIR);
					Random rand = new Random();
			    	int r = rand.nextInt(3);
			    	if(r == 0) {
			    		SpoutManager.getSoundManager().playGlobalSoundEffect(SoundEffect.GLASS_BREAK_1, loc, 12, 200);
			    	}
			    	if(r == 1) {
			    		SpoutManager.getSoundManager().playGlobalSoundEffect(SoundEffect.GLASS_BREAK_2, loc, 12, 200);
			    	}
			    	if(r == 2) {
			    		SpoutManager.getSoundManager().playGlobalSoundEffect(SoundEffect.GLASS_BREAK_3, loc, 12, 200);
			    	}
			    	final Block b = w.getBlockAt(loc);
			    	_SimpleMaterials.getServer().getScheduler().scheduleSyncDelayedTask(_SimpleMaterials, new Runnable() {
						public void run() {
							b.setType(Material.WATER);
				        }
					}, 1);
				}
			}
		}
	}
}
