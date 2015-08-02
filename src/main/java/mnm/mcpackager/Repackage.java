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

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.commons.io.IOUtils;

import mnm.mcpackager.gui.Window;

/**
 * <h1>MCPatcher Repackager</h1>
 * 
 * <p>
 * A small Java program to extract classes that MCPatcher adds and modifies in
 * Minecraft and package them in an easy to install library.
 * </p>
 * 
 * @author Matthew Messinger
 * 
 */
public class Repackage extends Thread {

    public static int errors = 0;

    private static Window window;
    private static List<File> detectedJars;

    public static void main(String[] args) throws InterruptedException {
        window = new Window();
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                window.getFrame().setVisible(true);
            }
        });
        detectPatchedJars();
        window.getFoundJars().removeAll();
        window.getFoundJars().addItem("");
        for (File f : detectedJars) {
            window.getFoundJars().addItem(f.getParentFile().getName());
        }
        if (detectedJars.isEmpty()) {
            window.getFoundJars().addItem("No jars found.");
        }
    }

    private static void detectPatchedJars() {
        detectedJars = new ArrayList<File>();
        File vers = new File(Constants.MINECRAFT_DIR, "versions");
        // go through the directories
        for (File f : vers.listFiles()) {
            File file = new File(f, f.getName() + ".jar");
            if (file.isFile()) {
                JarFile jar = null;
                try {
                    jar = new JarFile(file);
                    ZipEntry ze = jar.getEntry("mcpatcher.properties");
                    if (ze == null) {
                        continue;
                    }
                    detectedJars.add(file);
                    System.out.println("Found valid patched jar in " + f.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(jar);
                }
            }
        }
    }

    public static Window getWindow() {
        return window;
    }

    public void run() {
        errors = 0;
        try {
            if (window.getJar() == null) {
                throw new IllegalArgumentException("No jar selected");
            }
            PatchedJar patchedJar = new PatchedJar(window.getJar());
            PackageType.LIBRARY.repackage(patchedJar);
            System.out.println("Created new Jar at " + patchedJar.getNewJar().getPath()
                    + (errors > 0 ? "\nFinished with " + errors + " error" + (errors > 1 ? "s" : "") : "") + ".");
            window.newDialog("Created new Jar at " + patchedJar.getNewJar().getPath() + (errors > 0
                    ? "\nFinished with " + errors + " error" + (errors > 1 ? "s" : "") + ". (see console)" : ""));
        } catch (Exception e1) {
            e1.printStackTrace();
            window.newDialog(String.format("Error: %s" + (errors > 1 ? " and " + (errors - 1) + " others" : "")
                    + ".\nSee console for details.", e1.getMessage()));
            return;
        }
        System.exit(0);
    }

}
