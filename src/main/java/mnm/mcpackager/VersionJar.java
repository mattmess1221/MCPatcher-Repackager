package mnm.mcpackager;

import java.io.File;
import java.util.jar.JarFile;

public interface VersionJar {

    String getVersion();

    File getFile();

    JarFile getJar();

    boolean isVanilla();
}
