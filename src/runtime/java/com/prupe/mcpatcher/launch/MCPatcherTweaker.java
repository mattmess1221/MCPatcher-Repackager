package com.prupe.mcpatcher.launch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class MCPatcherTweaker implements ITweaker {
	
	private List<String> arguments = new ArrayList<String>();

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
		this.arguments.addAll(args);
		this.arguments.add("--gameDir");   this.arguments.add(gameDir.getPath());
		this.arguments.add("--assetsDir"); this.arguments.add(assetsDir.getPath());
		this.arguments.add("--version");	  this.arguments.add(profile);
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		classLoader.registerTransformer(MCPTransformer.class.getName());
	}

	@Override
	public String getLaunchTarget() {
		return "net.minecraft.client.main.Main";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] getLaunchArguments() {
		List<String> list = new ArrayList<String>();
		List<String> args = (List<String>) Launch.blackboard.get("ArgumentList");
		if(args == null)
			args = new ArrayList<String>();
		for(int i = 0; i < this.arguments.size(); i+=2){
			String s = this.arguments.get(i);
			if(s.startsWith("--")){
				if(!args.contains(s)){
					list.add(s);
					list.add(this.arguments.get(i+1));
				}
			}else{
				i--;
			}
		}
		return list.toArray(new String[0]);
	}

}
