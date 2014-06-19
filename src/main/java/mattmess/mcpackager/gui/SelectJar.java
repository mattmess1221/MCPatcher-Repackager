package mattmess.mcpackager.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import mattmess.mcpackager.JarFilter;
import mattmess.mcpackager.Utils;

public class SelectJar implements ActionListener {

	static private JFileChooser jar = new JFileChooser(new File(
			Utils.mcDir, "versions"));
	static public File jarFile;
	static private SelectJar sj = new SelectJar();
	
	public SelectJar(){
		jar.setFileFilter(new JarFilter());
	}
	
	public static SelectJar getFilter(){
		return sj;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		jar.setDialogType(JFileChooser.OPEN_DIALOG);
		
		int returnVal = jar.showOpenDialog(Window.getWindow());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jar.getSelectedFile();
			if (jar.accept(file)) {
				jarFile = file;
				System.out.println("Opening: " + file.getName() + ".");
			}
		} else {
			System.out.println("Open command cancelled by user.");
		}
		
		try{
			Window.setFile(jarFile);
		}catch(NullPointerException exp){}
		Window.getWindow().pack();

	}
}
