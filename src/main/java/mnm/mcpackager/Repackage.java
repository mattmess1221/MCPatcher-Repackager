/**
 * MCPatcher Repackager
 * Copyright (C) 2014 Matthew Messinger
 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package mnm.mcpackager;

import mnm.mcpackager.gui.Select;
import mnm.mcpackager.gui.Window;

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
public class Repackage extends Thread {
	
	public static int errors = 0;
	private PackageTypes method;

	private static Window window = new Window();

	public static void main(String[] args) throws InterruptedException {
		window.setVisible(true);
	}
	
	public static Window getWindow(){
		return window;
	}
	
	public Repackage(){
		super("Repackager");
	}
	
	public void action(){
		errors = 0;
		this.start();
	}
	
	public void run(){
		try {
			if (Select.jarFile == null) {
				throw new IllegalArgumentException("No jar selected");
			}
			this.method = window.getPackageMethod();
			PatchedJar patchedJar = new PatchedJar(Select.jarFile);
			method.repackage(patchedJar);
			System.out.println("Created new Jar at " + patchedJar.getNewJar().getPath() + (errors > 0 ? "\nFinished with " + errors + " error"+(errors>1?"s":"") : "") + ".");
			window.newDialog("Created new Jar at " + patchedJar.getNewJar().getPath() + (errors > 0 ? "\nFinished with " + errors + " error" + (errors>1?"s":"") + ". (see console)" : ""));
		} catch (Exception e1) {
			e1.printStackTrace();
			window.newDialog(String.format("Error: %s" + (errors > 1 ? " and " + (errors - 1) + " others" : "") + ".\nSee console for details.", e1.getMessage()));
			return;
		}
		System.exit(0);
	}

}
