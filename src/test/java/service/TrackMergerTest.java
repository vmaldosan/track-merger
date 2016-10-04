package service;

import main.TrackMerger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import utils.Utils.ExecutionResultCode;

public class TrackMergerTest {

	@Before
	public void setUp() {
		TrackMerger.setMergerService(new MergerServiceMock());
	}

	@Test
	public void testNoArgs() {
		ExecutionResultCode result = TrackMerger.execute(null);
		Assert.assertEquals("Wrong Execution Code obtained,", ExecutionResultCode.NO_ARGS, result);
	}

	@Test
	public void testNoEnoughFiles() {
		String[] args = new String[3];
		args[0] = "--merge";
		args[1] = "-f";
		args[2] = "file1";
		ExecutionResultCode result = TrackMerger.execute(args);
		Assert.assertEquals("Wrong Execution Code obtained,", ExecutionResultCode.NOT_ENOUGH_FILES,
				result);
	}

	@Test
	public void testUnrecognizedOp() {
		String[] args = new String[4];
		args[0] = "--cut";
		args[1] = "-f";
		args[2] = "file1";
		args[3] = "file2";
		ExecutionResultCode result = TrackMerger.execute(args);
		Assert.assertEquals("Wrong Execution Code obtained,",
				ExecutionResultCode.UNRECOGNIZED_OPERATION, result);
	}

	@Test
	public void testDefaultDestination() {
		String[] args = new String[4];
		args[0] = "--merge";
		args[1] = "-f";
		args[2] = "file1";
		args[3] = "file2";

		ExecutionResultCode result = TrackMerger.execute(args);

		Assert.assertEquals("Wrong Execution Code obtained,", ExecutionResultCode.SUCCESS, result);
	}
}
