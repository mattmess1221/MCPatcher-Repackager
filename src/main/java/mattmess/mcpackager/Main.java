/**
 * 
 */
package mattmess.mcpackager;

import mattmess.mcpackager.gui.Window;

/**
 * <h1>MCPatcher Repackager</h1>
 * 
 * <p>A small Java program to extract classes that
 * MCPatcher adds and modifies in Minecraft and
 * package them in an easy to install library.</p>
 * 
 * @author Matthew Messinger
 * 
 */
public class Main {
	private static Window window = new Window();

	public static void main(String[] args) throws InterruptedException {
		window.setVisible(true);
	}
}
