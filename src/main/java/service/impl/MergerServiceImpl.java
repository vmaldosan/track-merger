package service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import service.MergerService;
import utils.Utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
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

	@Inject
	public MergerServiceImpl() {
		mapper = new XmlMapper();

		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector(mapper.getTypeFactory());

		AnnotationIntrospector pair = AnnotationIntrospectorPair.create(primary, secondary);
		mapper.setAnnotationIntrospector(pair);

		SimpleDateFormat sdf = new SimpleDateFormat(Utils.LONG_DATE_FORMAT);
		mapper.setDateFormat(sdf);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setSerializationInclusion(Include.NON_NULL);
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
		int numTrackpoints = trackpoints.size();
		for (int i = numTrackpoints - 1; i > 0; i--) {
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
	 * Writes a Training into the file located at <i>fileName</i>.
	 * Candidate to be moved to a new class.
	 * 
	 * @param training
	 * @param fileName
	 */
	@Override
	public void serializeTrackFile(TrainingCenterDatabaseT training, String fileName) {
		File destination = new File(fileName);
		ObjectWriter writer = mapper.writer().withRootName("TrainingCenterDatabase");

		try (FileOutputStream fos = new FileOutputStream(destination)) {
			fos.write(writer.writeValueAsBytes(training));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();

		} catch (JsonProcessingException e2) {
			e2.printStackTrace();

		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}

	/**
	 * @param fileNames - list of tcx files to merge. Assuming not null or empty.
	 * @param destination - relative path and file name for the merged file.
	 */
	@Override
	public void mergeTcxFiles(List<String> fileNames, String destination) {
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

		int numTrainings = trainings.size();
		for (int i = 1; i < numTrainings; i++) {
			// 4. Add last valid distance to all successive Trainings.
			ActivityT activity = trainings.get(i).getActivities().getActivity().get(0);
			addDistanceToActivityTracks(distanceWhenInterrupted, activity);

			// 5. Merge all Activities into the first Training.
			firstTraining.getActivities().getActivity().get(0).getLap().addAll(activity.getLap());
		}

		// 6. Serialize bean into TCX file.
		serializeTrackFile(firstTraining, destination);
	}
}
