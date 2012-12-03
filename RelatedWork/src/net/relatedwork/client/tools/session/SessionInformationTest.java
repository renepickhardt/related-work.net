package net.relatedwork.client.tools.session;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SessionInformationTest {
	
	private static SessionInformation SIO;
	
	@BeforeClass
	public static void testSetup() {
		SIO = new SessionInformation("TestSession");
	}

	@AfterClass
	public static void testCleanup() {
		SIO = null;
	}
	
	@Test
	public void testClearLogs() {
		SIO.clearLogs();
		assertEquals(SIO.getVisitedUrls(), new ArrayList<String>());
	}

	@Test
	public void testLogUrl() {
		SIO.clearLogs();
		String s1 = "First URL";
		String s2 = "Second URL";
		
		SIO.logUrl(s1);
		SIO.logUrl(s2);
		
		assert(SIO.getVisitedUrls().contains(s1));
		assert(SIO.getVisitedUrls().contains(s2));
	}

}
