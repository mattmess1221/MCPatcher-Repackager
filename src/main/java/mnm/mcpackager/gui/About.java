package mnm.mcpackager.gui;

import mnm.mcpackager.Constants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class About extends JDialog {

	public static void openHelp(JFrame frame) {
		try {
			About dialog = new About(frame);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private About(final JFrame parent) {
		super(parent);
		setType(Type.POPUP);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("About");
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				parent.setEnabled(true);
				parent.requestFocus();
			}

			@Override
			public void windowOpened(WindowEvent e) {
				parent.setEnabled(false);
			}
		});
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(dim.width/3, dim.height/3, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{434, 0};
		gridBagLayout.rowHeights = new int[]{229, 33, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		JComponent tabAbout = new JPanel();
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
			gbc_tabbedPane.fill = GridBagConstraints.BOTH;
			gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
			gbc_tabbedPane.gridx = 0;
			gbc_tabbedPane.gridy = 0;
			getContentPane().add(tabbedPane, gbc_tabbedPane);
			tabbedPane.addTab("About", null, tabAbout, null);
			tabAbout.setBorder(new EmptyBorder(5, 5, 5, 5));
			{
				JPanel tabUsage = new JPanel();
				tabbedPane.addTab("Usage", null, tabUsage, null);
				tabUsage.setLayout(new BorderLayout(0, 0));
				{
					JLabel lblNewLabel = new JLabel("Usage");
					lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
					lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
					tabUsage.add(lblNewLabel, BorderLayout.NORTH);
				}
				{
					JLabel lblSda = new JLabel("<html>\r\n\t<ol>\r\n\t\t<li>Run MCPatcher on the desired version of Minecraft.</li>\r\n\t\t<li>In the repackager and click the <em>Select Jar</em> button and select your patched jar.</li>\r\n\t\t<li>Click the <em>Repackage</em> button and wait for the program to finish.</li><br/> If any issues arrise, a dialog will pop up with a short description of what went wrong.\r\n\t</ol>\r\n</html>");
					lblSda.setVerticalAlignment(SwingConstants.TOP);
					lblSda.setFont(new Font("Tahoma", Font.PLAIN, 12));
					tabUsage.add(lblSda, BorderLayout.CENTER);
				}
			}
			{
				JPanel tabCredits = new JPanel();
				tabbedPane.addTab("Credits", null, tabCredits, null);
				tabCredits.setLayout(new BorderLayout(0, 0));
				{
					JLabel lblLibrariesTitle = new JLabel("Credits");
					lblLibrariesTitle.setHorizontalAlignment(SwingConstants.CENTER);
					lblLibrariesTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
					tabCredits.add(lblLibrariesTitle, BorderLayout.NORTH);
				}
				{
					JPanel panelLibs = new JPanel();
					tabCredits.add(panelLibs, BorderLayout.CENTER);
					panelLibs.setLayout(null);
					
					{
						JLabel lblUsedLibraries = new JLabel("Used Libraries");
						lblUsedLibraries.setBounds(36, 11, 80, 14);
						lblUsedLibraries.setFont(new Font("Tahoma", Font.BOLD, 11));
						panelLibs.add(lblUsedLibraries);
					}
					
					{
						JPanel panelCommonsIo = new JPanel();
						panelCommonsIo.setBounds(61, 30, 256, 24);
						panelLibs.add(panelCommonsIo);
						panelCommonsIo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
						JLabel lblCommonsIo = new JLabel("Apache Commons IO");
						panelCommonsIo.add(lblCommonsIo);
						lblCommonsIo.addMouseListener(new LinkAdapter(lblCommonsIo, Constants.URI_COMMONS_IO));
						
						JLabel lblCommonsIoDesc = new JLabel("(For reading and writing files)");
						panelCommonsIo.add(lblCommonsIoDesc);
					}
					
				
					{
						JPanel panelGson = new JPanel();
						panelGson.setBounds(61, 55, 184, 24);
						panelLibs.add(panelGson);
						panelGson.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
						JLabel lblGson = new JLabel("Google Gson");
						panelGson.add(lblGson);
						lblGson.addMouseListener(new LinkAdapter(lblGson, Constants.URI_GSON));
						
						JLabel lblGsonDesc = new JLabel("(Reading version.json)");
						panelGson.add(lblGsonDesc);
					}
				
				
					{
						JPanel panelLaunch = new JPanel();
						panelLaunch.setBounds(61, 80, 191, 24);
						panelLibs.add(panelLaunch);
						panelLaunch.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
						JLabel lblLaunch = new JLabel("Launch Wrapper");
						panelLaunch.add(lblLaunch);
						lblLaunch.addMouseListener(new LinkAdapter(lblLaunch, Constants.URI_LAUNCH_WRAPPER));
						
						JLabel lblLaunchDesc = new JLabel("(Loading MCPatcher)");
						panelLaunch.add(lblLaunchDesc);
					}
					
					JLabel lblSpecialThanks = new JLabel("Special Thanks to:");
					lblSpecialThanks.setFont(new Font("Tahoma", Font.BOLD, 11));
					lblSpecialThanks.setBounds(36, 115, 102, 14);
					panelLibs.add(lblSpecialThanks);
					
					JPanel panel = new JPanel();
					panel.setBounds(61, 129, 104, 24);
					panelLibs.add(panel);
					panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
					
					JLabel lblMcpatcher = new JLabel("MCPatcher");
					panel.add(lblMcpatcher);
					lblMcpatcher.addMouseListener(new LinkAdapter(lblMcpatcher, Constants.URI_MCPATCHER));
					
					JLabel lblByKahr = new JLabel("by Kahr");
					panel.add(lblByKahr);
				
				}
			}
		}
		tabAbout.setLayout(null);
		{
			JLabel lblTitle = new JLabel("MCPatcher Repackager v" + Constants.VERSION);
			lblTitle.setLabelFor(tabAbout);
			lblTitle.setBounds(100, 11, 229, 20);
			lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
			tabAbout.add(lblTitle);
		}
		{
			JLabel lblAuthor = new JLabel("(c) Matthew Messinger (killjoy1221)");
			lblAuthor.setBounds(122, 42, 185, 14);
			tabAbout.add(lblAuthor);
		}
		{
			JLabel lblDesc = new JLabel();
			lblDesc.setVerticalAlignment(SwingConstants.TOP);
			lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblDesc.setText("<html>MCPatcher Repackager is a tool for repackaging an MCPatcher patched<br/>Minecraft into a separate jar file.<br/><br/>This is useful when using a mod loader, such as LiteLoader or FML,<br/>that supports loading Tweaker mods. In the case of FML, it won't<br/>complain if you don't add the right JVM arguments.<HTML>");
			lblDesc.setBackground(SystemColor.menu);
			lblDesc.setBounds(10, 67, 413, 90);
			tabAbout.add(lblDesc);
		}
		{
			JPanel panelSource = new JPanel();
			panelSource.setBounds(10, 168, 198, 24);
			tabAbout.add(panelSource);
			panelSource.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

			JLabel lblSource = new JLabel("The source can be found on my");
			panelSource.add(lblSource);

			JLabel lblLink = new JLabel("GitHub");
			panelSource.add(lblLink);
			lblLink.addMouseListener(new LinkAdapter(lblLink, Constants.URI_SOURCE));
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints btnOK = new GridBagConstraints();
			btnOK.anchor = GridBagConstraints.NORTH;
			btnOK.fill = GridBagConstraints.HORIZONTAL;
			btnOK.gridx = 0;
			btnOK.gridy = 1;
			getContentPane().add(buttonPane, btnOK);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						About.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	public class LinkAdapter extends MouseAdapter {

		private final URI uri;
		private String original;
		
		private LinkAdapter(JComponent comp, URI uri){
			comp.setToolTipText(uri.toString());
			comp.setForeground(Color.BLUE);
			comp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.uri = uri;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			original = ((JLabel) e.getComponent()).getText();
			((JLabel) e.getComponent()).setText("<HTML><U>" + original
					+ "</U></HTML>");
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(original != null)
				((JLabel) e.getComponent()).setText(original);
		}
	}
}
