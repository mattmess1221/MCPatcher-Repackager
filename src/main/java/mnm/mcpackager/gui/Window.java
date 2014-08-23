package mnm.mcpackager.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mnm.mcpackager.PackageTypes;
import mnm.mcpackager.Constants;
import mnm.mcpackager.Repackage;

public class Window {
	private JFrame frame = new JFrame();
	private Box box = Box.createVerticalBox();
	private JLabel label = new JLabel();
	private JButton select = new JButton();
	private JButton repackage = new JButton();
	private JComboBox<PackageTypes> dropdown = new JComboBox<PackageTypes>();

	public Window() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setTitle("MCPatcher Repackager v" + Constants.VERSION);
		JPanel pane1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pane2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		frame.add(box);
		box.add(pane1);
		box.add(pane2);
		frame.setLayout(new FlowLayout(FlowLayout.TRAILING));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pane1.add(label);
		pane1.add(select);
		pane1.add(repackage);

		pane2.add(new JLabel("Package Method"));
		pane2.add(dropdown);

		select.setText("Select Jar");
		select.addActionListener(Select.getFilter());

		repackage.setText("Repackage");
		repackage.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new Repackage().action();
			}
		});

		label.setText("Select your mcpatcher jar  ");
		
		dropdown.addItem(PackageTypes.LIBRARY);
		dropdown.addItem(PackageTypes.TRANSFORMER);
		dropdown.setLocation(20, 20);
		
		frame.pack();
		frame.setLocation(screen.width / 2 - ImageObserver.WIDTH, screen.height / 3);
		

	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFile(File file) {
		System.out.println("Selecting " + file.getName());
		label.setText("Selected Jar: " + file.getName() + "  ");
	}

	public void resize() {
		frame.pack();
	}

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	public void newDialog(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}
	
	public PackageTypes getPackageMethod(){
		return (PackageTypes) this.dropdown.getSelectedItem();
	}
}
