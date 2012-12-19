package net.relatedwork.server.userHelper;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmailTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
//		Email myMail = new Email("r.pickhardt@googlemail.com","Email UnitTest", "Mail klappt");
//		myMail.send();
		Email myMail = new Email("derhein@gmail.com","Email UnitTest", "Mail klappt");
		myMail.send();
		myMail.enqueue();
	}

}
