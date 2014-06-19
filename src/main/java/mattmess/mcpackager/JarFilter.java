package mattmess.mcpackager;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class JarFilter extends FileFilter {
	

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String ext = Utils.getExtension(f);
		if (ext.equalsIgnoreCase(Utils.jar))
			return true;
		else return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Jar Archives";
	}

}
