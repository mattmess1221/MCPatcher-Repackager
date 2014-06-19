# MCPatcher Repackager
----
This is a tool designed to take the files that MCPatcher creates and modifies and repackages them into their own jar archive.  It will also add a tweak and transformer class so it can be loaded by LiteLoader and Forge Mod Loader without issue.

## Usage
1. Before you begin, start by downloading and running MCPatcher to patch your desired Minecraft version.  If you're using FML or Forge, patch with them.  LiteLoader does not need to be patched with it.

    **Note**: you must use correct mod (FML *OR* MinecraftForge).  Their files are not interchangeable.

2. Now launch the Java application.  It requires Java 7 to run properly.

3. When the window greets you, click the `Select Jar` button.  You will be shown a file selection screen with the location at your Minecraft versions folder.

4. Select the version that MCPatcher created.  Selecting anything else will give you an error.

5. Finish by clicking `Repackage`.  The magic will happen and your newly created jar will appear in .minecraft/mods/${mcversion}/.  If you had forge or fml installed, the file will be duly labelled.

6. The program will then exit.

If any issues arise, a popup will appear and give a short description of what went wrong.  A stacktrace will also appear in the console.