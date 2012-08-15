package wizardtaven.simplematerials.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.getspout.spoutapi.block.design.Quad;
import org.getspout.spoutapi.block.design.SubTexture;
import org.getspout.spoutapi.block.design.Vertex;

import wizardtaven.simplematerials.SimpleMaterials;

public class ShapeManager {

	private SimpleMaterials _SimpleMaterials;
	private static String _baseDir;
	
	private File _shapeFile = null;
	private YamlConfiguration _shapeYAML;
	
	public String _shapeName;
	public int _textureCount = 0;
	public float[] _bb;
	public int _quadCount;
	public Quad[] _quads;
	public SubTexture[] _subTextures;
	
	/**
	 * Constructor Method.
	 */
	public ShapeManager(SimpleMaterials instance, String shapeName, SubTexture[] subTextures) {
		_SimpleMaterials = instance;
		_shapeName = shapeName;
		_textureCount = subTextures.length;
		_subTextures = subTextures;
	}
	
	/**
	 * Loads the Texture and Shape
	 * @throws IOException
	 */
	public boolean loadShapeAndTexture() {
		// Load Files and Folders
		try { loadFilesAndFolders(); } 
		catch(IOException ex) { ex.printStackTrace(); _SimpleMaterials.sendConsoleMessage("Failed to load folders."); return false;}
		
		// Check Shape Exists
		if(!_shapeFile.exists()) {
			 _SimpleMaterials.sendConsoleMessage("Failed to find shape " + _shapeName + ".");
			 return false;
		}
		
		// Process Texture
		int[] textureId = new int[_textureCount];
		for (int i = 0; i < _textureCount; i++) {
			textureId[i] = i;
		}
		
		// Load Shape YAML
		_shapeYAML = YamlConfiguration.loadConfiguration(_shapeFile);
		
		// Load Bounding Box
		String boundString = _shapeYAML.getString("BoundingBox");
		String[] bbs = boundString.split(" ");
		if(bbs.length != 6) {
			_SimpleMaterials.sendConsoleMessage("Incorrect BoundingBox data.");
			return false;
		}
		_bb = new float[6];
		for(int i = 0; i < 6; i++) {
			float f = Float.parseFloat(bbs[i]);
			_bb[i] = f;
		}
		
		// Load Quads
		List<?> shapesList = _shapeYAML.getList("Shapes");
		_quadCount = shapesList.toArray().length;
		_quads = new Quad[_quadCount];
		int q = 0;
		for(Object os : shapesList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> shapeMap = (Map<String, Object>) os;
			String cords = (String) shapeMap.get("Coords");
			Quad quad = new Quad(q, _subTextures[(Integer) shapeMap.get("Texture")]);
			int j = 0;
			Vertex verts[] = new Vertex[4];
			for (String line : cords.split("\\r?\\n")) {
				String[] coordLine = line.split(" ");
				verts[j] = new Vertex(0, 0, Float.parseFloat(coordLine[0]), Float.parseFloat(coordLine[1]), Float.parseFloat(coordLine[2]), 0, 0);
				j++;
			}
			
			quad.addVertex(0, verts[3].getX(), verts[3].getY(), verts[3].getZ());
			quad.addVertex(1, verts[0].getX(), verts[0].getY(), verts[0].getZ());
			quad.addVertex(2, verts[1].getX(), verts[1].getY(), verts[1].getZ());
			quad.addVertex(3, verts[2].getX(), verts[2].getY(), verts[2].getZ());
			
			_quads[q] = quad;
			q++;
		}
		return true;
	}
	
	/**
	 * Checks to make sure the config files exist; if not, create them.
	 * 
	 * @throws IOException
	 */
	private void loadFilesAndFolders() throws IOException {	
		
		// Folders
		_baseDir = _SimpleMaterials.getDataFolder().getPath();
		
		// Files
		_shapeFile = new File(_baseDir, _shapeName + ".shape");
		
		// Plugin Directory
		if(!_SimpleMaterials.getDataFolder().exists()) { 
			_SimpleMaterials.getDataFolder().mkdirs();
		}
	}
}
