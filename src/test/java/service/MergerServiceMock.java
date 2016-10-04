package service;

import java.io.File;
import java.util.List;

import utils.Utils;

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
	public File mergeTcxFiles(List<String> fileNames, String destination) {
		return new File(Utils.DEFAULT_DEST);
	}

	@Override
	public void removeZeroDistanceTracks(TrainingCenterDatabaseT training) {
		// TODO Auto-generated method stub

	}

	@Override
	public File serializeTrackFile(TrainingCenterDatabaseT training, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

}
