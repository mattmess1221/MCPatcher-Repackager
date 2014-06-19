package mattmess.mcpackager.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mattmess.mcpackager.Repackager;
import mattmess.mcpackager.Utils;

public class Window {
	private static JFrame frame = new JFrame();
	private static JPanel pane = new JPanel();
	private static JLabel label = new JLabel();
	private static JButton select = new JButton();
	private static JButton repackage = new JButton();
	private static JPanel pane1 = new JPanel();

	public Window() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setTitle("MCPatcher Repackager v" + Utils.version);
		frame.add(pane1);
		frame.add(pane);
		frame.setLayout(new FlowLayout(FlowLayout.TRAILING));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		pane.add(select);
		pane.add(repackage);

		pane1.setLayout(new FlowLayout(FlowLayout.LEFT));
		pane1.add(label);

		select.setText("Select Jar");
		select.addActionListener(SelectJar.getFilter());

		repackage.setText("Repackage");
		repackage.addActionListener(new Repackager());

		label.setText("Select your mcpatcher jar");
		
		frame.pack();
		frame.setLocation(screen.width / 2 - ImageObserver.WIDTH,
				screen.height / 3);
		

	}

	public static JFrame getWindow() {
		return frame;
	}

	public static void setFile(File file) {
		System.out.println("Selecting " + file.getName());
		label.setText("Selected Jar: " + file.getName());
	}

	public static void resize() {
		frame.pack();
	}

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	public static void newDialog(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}
}
