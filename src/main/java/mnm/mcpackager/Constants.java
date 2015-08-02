package mnm.mcpackager;

import java.io.File;
import java.net.URI;

public class Constants {

    // General
    public final static String NAME = "MCPatcher Repackager";
    public final static String VERSION = "1.4";
    public final static String OS_NAME = System.getProperty("os.name").toLowerCase();
    public final static String USER_HOME = System.getProperty("user.home");
    public final static File MINECRAFT_DIR = getMinecraftDir();

    // File Names
    public final static String RUNTIME = "runtime.jar";
    public final static String MCPATCHER_ASSETS = "assets/minecraft/mcpatcher/";
    public final static String PROPERTIES = "mcpatcher.properties";

    // MCPatcher properties entry names
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

    // URLs
    public final static URI URI_SOURCE = URI.create("https://github.com/killjoy1221/MCPatcher-Repackager/");
    public final static URI URI_GSON = URI.create("https://code.google.com/p/google-gson/");
    public final static URI URI_COMMONS_IO = URI.create("http://commons.apache.org/io/");
    public final static URI URI_LAUNCH_WRAPPER = URI.create("https://github.com/Mojang/LegacyLauncher/");
    public static final URI URI_MCPATCHER = URI.create("http://www.minecraftforum.net/forums/topic/1226351-1");

    private static File getMinecraftDir() {
        if (OS_NAME.contains("win"))
            return new File(new File(System.getenv("APPDATA")), ".minecraft");
        else if (OS_NAME.contains("mac"))
            return new File(USER_HOME, "Library/Application Support/minecraft");
        else
            return new File(USER_HOME, ".minecraft");
    }
}
