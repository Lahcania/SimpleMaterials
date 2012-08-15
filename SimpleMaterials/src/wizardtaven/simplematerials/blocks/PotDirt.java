package wizardtaven.simplematerials.blocks;


import org.bukkit.Material;
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
public class PotDirt extends GenericCustomBlock {
	
	private SimpleMaterials _SimpleMaterials;
	
	public static final String _name = "Pot of Dirt";
	public final String _shapeFileName = "cube";
	public final String _textureURL = "http://dl.dropbox.com/u/82409066/Textures/pot_dirt.png";

	/**
	 * Constructs the Table.
	 */
	public PotDirt(SimpleMaterials instance) {
		super(instance, _name, 3);
		_SimpleMaterials = instance;
		setProperties();
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

}