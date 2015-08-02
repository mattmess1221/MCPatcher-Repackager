package mnm.mcpackager;

import static mnm.mcpackager.Constants.MINECRAFT_DIR;
import static mnm.mcpackager.Constants.PROPERTIES;
import static mnm.mcpackager.Constants.PROP_ADDED_CLASSES;
import static mnm.mcpackager.Constants.PROP_MINECRAFT_VERSION;
import static mnm.mcpackager.Constants.PROP_MODIFIED_CLASSES;
import static mnm.mcpackager.Constants.PROP_PATCHER_VERSION;
import static mnm.mcpackager.Constants.TWEAK_AUTHOR;
import static mnm.mcpackager.Constants.TWEAK_CLASS;
import static mnm.mcpackager.Constants.TWEAK_NAME;
import static mnm.mcpackager.Constants.TWEAK_ORDER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PatchedJar implements VersionJar {

    private File file;
    private JarFile jarFile;
    private VanillaJar vanillaJar;

    private String patcherVersion;
    public String[] modifiedClasses;
    public String[] addedClasses;

    private boolean fml;
    private boolean forge;

    public PatchedJar(File jar) throws IOException {
        this.file = jar;
        this.jarFile = new JarFile(jar);
        setup();
        detectForge();
    }

    private void detectForge() {
        File json = new File(file.getParentFile(), file.getName().replace(".jar", ".json"));
        Gson gson = new Gson();
        try {
            JsonObject obj = gson.fromJson(new InputStreamReader(new FileInputStream(json)), JsonObject.class);
            JsonArray array = obj.getAsJsonArray("libraries");
            for (JsonElement lib : array) {
                String l = lib.getAsJsonObject().get("name").getAsString();
                forge = l.contains("forge");
                fml = l.contains("fml");
                if (fml || forge)
                    return;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setup() throws IOException {
        Enumeration<JarEntry> files = jarFile.entries();
        JarEntry propFile = null;
        InputStream properties;

        boolean fileFound = false;
        while (files.hasMoreElements()) {
            JarEntry je = files.nextElement();
            if (je.getName().equalsIgnoreCase(PROPERTIES)) {
                fileFound = true;
                propFile = je;
            }
        }

        if (!fileFound)
            throw new IllegalArgumentException("Can't find mcpatcher.properties");

        System.out.println("Found mcpatcher.properties");
        properties = jarFile.getInputStream(propFile);
        InputStreamReader bri = new InputStreamReader(properties);
        BufferedReader br = new BufferedReader(bri);

        Properties property = new Properties();
        property.load(br);

        vanillaJar = new VanillaJar(property.getProperty(PROP_MINECRAFT_VERSION));
        patcherVersion = property.getProperty(PROP_PATCHER_VERSION);
        addedClasses = property.getProperty(PROP_ADDED_CLASSES).split(" ");
        modifiedClasses = property.getProperty(PROP_MODIFIED_CLASSES).split(" ");
    }

    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        attributes.putValue("TweakOrder", Integer.toString(TWEAK_ORDER));
        attributes.putValue("TweakName", TWEAK_NAME);
        attributes.putValue("TweakClass", TWEAK_CLASS);
        attributes.putValue("TweakVersion", patcherVersion);
        attributes.putValue("TweakAuthor", TWEAK_AUTHOR);

        return manifest;
    }

    public File getNewJar() {
        return new File(new File(new File(MINECRAFT_DIR, "mods"), vanillaJar.getVersion()),
                String.format("mcpatcher-%s-mc%s%s.jar", patcherVersion, vanillaJar.getVersion(),
                        (forge ? "-forge" : fml ? "-fml" : "")));
    }

    public JarFile getJar() {
        return this.jarFile;
    }

    public File getFile() {
        return this.file;
    }

    public VanillaJar getVanilla() {
        return this.vanillaJar;
    }

    public String getVersion() {
        return this.vanillaJar.getVersion();
    }

    public boolean isVanilla() {
        return false;
    }

}
