package org.mitre.base;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Test;
import org.nlogo.headless.HeadlessWorkspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class NetLogoControllerTest {

	@Autowired
	private static NetLogoController nlc = new NetLogoController();

	@AfterClass
	public static void close() throws InterruptedException {
		nlc.finalize();
	}


	@Test
	public void testConstructor() {
		nlc.clearRoutines();
		assertEquals(Maps.newHashMap(), nlc.getRoutines());
		assertEquals(HeadlessWorkspace.class, nlc.getWs().getClass());
	}

	@Test
	public void testUploadAgent() throws IOException {
		// mock the file
		MultipartFile fl = mock(MultipartFile.class);
		when(fl.getInputStream()).thenReturn(new ByteArrayInputStream("test upload file\n next line".getBytes()));

		// now upload
		nlc.uploadAgent("test-buyer", fl);

		// make the answer
		HashMap<String, ArrayList<String>> eq = new HashMap<>();
		ArrayList<String> prg = Lists.newArrayList();
		prg.add("test upload file");
		prg.add(" next line");
		eq.put("test-buyer", prg);

		assertEquals(eq, nlc.getRoutines());
	}

	@Test
	public void testInitWorkspace() throws IOException {
		// find the empty net logo file
		String ws = this.getClass().getClassLoader().getResource("empty-test.nlogo").getPath();

		// now upload
		nlc.openWorkspace(ws);

		assertEquals("empty-test.nlogo", nlc.getWs().getModelFileName());
		assertEquals(HeadlessWorkspace.class, nlc.getWs().getClass());
	}
}
