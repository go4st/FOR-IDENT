package de.hswt.fi.application;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationContext.class)
@ActiveProfiles("test")
public abstract class AbstractTest {

	@Before
	public abstract void setup();

	@After
	public abstract void cleanup();
}
