package wizardtaven.simplematerials.blocks;


import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.design.GenericBlockDesign;
import org.getspout.spoutapi.block.design.Quad;
import org.getspout.spoutapi.block.design.SubTexture;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.inventory.SpoutShapelessRecipe;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.block.GenericCustomBlock;

import wizardtaven.simplematerials.SimpleMaterials;
import wizardtaven.simplematerials.util.ShapeManager;

/**
 * 
 * @author Lahcania
 *
 */
public class PotEmpty extends GenericCustomBlock {
	
	private SimpleMaterials _SimpleMaterials;
	
	public static final String _name = "Empty Pot";
	public final String _shapeFileName = "cube";
	public final String _textureURL = "http://dl.dropbox.com/u/82409066/Textures/pot_empty.png";

	/**
	 * Constructs the Table.
	 */
	public PotEmpty(SimpleMaterials instance) {
		super(instance, _name, 20);
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
		 Texture texture = new Texture(_SimpleMaterials, _textureURL, 128, 32, 32);
		 SubTexture subTextures[] = new SubTexture[3];
		 subTextures[0] = new SubTexture(texture, 0, 0, 32, 32);
		 subTextures[1] = new SubTexture(texture, 32, 0, 32, 32);
		 subTextures[2] = new SubTexture(texture, 64, 0, 32, 32);
		 
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
			design.setQuad(quad);
			//design.setLightSource(i, 0, 1, 0);
			i++;
		}
		// Top
		design.setLightSource(0, 0, 1, 0);
		// Bottom
		design.setLightSource(1, 0, 0, 0);
		// Sides
		design.setLightSource(3, 0, 0, 1);
		design.setLightSource(4, 1, 0, 0);
		design.setLightSource(2, 0, 0, -1);
		design.setLightSource(5, -1, 0, 0);

		design.setMinBrightness(0.0f);
		design.setMaxBrightness(1.0f);
		this.setBlockDesign(design);
	}

	/**
	 * Create Recipe
	 */
	private void createRecipe() {
		ItemStack result = new SpoutItemStack(_SimpleMaterials._potdirt, 1);
		SpoutShapedRecipe recipe = new SpoutShapedRecipe(result);
		recipe.shape(
				"A  ", 
				"B  ", 
				"   ");
		recipe.setIngredient('A', MaterialData.dirt);
		recipe.setIngredient('B', this);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
		
		SpoutShapedRecipe recipe2 = new SpoutShapedRecipe(result);
		recipe2.shape(
				" A ", 
				" B ", 
				"   ");
		recipe2.setIngredient('A', MaterialData.dirt);
		recipe2.setIngredient('B', this);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe2);
		
		SpoutShapedRecipe recipe3 = new SpoutShapedRecipe(result);
		recipe3.shape(
				"  A", 
				"  B", 
				"   ");
		recipe3.setIngredient('A', MaterialData.dirt);
		recipe3.setIngredient('B', this);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe3);
		
		SpoutShapedRecipe recipe4 = new SpoutShapedRecipe(result);
		recipe4.shape(
				"   ", 
				"A  ", 
				"B  ");
		recipe4.setIngredient('A', MaterialData.dirt);
		recipe4.setIngredient('B', this);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe4);
		
		SpoutShapedRecipe recipe5 = new SpoutShapedRecipe(result);
		recipe5.shape(
				"   ", 
				" A ", 
				" B ");
		recipe5.setIngredient('A', MaterialData.dirt);
		recipe5.setIngredient('B', this);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe5);
		
		SpoutShapedRecipe recipe6 = new SpoutShapedRecipe(result);
		recipe6.shape(
				"   ", 
				"  A", 
				"  B");
		recipe6.setIngredient('A', MaterialData.dirt);
		recipe6.setIngredient('B', this);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe6);
		
		// Get Back Pot
			ItemStack result7 = new SpoutItemStack(this, 1);
			SpoutShapelessRecipe recipe7 = new SpoutShapelessRecipe(result7);
			recipe7.addIngredient(_SimpleMaterials._potdirt);
			SpoutManager.getMaterialManager().registerSpoutRecipe(recipe7);
		
	}
	//Generates a Random Chance to Drop a Rupee
    public void onBlockDestroyed(World world, int x, int y, int z, LivingEntity living) { 
    	Random rand = new Random();
    	int diceroll = rand.nextInt(100);
    	if(diceroll < 5){
    		Location loc = new Location(world, x, y, z);
    		ItemStack rupee = new SpoutItemStack(_SimpleMaterials._emerald, 1);
    		world.dropItem(loc, rupee);
    	}
    }


}