package org.mitre.base;

import org.nlogo.headless.HeadlessWorkspace;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NetLogoController {

	//
	// from: https://github.com/NetLogo/NetLogo/wiki/Controlling-API
	//
	public static void main(String [] argv) {
		HeadlessWorkspace workspace = HeadlessWorkspace.newInstance();
		try {
			workspace.open("models/Sample Models/Earth Science/Fire.nlogo", false);
			workspace.command("set density 62");
			workspace.command("random-seed 0");
			workspace.command("setup");
			workspace.command("repeat 50 [ go ]");
			System.out.println(workspace.report("burned-trees"));
			workspace.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
