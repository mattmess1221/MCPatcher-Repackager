package com.prupe.mcpatcher.asm;

import net.minecraft.launchwrapper.IClassTransformer;

public class MCPAdvancedTransformer implements IClassTransformer {

	private ClassPatchManager patches = new ClassPatchManager();
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(basicClass == null)
			return null;
		return patches.patch(basicClass, name);
	}

}
