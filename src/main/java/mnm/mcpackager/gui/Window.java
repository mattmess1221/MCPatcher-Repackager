package mnm.mcpackager.gui;

import mnm.mcpackager.Constants;
import mnm.mcpackager.Repackage;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Window {

	private JFrame frmRepackager;
	private JLabel lblSelectedJar;
	private File jarFile;
	private JButton btnAbout;

	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Window() {
		initialize();
	}

	private void initialize(){
		frmRepackager = new JFrame();
		frmRepackager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRepackager.setTitle("MCPatcher Repackager v" + Constants.VERSION);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frmRepackager.setBounds(dim.width/3, dim.height/3, 368, 131);
		frmRepackager.setResizable(false);

		JButton btnSelectJar = new JButton("Select Jar");
		btnSelectJar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				selectJar();
			}
		});
		frmRepackager.getContentPane().setLayout(null);
		btnSelectJar.setBounds(164, 66, 89, 23);
		frmRepackager.getContentPane().add(btnSelectJar);

		JButton btnRepackage = new JButton("Repackage");
		btnRepackage.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Repackage().start();
			}
		});
		btnRepackage.setIcon(null);
		btnRepackage.setBounds(263, 66, 89, 23);
		frmRepackager.getContentPane().add(btnRepackage);

		JLabel lblSelectedJarText = new JLabel("Selected Jar:");
		lblSelectedJarText.setBounds(28, 39, 75, 16);
		frmRepackager.getContentPane().add(lblSelectedJarText);

		lblSelectedJar = new JLabel();
		lblSelectedJar.setBounds(113, 38, 239, 17);
		frmRepackager.getContentPane().add(lblSelectedJar);

		btnAbout = new JButton("About");
		btnAbout.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				About.openHelp(Window.this.frmRepackager);
			}
		});
		btnAbout.setBounds(10, 66, 89, 23);
		frmRepackager.getContentPane().add(btnAbout);

		JLabel lblMcpatcherRepackager = new JLabel("MCPatcher Repackager");
		lblMcpatcherRepackager.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblMcpatcherRepackager.setBounds(28, 11, 160, 17);
		frmRepackager.getContentPane().add(lblMcpatcherRepackager);
	}

	/*
	 * Opens a File Chooser
	 */
	private void selectJar() {
		JFileChooser dlgSelectJar = new JFileChooser(new File(Constants.MINECRAFT_DIR, "versions"));
		dlgSelectJar.setDialogTitle("Select Jar");
		dlgSelectJar.setFileFilter(new JarFilter());
		dlgSelectJar.setDialogType(JFileChooser.OPEN_DIALOG);

		int returnVal = dlgSelectJar.showOpenDialog(this.frmRepackager);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = dlgSelectJar.getSelectedFile();
			if (dlgSelectJar.accept(file)) {
				jarFile = file;
				System.out.println("Opening: " + file.getName() + ".");
			}
		} else {
			System.out.println("Open command cancelled by user.");
		}

		try{
			setFile(jarFile);
		}catch(NullPointerException exp){}
	}

	public void setFile(File file) {
		System.out.println("Selecting " + file.getName());
		lblSelectedJar.setText(file.getName() + "  ");
	}

	public File getJar(){
		return this.jarFile;
	}

	public void newDialog(String message) {
		JOptionPane.showMessageDialog(frmRepackager, message);
	}

	public JFrame getFrame() {
		return frmRepackager;
	}
}
