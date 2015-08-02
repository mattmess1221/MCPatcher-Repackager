package mnm.mcpackager.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import mnm.mcpackager.Constants;
import mnm.mcpackager.Repackage;

public class Window {

    private JFrame frmRepackager;
    private JLabel lblSelectedJar;
    private File jarFile;
    private Choice foundJars;

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

    private void initialize() {
        frmRepackager = new JFrame();
        frmRepackager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmRepackager.setTitle("MCPatcher Repackager v" + Constants.VERSION);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frmRepackager.setBounds(dim.width / 3, dim.height / 3, 368, 131);
        frmRepackager.setResizable(false);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 43, 72, 89, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 17, 17, 23, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        frmRepackager.getContentPane().setLayout(gridBagLayout);

        JButton btnRepackage = new JButton("Go");
        btnRepackage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new Repackage().start();
            }
        });

        JButton btnSelectJar = new JButton("Other...");
        btnSelectJar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectJar();
            }
        });

        foundJars = new Choice();
        foundJars.add("Loading...");
        GridBagConstraints gbc_foundJars = new GridBagConstraints();
        gbc_foundJars.insets = new Insets(0, 0, 5, 5);
        gbc_foundJars.gridx = 0;
        gbc_foundJars.gridy = 1;
        foundJars.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                setFile(new File(Constants.MINECRAFT_DIR,
                        String.format("versions/%s/%s.jar", e.getItem(), e.getItem())));
            }
        });
        frmRepackager.getContentPane().add(foundJars, gbc_foundJars);

        JLabel lblSelectedJarText = new JLabel("Selected Jar:");
        GridBagConstraints gbc_lblSelectedJarText = new GridBagConstraints();
        gbc_lblSelectedJarText.anchor = GridBagConstraints.EAST;
        gbc_lblSelectedJarText.fill = GridBagConstraints.VERTICAL;
        gbc_lblSelectedJarText.insets = new Insets(0, 0, 5, 5);
        gbc_lblSelectedJarText.gridx = 1;
        gbc_lblSelectedJarText.gridy = 1;
        frmRepackager.getContentPane().add(lblSelectedJarText, gbc_lblSelectedJarText);

        lblSelectedJar = new JLabel();
        GridBagConstraints gbc_lblSelectedJar = new GridBagConstraints();
        gbc_lblSelectedJar.fill = GridBagConstraints.BOTH;
        gbc_lblSelectedJar.insets = new Insets(0, 0, 5, 0);
        gbc_lblSelectedJar.gridwidth = 3;
        gbc_lblSelectedJar.gridx = 2;
        gbc_lblSelectedJar.gridy = 1;
        frmRepackager.getContentPane().add(lblSelectedJar, gbc_lblSelectedJar);

        JButton btnAbout = new JButton("About");
        btnAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                About.openHelp(Window.this.frmRepackager);
            }
        });
        GridBagConstraints gbc_btnAbout = new GridBagConstraints();
        gbc_btnAbout.anchor = GridBagConstraints.NORTH;
        gbc_btnAbout.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnAbout.insets = new Insets(0, 0, 0, 5);
        gbc_btnAbout.gridx = 1;
        gbc_btnAbout.gridy = 3;
        frmRepackager.getContentPane().add(btnAbout, gbc_btnAbout);
        GridBagConstraints gbc_btnSelectJar = new GridBagConstraints();
        gbc_btnSelectJar.anchor = GridBagConstraints.NORTH;
        gbc_btnSelectJar.insets = new Insets(0, 0, 0, 5);
        gbc_btnSelectJar.gridx = 2;
        gbc_btnSelectJar.gridy = 3;
        frmRepackager.getContentPane().add(btnSelectJar, gbc_btnSelectJar);
        btnRepackage.setIcon(null);
        GridBagConstraints gbc_btnRepackage = new GridBagConstraints();
        gbc_btnRepackage.insets = new Insets(0, 0, 0, 5);
        gbc_btnRepackage.anchor = GridBagConstraints.NORTH;
        gbc_btnRepackage.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnRepackage.gridx = 3;
        gbc_btnRepackage.gridy = 3;
        frmRepackager.getContentPane().add(btnRepackage, gbc_btnRepackage);
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
        File jarFile = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = dlgSelectJar.getSelectedFile();
            if (dlgSelectJar.accept(file)) {
                jarFile = file;
                System.out.println("Opening: " + file.getName() + ".");
            }
        } else {
            System.out.println("Open command cancelled by user.");
        }

        try {
            setFile(jarFile);
        } catch (NullPointerException exp) {
        }
    }

    public void setFile(File file) {
        System.out.println("Selecting " + file.getName());
        this.jarFile = file;
        lblSelectedJar.setText(file.getName() + "  ");
    }

    public File getJar() {
        return this.jarFile;
    }

    public Choice getFoundJars() {
        return foundJars;
    }

    public void newDialog(String message) {
        JOptionPane.showMessageDialog(frmRepackager, message);
    }

    public JFrame getFrame() {
        return frmRepackager;
    }
}
