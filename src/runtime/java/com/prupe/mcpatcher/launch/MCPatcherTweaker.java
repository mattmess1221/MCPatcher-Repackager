package com.prupe.mcpatcher.launch;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import com.prupe.mcpatcher.asm.MCPAdvancedTransformer;
import com.prupe.mcpatcher.asm.MCPBasicTransformer;

public class MCPatcherTweaker implements ITweaker {
	
	private List<String> args = new ArrayList<String>();

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
		this.args.addAll(args);
		this.args.add("--gameDir");   this.args.add(gameDir.getPath());
		this.args.add("--assetsDir"); this.args.add(assetsDir.getPath());
		this.args.add("--version");	  this.args.add(profile);
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		InputStream stream = classLoader.getResourceAsStream("mcpatcher_data.pack");
		if(stream == null)
			classLoader.registerTransformer(MCPBasicTransformer.class.getName());
		else
			classLoader.registerTransformer(MCPAdvancedTransformer.class.getName());	
	}

	@Override
	public String getLaunchTarget() {
		return "net.minecraft.client.main.Main";
	}

	@Override
	public String[] getLaunchArguments() {
		return new String[0];
	}

}
