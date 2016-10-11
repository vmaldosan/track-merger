package service;

import java.util.List;

import com.garmin.xmlschemas.trainingcenterdatabase.v2.ActivityT;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrainingCenterDatabaseT;

public class MergerServiceMock implements MergerService {

	@Override
	public void addDistanceToActivityTracks(Double distance, ActivityT activity) {
		// TODO Auto-generated method stub

	}

	@Override
	public TrainingCenterDatabaseT deserializeTrackFile(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getLastValidDistance(TrainingCenterDatabaseT training) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mergeTcxFiles(List<String> fileNames, String destination) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeZeroDistanceTracks(TrainingCenterDatabaseT training) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serializeTrackFile(TrainingCenterDatabaseT training, String fileName) {
		// TODO Auto-generated method stub
	}

}
