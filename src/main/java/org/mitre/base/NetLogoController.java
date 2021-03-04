package org.mitre.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.nlogo.headless.HeadlessWorkspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@RestController
public class NetLogoController {

	// logger
	private static final Logger log = LoggerFactory.getLogger(NetLogoController.class);

	// netlogo workspace to run the programs in
	private HeadlessWorkspace ws;

	// Map of agent IDs to programs they want to execute
	private HashMap<String, ArrayList<String>> routines;


	/**
	 * default constructor
	 */
	public NetLogoController() {
		ws = HeadlessWorkspace.newInstance();
		log.debug("Created NetLogo workspace {}", ws);

		routines = Maps.newHashMap();
	}

	/**
	 * destructor to close netlogo workspace
	 * @throws InterruptedException
	 *
	 * @note finalize is not java-esque, but best option now
	 */
	@Override
	protected void finalize() throws InterruptedException {
		log.debug("Disposing of NetLogo workspace {}", ws);
		ws.dispose();
	}


	/**
	 *
	 * @param agent name to store program in under routines
	 * @param list of program to store in commands
	 *
	 * @return response whether parsed successfully
	 * @throws IOException
	 *
	 */
	@PostMapping("/netlogo/upload")
	public ResponseEntity<String> uploadAgentModel(@RequestParam("agent") String agent,
			                 @RequestParam("file") MultipartFile file) throws IOException {
		// capture the file and turn it into a reader
		InputStreamReader isr = new InputStreamReader(file.getInputStream(), "UTF-8");
		BufferedReader rd = new BufferedReader(isr);

		// now use readLine to copy into ArrayList
		ArrayList<String> nlcode = Lists.newArrayList();
		String next = rd.readLine();
		while (next != null) {
			nlcode.add(next);
			next = rd.readLine();
		}

		// add agent + netlogo program pair to global routines
		routines.put(agent, nlcode);

		// return successful
		return new ResponseEntity<>("File upload success!", HttpStatus.OK);
	}

}
