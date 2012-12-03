package net.relatedwork.server.userHelper;

import static org.junit.Assert.*;

import java.io.File;

import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.server.utils.IOHelper;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import scala.collection.parallel.ParIterableLike.Exists;

public class ServerSIOTest {
	private static ServerSIO sSIO;
	
	@BeforeClass 
	public static void setup(){
		sSIO = new ServerSIO("ServerTestSession");
		sSIO.logUrl("first logged URL");
		sSIO.logUrl("second URL");
		sSIO.logUrl("third URL");
	}

	@AfterClass
	public static void cleanup(){
		(new File(sSIO.getSavePath())).delete();
	}
	
	@Test
	public void testSave() {
		sSIO.save();
		assert((new File(sSIO.getSavePath())).exists());
	}

}
 