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
	public void testDeserializeFileSimple() {
		TrainingCenterDatabaseT beanFromXml = mergerService
				.deserializeTrackFile("src/test/resources/test.tcx");

		Assert.assertNotNull("Null bean,", beanFromXml);
		Assert.assertEquals("Deserialized bean not from the expected class,",
				beanFromXml.getClass(), TrainingCenterDatabaseT.class);
		Assert.assertNotNull("Activities should not be null,", beanFromXml.getActivities());
		Assert.assertEquals("Ids do not match,", "2016-09-25T11:40:17.000Z", beanFromXml
				.getActivities().getActivity().get(0).getId().toString());
	}

}
