package mattmess.mcpackager;

import java.io.File;

public class Utils {
	public final static String jar = "jar";
	public final static String osName = System.getProperty("os.name").toLowerCase();
	public final static String userHome = System.getProperty("user.home");
	public final static File mcDir = getMinecraftDir();
	public final static String version = "1.2";

	private static File getMinecraftDir() {
		switch (osName.substring(0, 3)) {
		case "win":
			return new File(new File(System.getenv("APPDATA")), ".minecraft");
		case "mac":
			return new File(userHome, "Library/Application Support/minecraft");
		default:
		case "sun":
		case "uni":
		case "lin":
			return new File(userHome, ".minecraft");
		}
	}

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1)
			ext = s.substring(i + 1).toLowerCase();
		return ext;
	}

}
