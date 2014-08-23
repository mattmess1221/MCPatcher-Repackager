package mnm.mcpackager;

import static mnm.mcpackager.Constants.MCPATCHER_ASSETS;
import static mnm.mcpackager.Constants.MCPATCHER_DATA_PACK;
import static mnm.mcpackager.Constants.PROPERTIES;
import static mnm.mcpackager.Constants.RUNTIME;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import org.apache.commons.io.IOUtils;

public abstract class PackageTypes {
	
	/**
	 * Creates a jar to be added to the libraries section in the json.<br/>
	 * Doesn't require the addition of LaunchWrapper or ASM.<br/>
	 * If using Forge or LiteLoader, it can be put in the mods dir.
	 */
	public static final PackageTypes LIBRARY = new PackageLibrary();
	
	/**
	 * Happy forge :)<br/>
	 * Similar to Library, but uses advanced patching and ASM transforming.<br/>
	 * Requires LaunchWrapper and ASM.
	 */
	public static final PackageTypes TRANSFORMER = new PackageTransformer();
	
	private PackageTypes(){
		
	}
	
	protected void addPatcherToJar(JarOutputStream jos, PatchedJar pj) throws IOException{
		String[] toAdd = this.getClassesToAdd(pj);
		JarFile jf = pj.getOldJar();
		for(String add : toAdd){
			try{
				ZipEntry ze = new ZipEntry(add.replace('.', '/').concat(".class"));
				addToJar(jos, jf.getInputStream(ze), ze);
			}catch(IOException e){
				e.printStackTrace();
				Repackage.errors++;
			}
		}
	}
	
	protected void addPatcherAssetsToJar(JarOutputStream jos, PatchedJar patchedJar){
		JarFile jf = patchedJar.getOldJar();
		Enumeration<JarEntry> enumer = jf.entries();
		while(enumer.hasMoreElements()){
			try {
				JarEntry entry = enumer.nextElement();
				if(entry.getName().contains(MCPATCHER_ASSETS) || entry.getName().equals(PROPERTIES)){
					this.addToJar(jos, jf.getInputStream(entry), entry);
				}
			} catch (IOException e) {
				Repackage.errors++;
				e.printStackTrace();	
			}
		}
	}
	
	protected void addExtraToJar(JarOutputStream jos) throws IOException{
		JarInputStream jis = null;
		try{
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(RUNTIME);
			jis = new JarInputStream(is);
			while(true){
				try{
					ZipEntry ze = jis.getNextEntry();
					if(ze == null) break;
					if(ze.getName().endsWith("/")) continue;
					addToJar(jos, jis, ze);
					jis.closeEntry();
				}catch(IOException e){
					e.printStackTrace();
					Repackage.errors++;
				}
			}
		}finally{
			if(jis != null)
				jis.close();
		}
	}
	
	protected void addToJar(JarOutputStream jos, InputStream is, ZipEntry entry) throws IOException{
		this.addToJar(jos, readInputStream(is).toByteArray(), entry);
	}
	
	protected void addToJar(JarOutputStream jos, byte[] bytes, ZipEntry entry) throws IOException{
		System.out.println(String.format("Adding entry '%s'", entry.getName()));
		ZipEntry ze = new ZipEntry(entry);
		jos.putNextEntry(ze);
		jos.write(bytes);
		jos.closeEntry();
	}
	
	protected ByteArrayOutputStream readInputStream(InputStream is) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while(is.available()>0){
			int data = is.read();
			if(data<0) break;
			baos.write(data);
		}
		return baos;
	}
	
	protected JarOutputStream createJar(File file, Manifest manifest) throws FileNotFoundException, IOException{
		System.out.println(String.format("Creating new jar file at '%s'", file.getPath()));
		return new JarOutputStream(new FileOutputStream(file), manifest);
	}
	
	public void repackage(PatchedJar patchedJar) throws IOException {
		JarOutputStream jos = null;
		try{
			jos = this.createJar(patchedJar.getNewJar(), patchedJar.getManifest());
			this.addPatcherToJar(jos, patchedJar);
			this.addPatcherAssetsToJar(jos, patchedJar);
			this.addExtraToJar(jos);
		}finally{
			IOUtils.closeQuietly(jos);
		}
	}
	
	protected abstract String[] getClassesToAdd(PatchedJar patchedJar);
	
	public abstract String toString();
	
	public abstract String getDescription();
	
	private static class PackageLibrary extends PackageTypes {

		@Override
		public String toString() {
			return Constants.DROPDOWN_LIBRARY_NAME;
		}

		@Override
		public String getDescription() {
			return "The basic package type.\n"
					+ " Can be put in the json or loaded by Forge/LiteLoader.\n"
					+ " Doesn't require ASM or LaunchWrapper.";
		}
		
		@Override
		protected String[] getClassesToAdd(PatchedJar patchedJar) {
			List<String> list = new ArrayList<String>();
			list.addAll(Arrays.asList(patchedJar.modifiedClasses));
			list.addAll(Arrays.asList(patchedJar.addedClasses));
			return list.toArray(new String[0]);
		}
	}
	
	private static class PackageTransformer extends PackageTypes {
		
		@Override
		public String toString() {
			return Constants.DROPDOWN_TRANSFORMER_NAME;
		}

		@Override
		public String getDescription() {
			return "Uses advanced ASM to patch the Minecraft\n"
					+ " jar at runtime.  Doesn't actually package\n"
					+ " Mojang classes, making it legal to redistribute.";
		}
		
		@Override
		protected void addPatcherToJar(JarOutputStream jos, PatchedJar patchedJar) throws IOException{
			System.out.println("Generating patches...");
			VanillaJar vanillaJar = patchedJar.getVanilla();
			Difference diff = new Difference(patchedJar, vanillaJar);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			diff.pack(baos);
			this.addToJar(jos, baos.toByteArray(), new ZipEntry(MCPATCHER_DATA_PACK));
			super.addPatcherToJar(jos, patchedJar);
		}
		
		@Override
		protected String[] getClassesToAdd(PatchedJar patchedJar) {
			return patchedJar.addedClasses;
		}
	}
}
