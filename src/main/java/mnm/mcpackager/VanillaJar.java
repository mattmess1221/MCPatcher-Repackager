package mnm.mcpackager;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class VanillaJar {
	
	private String version;
	private File file;
	private JarFile jarFile;
	
	public VanillaJar(String version) throws IOException{
		this.version = version;
		this.file = new File(new File(new File(Constants.MINECRAFT_DIR, "versions"), version), version + ".jar");
		this.jarFile = new JarFile(file);
	}
	
	public String getVersion(){
		return version;
	}
	
	public File getFile(){
		return file;
	}
	
	public JarFile getJar(){
		return jarFile;
	}
}
