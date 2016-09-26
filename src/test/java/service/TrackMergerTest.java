package service;

import main.TrackMerger;

import org.junit.Assert;
import org.junit.Test;

import utils.Utils.ExecutionResultCode;

public class TrackMergerTest {

	@Test
	public void testNoArgs() {
		ExecutionResultCode result = TrackMerger.main(null);
		Assert.assertEquals("Wrong Execution Code obtained,", ExecutionResultCode.NO_ARGS, result);
	}

	@Test
	public void testUnrecognizedOp() {
		String[] args = new String[1];
		args[0] = "--cut";
		ExecutionResultCode result = TrackMerger.main(args);
		Assert.assertEquals("Wrong Execution Code obtained,",
				ExecutionResultCode.UNRECOGNIZED_OPERATION, result);
	}

	@Test
	public void testNoEnoughFiles() {
		String[] args = new String[3];
		args[0] = "--merge";
		args[1] = "-f";
		args[2] = "file1";
		ExecutionResultCode result = TrackMerger.main(args);
		Assert.assertEquals("Wrong Execution Code obtained,", ExecutionResultCode.NOT_ENOUGH_FILES,
				result);
	}
}
