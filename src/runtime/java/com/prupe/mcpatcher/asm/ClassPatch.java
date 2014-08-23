package com.prupe.mcpatcher.asm;

public class ClassPatch {
	
	public String name;
	public byte[] patch;

	public ClassPatch(String name, byte[] patch){
		this.name = name;
		this.patch = patch;
	}
	
}
