package org.mitre.base;

import org.nlogo.headless.HeadlessWorkspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NetLogoController {

	// logger
	private static final Logger log = LoggerFactory.getLogger(NetLogoController.class);


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
			log.info("{}", workspace.report("burned-trees"));
			workspace.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
