package service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrainingCenterDatabaseT;

public class MergerServiceTest {

	private MergerService mergerService;
	
	@Before
	public void setUp() {
		mergerService = new MergerService();
	}

	@Test
	public void testDeserializeFile() {
		TrainingCenterDatabaseT beanFromXml = mergerService
				.deserializeTrackFile("src/test/resources/test.tcx");

		Assert.assertNotNull("Null bean,", beanFromXml);
		Assert.assertEquals("Deserialized bean not from the expected class,",
				beanFromXml.getClass(), TrainingCenterDatabaseT.class);
	}

}
