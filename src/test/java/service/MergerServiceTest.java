package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import service.impl.MergerServiceImpl;
import utils.Utils;

import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrackpointT;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrainingCenterDatabaseT;

public class MergerServiceTest {

	private MergerServiceImpl mergerService;

	@Before
	public void setUp() {
		mergerService = new MergerServiceImpl();
	}

	@Test
	public void testDeserializeFile() {
		TrainingCenterDatabaseT training = mergerService
				.deserializeTrainingFile("src/test/resources/full_file.tcx");

		assertNotNull("Null bean,", training);
		assertEquals("Deserialized bean not from the expected class,", training.getClass(),
				TrainingCenterDatabaseT.class);
		assertNotNull("Activities should not be null,", training.getActivities());
		assertEquals("Ids do not match,", "2016-09-25T11:25:34.000Z", training.getActivities()
				.getActivity().get(0).getId().toString());
	}

	@Test
	public void testRemoveZeroDistanceTracks() {
		TrainingCenterDatabaseT training = mergerService
				.deserializeTrainingFile("src/test/resources/activity1.tcx");
		assertNotNull("Null bean,", training);

		mergerService.removeZeroDistanceTracks(training);
		List<TrackpointT> trackpoints = training.getActivities().getActivity().get(0).getLap()
				.get(2).getTrack().get(0).getTrackpoint();

		// Search for 0 distance on the last 5 trackpoints.
		int numTrackpoints = trackpoints.size();
		for (int i = numTrackpoints - 1; i >= numTrackpoints - 5; i--) {
			Double distance = trackpoints.get(i).getDistanceMeters();
			assertTrue("Found a non 0 distance,", !distance.equals(0.0));
		}
	}

	@Test
	public void testGetLastValidDistance() {
		TrainingCenterDatabaseT training = mergerService
				.deserializeTrainingFile("src/test/resources/full_file.tcx");
		assertNotNull("Null bean,", training);

		double d = mergerService.getLastValidDistance(training);
		assertEquals(2838.3d, d, 0d);
	}

	@Test
	public void testAddDistanceToActivity() {
		TrainingCenterDatabaseT training = mergerService
				.deserializeTrainingFile("src/test/resources/activity2.tcx");
		assertNotNull("Null bean,", training);

		// Add 1 Km to every lap.
		mergerService.addDistanceToActivityTracks(1000d, training.getActivities().getActivity()
				.get(0));

		// Check distance of first trackpoint
		Double first = training.getActivities().getActivity().get(0).getLap().get(0).getTrack()
				.get(0).getTrackpoint().get(0).getDistanceMeters();
		assertEquals(1000.0d, first, 0d);

		// Check last trackpoint
		Double last = training.getActivities().getActivity().get(0).getLap().get(0).getTrack()
				.get(0).getTrackpoint().get(297).getDistanceMeters();
		assertEquals(2000.68d, last, 0.001d);
	}

	@Test
	public void testSerializeTrackFile_defaultDest() {
		TrainingCenterDatabaseT training = mergerService
				.deserializeTrainingFile("src/test/resources/activity2.tcx");
		assertNotNull("Null bean,", training);

		mergerService.serializeTrainingFile(training, Utils.DEFAULT_DEST);

		File mergedFile = new File(Utils.DEFAULT_DEST);
		assertTrue("Merged file with default name not found,",
				mergedFile.exists() && mergedFile.isFile());
	}

	@Test
	public void testMergeTcxFiles_defaultDest() {
		List<String> fileNames = new ArrayList<>(2);
		fileNames.add("activity1.tcx");
		fileNames.add("activity2.tcx");

		mergerService.mergeTcxFiles(fileNames, Utils.DEFAULT_DEST);

		File mergedFile = new File(Utils.DEFAULT_DEST);
		assertTrue("Merged file with default name not found,",
				mergedFile.exists() && mergedFile.isFile());
	}

}
