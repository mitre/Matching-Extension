package org.mitre.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.nlogo.api.LogoException;
import org.nlogo.core.CompilerException;
import org.nlogo.headless.HeadlessWorkspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@Component
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
	@PostMapping("/netlogo/uploadAgent")
	public ResponseEntity<String> uploadAgent(@RequestParam("agent") String agent,
			                 @RequestParam("file") MultipartFile file) throws IOException {
		// list to save code into
		ArrayList<String> nlcode = Lists.newArrayList();

		// capture the file and turn it into a reader
		try (BufferedReader rd = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			// now use readLine to copy into ArrayList
			String next = rd.readLine();
			while (next != null) {
				nlcode.add(next);
				next = rd.readLine();
			}
		}

		// add agent + netlogo program pair to global routines
		routines.put(agent, nlcode);

		log.debug("Put agent {} with Netlogo program code {}.", agent,
				         file.getOriginalFilename());

		// return successful
		return new ResponseEntity<>("Agent file upload success!", HttpStatus.OK);
	}


	/**
	 * @param workspace file to initialize
	 *
	 * @return response whether parsed successfully
	 * @throws IOException
	 */
	@PostMapping("/netlogo/initWorkspace")
	public ResponseEntity<String> initWorkspace(@RequestParam("file") MultipartFile file) throws IOException {
		// copy to file system
		Path newFile = Files.createTempFile("ws", ".nlogo");
		file.transferTo(newFile);

		// actually open workspace, factored out for test
		openWorkspace(newFile.toFile().toString());

		// return successful
		return new ResponseEntity<>("Workspace file initialization success!", HttpStatus.OK);
	}

	/**
	 * @return the routines
	 */
	public Map<String, ArrayList<String>> getRoutines() {
		return routines;
	}

	/**
	 * @param string name of path
	 *
	 * @throws IOException
	 * @throws LogoException
	 * @throws CompilerException
	 *
	 */
	public void openWorkspace(String path) throws CompilerException, LogoException, IOException {
		this.ws.open(path, true);
		log.debug("Initialized workspace with Netlogo file {}", path);
	}

	/**
	 * @return the workspace
	 */
	public HeadlessWorkspace getWs() {
		return ws;
	}

	/**
	 * clears routines
	 */
	public void clearRoutines() {
		routines = Maps.newHashMap();
	}
}
