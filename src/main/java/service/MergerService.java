package service;

import java.io.File;
import java.util.List;

import com.garmin.xmlschemas.trainingcenterdatabase.v2.ActivityT;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrainingCenterDatabaseT;

public interface MergerService {

	void addDistanceToActivityTracks(Double distance, ActivityT activity);

	TrainingCenterDatabaseT deserializeTrackFile(String fileName);

	Double getLastValidDistance(TrainingCenterDatabaseT training);

	File mergeTcxFiles(List<String> fileNames, String destination);

	void removeZeroDistanceTracks(TrainingCenterDatabaseT training);

	File serializeTrackFile(TrainingCenterDatabaseT training, String fileName);
}
