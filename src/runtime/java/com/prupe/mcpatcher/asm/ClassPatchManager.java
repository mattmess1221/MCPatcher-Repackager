package com.prupe.mcpatcher.asm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;

import com.nothome.delta.GDiffPatcher;

public class ClassPatchManager {
	
	private GDiffPatcher patcher = new GDiffPatcher();
	
	private Map<String, ClassPatch> patches = new HashMap<String, ClassPatch>();
	private Map<String, byte[]> patchedClasses = new HashMap<String, byte[]>();

	public ClassPatchManager(){
		try {
			setup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setup() throws IOException{
		JarInputStream jis;
		try{
			InputStream binPatches = getClass().getResourceAsStream("/mcpatcher_data.pack");
			if(binPatches == null){
				System.out.println("[MCPatcher] Patches missing.");
				return;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JarOutputStream jos = new JarOutputStream(baos);
			Pack200.newUnpacker().unpack(binPatches, jos);
			jis = new JarInputStream(new ByteArrayInputStream(baos.toByteArray()));
		}catch(IOException e){
			throw e;
		}
		
		do{
			JarEntry entry = jis.getNextJarEntry();
			if(entry == null)
				break;
			ClassPatch cp = readPatch(entry, jis);
			if(cp != null){
				patches.put(cp.name, cp);
			}else{
				System.out.println("Couldn't load patch '" + entry.getName() + "'");
			}
			
		}while(true);
		patchedClasses.clear();
	}
	
	public byte[] patch(byte[] bytes, String name){
		if(patches == null || !patches.containsKey(name))
			return bytes;
		if(patchedClasses.containsKey(name)){
			System.out.println("Patching class '" + name + "'");
			return patchedClasses.get(name);
		}
		ClassPatch patch = patches.get(name);
		try{
			bytes = patcher.patch(bytes, patch.patch);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		patchedClasses.put(name, bytes);
		return bytes;
	}
	
	private ClassPatch readPatch(JarEntry entry, JarInputStream jis){
		ObjectInputStream input;
		try{
			input = new ObjectInputStream(jis);
			String name = input.readUTF();
			int length = input.readInt();
			byte[] bytes = new byte[length];
			input.read(bytes);
			return new ClassPatch(name, bytes);
		}catch(IOException e){
			return null;
		}
	}
}
