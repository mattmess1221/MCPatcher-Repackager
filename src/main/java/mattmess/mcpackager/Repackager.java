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
package mattmess.mcpackager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import mattmess.mcpackager.gui.SelectJar;
import mattmess.mcpackager.gui.Window;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
/**
 * This class does the magic.
 * @author Matthew Messinger
 */
public class Repackager implements ActionListener {

	private String minecraftVersion;
	private String patcherVersion;
	private String[] modifiedClasses;
	private String[] addedClasses;
	private File newJar;
	private boolean forge;
	private boolean fml;

	@Override
	public void actionPerformed(ActionEvent e) {
		JarFile jar = null;
		try {
			if (SelectJar.jarFile == null) {
				throw new Exception("No jar selected");
			}

			detectForge(SelectJar.jarFile);
			jar = new JarFile(SelectJar.jarFile);
			getChangedClasses(jar);
			packageLibrary(jar, newJar);			
		} catch (Exception e1) {
			e1.printStackTrace();
			Window.newDialog(String.format("Error: %s.\nSee console for details.", e1.getMessage()));
			return;
		}
		System.out.println("Created new Jar at " + newJar.getPath());
		Window.newDialog("Created new Jar at " + newJar.getPath());
		System.exit(0);
	}

	private void getChangedClasses(JarFile file) throws IOException {
		Enumeration<JarEntry> files = file.entries();
		final String prop = "mcpatcher.properties";
		JarEntry propFile = null;
		InputStream properties;

		boolean fileFound = false;
		while (files.hasMoreElements()) {
			JarEntry je = files.nextElement();
			if (je.getName().equalsIgnoreCase(prop)) {
				fileFound = true;
				propFile = je;
			}
		}
		
		if (!fileFound)
			throw new FileNotFoundException("Can't find mcpatcher.properties");
		
		// JOptionPane.showMessageDialog(Window.getWindow(),
		// "Found mcpatcher.properties");
		System.out.println("Found mcpatcher.properties");
		properties = file.getInputStream(propFile);
		InputStreamReader bri = new InputStreamReader(properties);
		BufferedReader br = new BufferedReader(bri);
		
		Properties property = new Properties();
		property.load(br);
		
		minecraftVersion = property.getProperty("minecraftVersion");
		patcherVersion = property.getProperty("patcherVersion");
		addedClasses = property.getProperty("addedClasses").split(" ");
		modifiedClasses = property.getProperty("modifiedClasses").split(" ");
		newJar = new File(new File(new File(Utils.mcDir, "mod"), minecraftVersion),
				String.format("mcpatcher-%s-mc%s%s.jar", patcherVersion, minecraftVersion,
						(forge ? "-forge" : fml ? "-fml" : "")));
	}

	private void detectForge(File jar) {
		File json = new File(jar.getParentFile(), jar.getName().replace("jar",
				"json"));
		Gson gson = new Gson();
		;
		try {
			JsonObject obj = gson.fromJson(new InputStreamReader(new FileInputStream(json)), JsonObject.class);
			JsonArray array = obj.getAsJsonArray("libraries");
			for (JsonElement lib : array) {
				String l = lib.getAsJsonObject().get("name").getAsString();
				if (l.contains("forge"))
					forge = true;
				else if (l.contains("fml"))
					fml = true;
				if (fml || forge)
					return;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void packageLibrary(JarFile oldFile, File newFile)
			throws IOException {
		Enumeration<JarEntry> entries = oldFile.entries();
		String[] allClasses = ArrayUtils.addAll(modifiedClasses, addedClasses);
		for (int i = 0; i < allClasses.length; i++) {
			allClasses[i] = allClasses[i].replace(".", "/").concat(".class");
		}
		List<String> classes = Arrays.asList(allClasses);
		newFile.getParentFile().mkdirs();
		newFile.createNewFile();
		
		JarOutputStream out = new JarOutputStream(new FileOutputStream(newFile), generateManifest());

		System.out.println("Creating jar");
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			JarEntry newEntry = new JarEntry(entry.getName());
			if (classes.contains(entry.getName())
					|| entry.getName().contains("assets/minecraft/mcpatcher/")
					|| entry.getName().equals("mcpatcher.properties")) {
				out.putNextEntry(newEntry);

				BufferedInputStream in = new BufferedInputStream(
						oldFile.getInputStream(entry));

				while (in.available() > 0) {
					out.write(in.read());
				}
				in.close();
			}
			out.closeEntry();
		}
		oldFile.close();

		JarInputStream zip = new JarInputStream(ClassLoader.getSystemClassLoader().getResourceAsStream("tweak.jar"));

		while (true) {
			JarEntry entry = zip.getNextJarEntry();
			if(entry == null)
				break;
			out.putNextEntry(new ZipEntry(entry));
			
			while (zip.available() > 0) {
				int bytes = zip.read();
				if(bytes != -1)
					out.write(bytes);
			}
			out.closeEntry();

		}
		zip.close();
		
		out.finish();
		out.close();
	}
	
	private Manifest generateManifest(){
		Manifest manifest = new Manifest();
		Attributes attributes = manifest.getMainAttributes();
		attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
		attributes.putValue("TweakOrder", "-1000");
		attributes.putValue("TweakName", "MCPatcher");
		attributes.putValue("TweakClass", "com.prupe.mcpatcher.launch.MCPatcherTweaker");
		attributes.putValue("TweakVersion", patcherVersion);
		attributes.putValue("TweakAuthor", "Kahr");
		
		return manifest;
	}
}
