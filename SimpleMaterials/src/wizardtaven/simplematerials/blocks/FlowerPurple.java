package wizardtaven.simplematerials.blocks;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.block.design.GenericBlockDesign;
import org.getspout.spoutapi.block.design.Quad;
import org.getspout.spoutapi.block.design.SubTexture;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.inventory.SpoutShapelessRecipe;
import org.getspout.spoutapi.material.block.GenericCustomBlock;

import wizardtaven.simplematerials.SimpleMaterials;
import wizardtaven.simplematerials.util.ShapeManager;

/**
 * 
 * @author Taven
 *
 */
public class FlowerPurple extends GenericCustomBlock {
	
	private SimpleMaterials _SimpleMaterials;
	
	public static final String _name = "Sleepyend";
	public final String _shapeFileName = "flower";
	public final String _textureURL = "http://dl.dropbox.com/u/82409066/items/sleepyend.png";

	/**
	 * Constructs the Table.
	 */
	public FlowerPurple(SimpleMaterials instance) {
		super(instance, _name, 37);
		_SimpleMaterials = instance;
		this.setName(_name);
		createRecipes();
		createDesign();
		SpoutManager.getFileManager().addToPreLoginCache(_SimpleMaterials, _textureURL);
		_SimpleMaterials.sendConsoleMessage("Block '" + _name + "' Loaded with SpoutID: " + Integer.toString(this.getCustomId()));
	}

	/**
	 * Create Recipes
	 */
	private void createRecipes() {
		ItemStack result = new ItemStack(Material.INK_SACK, 2, (byte)(15 - DyeColor.PURPLE.getData()));
		SpoutShapelessRecipe recipe = new SpoutShapelessRecipe(result);
		recipe.addIngredient(this);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
	}	
	
	
	/**
	 * Creates the Design of the Block.
	 */
	private void createDesign() {
		 GenericBlockDesign design = new GenericBlockDesign();

		 // Create Textures
		 Texture texture = new Texture(_SimpleMaterials, _textureURL, 32, 32, 32);
		 SubTexture subTextures[] = new SubTexture[1];
		 subTextures[0] = new SubTexture(texture, 0, 0, 32, 32);
		 
		// Set Brightness
		design.setMinBrightness(0.0f);
		design.setMaxBrightness(1.0f);
		 
		// Load Shape Data
		ShapeManager shape = new ShapeManager(_SimpleMaterials, _shapeFileName, subTextures);
		if(!shape.loadShapeAndTexture()) { return; }
		
		// Set Texture
		design.setTexture(_SimpleMaterials, texture);
		
		// Set Quads
		int i = 0;
		design.setQuadNumber(shape._quadCount);
		for(Quad quad : shape._quads) {
			design.setLightSource(i, 0, 1, 0);
			design.setQuad(quad);
			i++;
		}
		
		this.setBlockDesign(design);
	}
	
	/**
	 * Only Place on Dirt
	 */
	public void onBlockPlace(World w, int x, int y, int z) { 
		Location loc = new Location(w, x, y-1, z);
		SpoutBlock b = (SpoutBlock) w.getBlockAt(loc);
		if(b.getType() != Material.GRASS && b.getType() != Material.DIRT) {
			Location oloc = new Location(w,x,y,z);
			SpoutBlock pb = (SpoutBlock) w.getBlockAt(oloc);
			pb.setCustomBlock(null);
			pb.setType(Material.AIR);
		}
	}

}