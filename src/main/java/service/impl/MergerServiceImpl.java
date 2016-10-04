package service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import service.MergerService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.ActivityLapT;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.ActivityT;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrackpointT;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrainingCenterDatabaseT;

public class MergerServiceImpl implements MergerService {

	private XmlMapper mapper;

	public MergerServiceImpl() {
		mapper = new XmlMapper();

		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector(mapper.getTypeFactory());

		AnnotationIntrospector pair = AnnotationIntrospectorPair.create(primary, secondary);
		mapper.setAnnotationIntrospector(pair);
	}

	/**
	 * Reads Training from a TCX file. Candidate to be moved to a new class.
	 * 
	 * @param fileName - Assuming not null.
	 * @return TrainingCenterDatabaseT - Bean with the
	 */
	@Override
	public TrainingCenterDatabaseT deserializeTrackFile(String fileName) {
		TrainingCenterDatabaseT beanFromXml = null;
		try {
			beanFromXml = mapper.readValue(new File(fileName), TrainingCenterDatabaseT.class);

		} catch (JsonParseException jpe) {
			jpe.printStackTrace();

		} catch (JsonMappingException jme) {
			jme.printStackTrace();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return beanFromXml;
	}

	private ActivityLapT getLastLap(TrainingCenterDatabaseT training) {
		// Assuming one activity per training.
		List<ActivityLapT> laps = training.getActivities().getActivity().get(0).getLap();
		return laps.get(laps.size() - 1);
	}

	@Override
	public void removeZeroDistanceTracks(TrainingCenterDatabaseT training) {
		// Assuming one track per lap.
		List<TrackpointT> trackpoints = getLastLap(training).getTrack().get(0).getTrackpoint();
		for (int i = trackpoints.size() - 1; i > 0; i--) {
			if (trackpoints.get(i).getDistanceMeters().equals(0.0)) {
				trackpoints.remove(i);
			} else {
				break;
			}
		}
	}

	@Override
	public Double getLastValidDistance(TrainingCenterDatabaseT training) {
		// Assuming one track per lap.
		List<TrackpointT> trackpoints = getLastLap(training).getTrack().get(0).getTrackpoint();
		return trackpoints.get(trackpoints.size() - 1).getDistanceMeters();
	}

	/**
	 * Adds the distance parameter to every trackpoint in the activity.
	 * 
	 * @param distance - Double to be added
	 * @param activity - ActivityT containing the Track, which contains Trackpoints
	 */
	@Override
	public void addDistanceToActivityTracks(Double distance, ActivityT activity) {
		for (ActivityLapT lap : activity.getLap()) {
			for (TrackpointT trackpoint : lap.getTrack().get(0).getTrackpoint()) {
				trackpoint.setDistanceMeters(trackpoint.getDistanceMeters() + distance);
			}
		}
	}

	/**
	 * @param fileNames - list of tcx files to merge. Assuming not null or empty.
	 * @param destination - relative path and file name for the merged file.
	 */
	@Override
	public File mergeTcxFiles(List<String> fileNames, String destination) {
		File result = null;

		// 1. Deserialize activity files.
		List<TrainingCenterDatabaseT> trainings = new ArrayList<TrainingCenterDatabaseT>();
		for (String fileName : fileNames) {
			trainings.add(deserializeTrackFile(fileName));
		}
		// 2. Remove 0 distance points from the last lap of every Activity.
		for (TrainingCenterDatabaseT training : trainings) {
			removeZeroDistanceTracks(training);
		}

		// 3. Get last valid distance of first training.
		Double distanceWhenInterrupted = getLastValidDistance(trainings.get(0));

		TrainingCenterDatabaseT firstTraining = trainings.get(0);

		for (int i = 1; i < trainings.size(); i++) {
			// 4. Add last valid distance to all successive Trainings.
			ActivityT activity = trainings.get(i).getActivities().getActivity().get(0);
			addDistanceToActivityTracks(distanceWhenInterrupted, activity);

			// 5. Merge all Activities into the first Training.
			firstTraining.getActivities().getActivity().add(activity);
		}

		// 6. Serialize bean into TCX file.
		result = serializeTrackFile(firstTraining, destination);

		return result;
	}

	/**
	 * Writes a Training into a TCX file. Candidate to be moved to a new class.
	 * 
	 * @param training
	 * @param fileName
	 * @return
	 */
	@Override
	public File serializeTrackFile(TrainingCenterDatabaseT training, String fileName) {

		return null;
	}
}
