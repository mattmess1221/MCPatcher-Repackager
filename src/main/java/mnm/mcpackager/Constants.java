package mnm.mcpackager;

import java.io.File;

public class Constants {

	// General
	public final static String NAME = "MCPatcher Repackager";
	public final static String VERSION = "1.3";
	public final static String OS_NAME = System.getProperty("os.name").toLowerCase();
	public final static String USER_HOME = System.getProperty("user.home");
	public final static File MINECRAFT_DIR = getMinecraftDir();
	
	// File Names
	public final static String RUNTIME = "runtime.jar";
	public final static String MCPATCHER_DATA_PACK = "mcpatcher_data.pack";
	public final static String MCPATCHER_ASSETS = "assets/minecraft/mcpatcher/";
	
	// MCPatcher properties entry names
	public final static String PROPERTIES = "mcpatcher.properties";
	public final static String PROP_MINECRAFT_VERSION = "minecraftVersion";
	public final static String PROP_PATCHER_VERSION = "patcherVersion";
	public final static String PROP_ADDED_CLASSES = "addedClasses";
	public final static String PROP_MODIFIED_CLASSES = "modifiedClasses";
	
	// ITweaker related
	public final static int TWEAK_ORDER = -1000;
	public final static String TWEAK_NAME = "MCPatcher";
	public final static String TWEAK_CLASS = "com.prupe.mcpatcher.launch.MCPatcherTweaker";
	public final static String TWEAK_AUTHOR = "Kahr";
	
	// Packaging methods
	public final static String DROPDOWN_LIBRARY_NAME = "Library";
	public final static String DROPDOWN_TRANSFORMER_NAME = "Transformer (WIP)";
	
	private static File getMinecraftDir() {
		switch (OS_NAME.substring(0, 3)) {
		case "win":
			return new File(new File(System.getenv("APPDATA")), ".minecraft");
		case "mac":
			return new File(USER_HOME, "Library/Application Support/minecraft");
		default:
		case "sun":
		case "uni":
		case "lin":
			return new File(USER_HOME, ".minecraft");
		}
	}


}
