package wizardtaven.simplematerials.blocks;

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
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.block.GenericCustomBlock;

import wizardtaven.simplematerials.SimpleMaterials;
import wizardtaven.simplematerials.util.ShapeManager;

/**
 * 
 * @author Lahcania
 *
 */
public class Rope extends GenericCustomBlock {
	
	private SimpleMaterials _SimpleMaterials;
	
	public static final String _name = "Rope";
	public final String _shapeFileName = "rope2";
	public final String _textureURL = "http://dl.dropbox.com/u/82409066/Textures/rope.png";

	/**
	 * Constructs the Table.
	 */
	public Rope(SimpleMaterials instance) {
		super(instance, _name, 65);
		_SimpleMaterials = instance;
		setProperties();
		createRecipe();
		createDesign();
		SpoutManager.getFileManager().addToPreLoginCache(_SimpleMaterials, _textureURL);
		_SimpleMaterials.sendConsoleMessage("Block '" + _name + "' Loaded with SpoutID: " + Integer.toString(this.getCustomId()));
	}
	
	private void setProperties() {
		this.setName(_name);
		this.setOpaque(true);
		this.setRotate(false);
		this.setFriction(0.6f);
		this.setHardness(1.0f);
		this.setLightLevel(0);
	}

	private void createDesign() {
		 GenericBlockDesign design = new GenericBlockDesign();

		 // Create Textures
		 Texture texture = new Texture(_SimpleMaterials, _textureURL, 16, 32, 32);
		 SubTexture subTextures[] = new SubTexture[2];
		 subTextures[0] = new SubTexture(texture, 0, 0, 8, 32);
		 subTextures[1] = new SubTexture(texture, 8, 24, 8, 8);
		 
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
		
		design.setMinBrightness(0.0f);
		design.setMaxBrightness(0.5f);
		
		this.setBlockDesign(design);
	}

	/**
	 * Create Recipe
	 */
	private void createRecipe() {
		ItemStack result = new SpoutItemStack(this, 1);
		SpoutShapedRecipe recipe = new SpoutShapedRecipe(result);
		recipe.shape(
				" A ", 
				" A ", 
				" A ");
		recipe.setIngredient('A', MaterialData.string);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
	}
	
	/**
	 * Only hangable
	 */
	public void onBlockPlace(World w, int x, int y, int z) { 
		Location loc = new Location(w, x, y+1, z);
		SpoutBlock b = (SpoutBlock) w.getBlockAt(loc);
		if(b.getType() == Material.AIR) {
			Location oloc = new Location(w,x,y,z);
			SpoutBlock pb = (SpoutBlock) w.getBlockAt(oloc);
			pb.setCustomBlock(null);
			pb.setType(Material.AIR);
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int changedId) { 
    	Location loc = new Location(world,x,y+1,z);
    	SpoutBlock bl = (SpoutBlock) world.getBlockAt(loc);
    	if(bl.getType() == Material.AIR) {
    		Location oloc = new Location(world,x,y,z);
			SpoutBlock pb = (SpoutBlock) world.getBlockAt(oloc);
			pb.setCustomBlock(null);
			pb.setType(Material.AIR);
    	}
	} 
	/**
	 * Climbable?
	 */
	//@EventHandler
}
