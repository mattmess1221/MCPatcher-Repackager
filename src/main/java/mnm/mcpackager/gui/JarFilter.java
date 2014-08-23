package mnm.mcpackager.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class JarFilter extends FileFilter {
	

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String ext = getExtension(f);
		if (ext.equalsIgnoreCase("jar"))
			return true;
		else return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Jar Archives";
	}
	
	private String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1)
			ext = s.substring(i + 1).toLowerCase();
		return ext;
	}

}
