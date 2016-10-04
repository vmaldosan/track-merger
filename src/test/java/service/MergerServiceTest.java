package service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import service.impl.MergerServiceImpl;

import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrackpointT;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrainingCenterDatabaseT;

public class MergerServiceTest {

	private MergerServiceImpl mergerService;
	
	@Before
	public void setUp() {
		mergerService = new MergerServiceImpl();
	}

	@Test
	public void testDeserializeFile_simple() {
		TrainingCenterDatabaseT beanFromXml = mergerService
				.deserializeTrackFile("src/test/resources/test.tcx");

		Assert.assertNotNull("Null bean,", beanFromXml);
		Assert.assertEquals("Deserialized bean not from the expected class,",
				beanFromXml.getClass(), TrainingCenterDatabaseT.class);
		Assert.assertNotNull("Activities should not be null,", beanFromXml.getActivities());
		Assert.assertEquals("Ids do not match,", "2016-09-25T11:40:17.000Z", beanFromXml
				.getActivities().getActivity().get(0).getId().toString());
	}

	@Test
	public void testRemoveZeroDistanceTracks() {
		TrainingCenterDatabaseT training = mergerService
				.deserializeTrackFile("src/test/resources/activity1.tcx");
		mergerService.removeZeroDistanceTracks(training);
		List<TrackpointT> trackpoints = training.getActivities().getActivity().get(0).getLap()
				.get(2).getTrack().get(0).getTrackpoint();

		// Search for 0 distance on the last 20 trackpoints.
		for (int i = trackpoints.size() - 1; i > trackpoints.size() - 20; i--) {
			Double distance = trackpoints.get(i).getDistanceMeters();
			Assert.assertTrue("Found a non 0 distance,", !distance.equals("0.0"));
		}
	}

	@Test
	public void testGetLastValidDistance() {
		TrainingCenterDatabaseT training = mergerService
				.deserializeTrackFile("src/test/resources/activity2.tcx");

		double d = mergerService.getLastValidDistance(training);
		Assert.assertEquals(1000.6800000000001d, d, 0d);
	}

	@Test
	public void testAddDistanceToActivity() {
		TrainingCenterDatabaseT training = mergerService
				.deserializeTrackFile("src/test/resources/activity2.tcx");

		// Add 1 Km to every lap.
		mergerService.addDistanceToActivityTracks(1000d, training.getActivities().getActivity().get(0));

		// Check distance of first trackpoint
		Double first = training.getActivities().getActivity().get(0).getLap().get(0).getTrack()
				.get(0).getTrackpoint().get(0).getDistanceMeters();
		Assert.assertEquals(1000.0d, first, 0d);

		// Check last trackpoint
		Double last = training.getActivities().getActivity().get(0).getLap().get(0).getTrack()
				.get(0).getTrackpoint().get(297).getDistanceMeters();
		Assert.assertEquals(2000.68d, last, 0.001d);
	}

	/*@Test
	public void testMergeTcxFiles_fileNotFound() {
		List<String> fileNames = new ArrayList<String>();
		fileNames.add("src/test/resources/test1.tcx");
		fileNames.add("src/test/resources/test2.tcx");
		mergerService.mergeTcxFiles(fileNames);
		File mergedFile = new File("merged.tcx");
		Assert.assertTrue("Merged file not found", mergedFile.exists() && mergedFile.isFile());
	}*/
}
