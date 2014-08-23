package mnm.mcpackager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.ZipEntry;

import org.apache.commons.io.IOUtils;

import com.nothome.delta.Delta;

public class Difference {
	
	private Map<String, byte[]> patches = new HashMap<String, byte[]>();

	private Delta delta = new Delta();
	
	private PatchedJar patchedJar;
	private VanillaJar vanillaJar;
	
	public Difference(PatchedJar patchedJar, VanillaJar vanillaJar) throws IOException{
		this.patchedJar = patchedJar;
		this.vanillaJar = vanillaJar;
		createDiffs();
	}
	

	private void createDiffs(){
		JarFile patched = patchedJar.getOldJar();
		JarFile vanilla = vanillaJar.getJar();
		for(String name : patchedJar.modifiedClasses){
			try{
				String entry = name.replace('.', '/').concat(".class");
				InputStream vanillaClass = vanilla.getInputStream(vanilla.getEntry(entry));
				InputStream patchedClass = patched.getInputStream(patched.getEntry(entry));
				patches.put(name.replace('.', '/').concat(".binpatch"), genDiff(vanillaClass, patchedClass, name));
			}catch(IOException io){
				io.printStackTrace();
				Repackage.errors++;
			}
		}
	}
	
	private byte[] genDiff(InputStream vanilla, InputStream patched, String name) throws IOException{
		byte[] outold = readClass(vanilla);
		byte[] outnew = readClass(patched);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		byte[] diff = delta.compute(outold, outnew);
		out.writeUTF(name);        // patch name
		out.writeInt(diff.length); // patch length
		out.write(diff);           // patch
		return baos.toByteArray();
	}
	
	public byte[] createPatchJar(Map<String, byte[]> patches) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JarOutputStream jar = new JarOutputStream(out);
		for(Entry<String, byte[]> entry : patches.entrySet()){
			jar.putNextEntry(new ZipEntry(entry.getKey()));
			jar.write(entry.getValue());
		}
		jar.close();
		return out.toByteArray();
	}
	
	private byte[] readClass(InputStream clas) throws IOException{
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IOUtils.copy(clas,  out);
			return out.toByteArray();
		}finally{
			IOUtils.closeQuietly(clas);
		}
	}
	
	public OutputStream pack(OutputStream out) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JarOutputStream jos = new JarOutputStream(baos);
		for(Entry<String, byte[]> entry : this.patches.entrySet()){
			try{
				ZipEntry ze = new ZipEntry(entry.getKey());
				jos.putNextEntry(ze);
				jos.write(entry.getValue());
				jos.closeEntry();
			}catch(IOException io){
				io.printStackTrace();
				Repackage.errors++;
			}
		}
		jos.close();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		baos.close();
		JarInputStream in = new JarInputStream(bais);
		Pack200.newPacker().pack(in, out);
		return out;
	}
}
