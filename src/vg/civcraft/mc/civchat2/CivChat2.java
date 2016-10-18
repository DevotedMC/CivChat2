package vg.civcraft.mc.civchat2;

import java.io.File;
import java.util.LinkedList;

import vg.civcraft.mc.civchat2.command.CivChat2CommandHandler;
import vg.civcraft.mc.civchat2.database.DatabaseManager;
import vg.civcraft.mc.civchat2.listeners.CivChat2Listener;
import vg.civcraft.mc.civchat2.utility.CivChat2Config;
import vg.civcraft.mc.civchat2.utility.CivChat2FileLogger;
import vg.civcraft.mc.civchat2.utility.CivChat2Log;
import vg.civcraft.mc.civchat2.CivChat2Manager;
import vg.civcraft.mc.civmodcore.ACivMod;
import vg.civcraft.mc.civmodcore.command.CommandHandler;
import vg.civcraft.mc.namelayer.permission.PermissionType;

/**
 * @author jjj5311
 *
 */
public class CivChat2 extends ACivMod {
	
	private static CivChat2 instance;
	private static CivChat2Log log_;
	private static boolean groupsEnabled;
	private static CivChat2Config config_;
	private static CivChat2Manager chatMan;
	private CivChat2Listener chatListener;
	private CivChat2FileLogger fileLog;
	private DatabaseManager DBM;
	
	public void onEnable(){
		//onEnable stuff
		StringBuilder sb = new StringBuilder();
		instance = this;
		config_ = new CivChat2Config();
		config_.setConfigOptions(getConfig());
		if(!new File(sb.append(this.getDataFolder()).append("config.yml").toString()).exists()){
			//config.yml does not exist save the default
			this.saveDefaultConfig();
		}
		sb.delete(0, sb.length());
		config_.getDebug();
		log_ = new CivChat2Log();
		log_.initializeLogger(instance);
		fileLog = new CivChat2FileLogger();
		DBM = new DatabaseManager();
		chatMan = new CivChat2Manager(instance);
		groupsEnabled = config_.getGroupsEnabled();
		log_.info(sb.append("groupsEnabled is set to: ")
					.append(groupsEnabled)
					.toString());
		sb.delete(0, sb.length());
		log_.debug("Debug Enabled");
		handle = new CivChat2CommandHandler();
		handle.registerCommands();
		
		chatListener = new CivChat2Listener(chatMan);
		registerNameLayerPermissions();
		registerEvents();
	}
	
	public void onDisable(){
		//onDisable stuff
	}
	
	public CivChat2Manager getCivChat2Manager(){
		return CivChat2.chatMan;
	}

	public static boolean debugEnabled() {
		return config_.getDebug();
	}
	
	public static void debugmessage(String msg){
		log_.debug(msg);
	}

	public static CivChat2Log getCivChat2Log() {
		return log_;
	}
	
	public void registerEvents(){
		getServer().getPluginManager().registerEvents(chatListener, instance);
	}
	
	@SuppressWarnings("unchecked")
	public void registerNameLayerPermissions() {
		LinkedList <Integer> membersAndAbove = new LinkedList<Integer>();
		membersAndAbove.add(1);
		membersAndAbove.add(2);
		membersAndAbove.add(3);
		PermissionType.registerPermission("READ_CHAT", (LinkedList<Integer>) membersAndAbove.clone(), "Allows sending messages in this groups chat");
		PermissionType.registerPermission("WRITE_CHAT", (LinkedList<Integer>) membersAndAbove.clone(), "Allows receiving messages sent in this groups chat");
	}

	public CommandHandler getCivChat2CommandHandler() {
		return handle;
	}

	public static void warningMessage(String errorMsg) {
		log_.warning(errorMsg);		
	}
	
	public static void infoMessage(String infoMsg){
		log_.info(infoMsg);
	}

	public static CivChat2 getInstance() {
		return instance;
	}

	public static void severeMessage(String severeMsg) {
		log_.severe(severeMsg);
	}

	public CivChat2Config getPluginConfig() {
		return config_;
	}

	public CivChat2FileLogger getCivChat2FileLogger() {
		return fileLog;
	}
	
	public DatabaseManager getDatabaseManager(){
		return this.DBM;
	}
	
	@Override
	protected String getPluginName() {
		return "CivChat2";
	}
}
