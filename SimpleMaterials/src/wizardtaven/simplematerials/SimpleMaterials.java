package wizardtaven.simplematerials;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.MaterialData;

import wizardtaven.simplematerials.blocks.*;
import wizardtaven.simplematerials.items.*;


public class SimpleMaterials extends JavaPlugin {
	
	public ConsoleCommandSender _cs;
	public final String _prefix = ChatColor.LIGHT_PURPLE + "[SimpleBlocks] ";
	public SimpleMaterials _instance;
	public UnfiredClayPot _unfiredclaypot;
	public PotEmpty _potempty;
	public PotDirt _potdirt;
	public Emerald _emerald;
	
	public void onEnable() {
		_instance = this;
		_cs = getServer().getConsoleSender();
		sendConsoleMessage("Loading Custom Materials...");
		sendConsoleMessage("-----------------BLOCKS------------------");
		// Damaging
		new IceSpikeBlock(this);
		// Tables
		new TableOak(this);
		new TablePine(this);
		new TableBirch(this);
		new TableJungle(this);
		new TableStone(this);
		// Flowers
		new FlowerWhite(this);
		new FlowerBlue(this);
		new FlowerPurple(this);
		//Rope Blocks
		new Rope(this);
		//Farm Blocks
		new WheatBale(this);
		sendConsoleMessage("-----------------ITEMS------------------");
		new Orange(this);
		new EnderCharm(this);
		new DaggerDiamond(this);
		sendConsoleMessage("-----------------POTS------------------");
		_potdirt = new PotDirt(this);
		_potempty = new PotEmpty(this);
		_emerald = new Emerald(this);
		_unfiredclaypot = new UnfiredClayPot(this);
		sendConsoleMessage("-----------------------------------");
		sendConsoleMessage("Custom Materials Loaded!");
		SpoutManager.getMaterialManager().setItemName(MaterialData.endPortalFrame, "Ancient Ender Stone");
		SpoutManager.getMaterialManager().setItemName(MaterialData.mushroomSoup, "Tankard of Mead");
		SpoutManager.getMaterialManager().setItemName(MaterialData.bowl, "Empty Tankard");
		SpoutManager.getMaterialManager().setItemName(MaterialData.cookedChicken, "Veggie Ranch Trio Pizza");
		SpoutManager.getMaterialManager().setItemName(MaterialData.rawChicken, "Raw Pizza");
	}
	
	/**
	 * Sends a Colored Message to the Console.
	 */
	public void sendConsoleMessage(String msg) {
		_cs.sendMessage(_prefix + ChatColor.AQUA + msg);
	}
}