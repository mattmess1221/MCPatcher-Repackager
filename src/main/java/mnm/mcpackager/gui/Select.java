package mnm.mcpackager.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import mnm.mcpackager.Constants;
import mnm.mcpackager.Repackage;

public class Select implements ActionListener {

	static private JFileChooser jar = new JFileChooser(new File(Constants.MINECRAFT_DIR, "versions"));
	static public File jarFile;
	static private Select sj = new Select();
	
	public Select(){
		jar.setFileFilter(new JarFilter());
	}
	
	public static Select getFilter(){
		return sj;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		jar.setDialogType(JFileChooser.OPEN_DIALOG);
		
		int returnVal = jar.showOpenDialog(Repackage.getWindow().getFrame());

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
			Repackage.getWindow().setFile(jarFile);
		}catch(NullPointerException exp){}
		Repackage.getWindow().resize();
	}
}
