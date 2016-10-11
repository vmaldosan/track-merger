package service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import service.impl.ArgumentHandlerImpl;
import utils.Utils.ExecutionResultCode;

public class ArgumentHandlerTest {

	ArgumentHandlerImpl handler;
	
	@Before
	public void setUp() {
		handler = new ArgumentHandlerImpl(new MergerServiceMock());
	}

	@Test
	public void testNoArgs() {
		ExecutionResultCode result = handler.execute(null);
		assertEquals("Wrong Execution Code obtained,", ExecutionResultCode.NO_ARGS, result);
	}

	@Test
	public void testNoEnoughFiles() {
		String[] args = new String[3];
		args[0] = "--merge";
		args[1] = "-f";
		args[2] = "file1";
		ExecutionResultCode result = handler.execute(args);
		assertEquals("Wrong Execution Code obtained,", ExecutionResultCode.NOT_ENOUGH_FILES,
				result);
	}

	@Test
	public void testUnrecognizedOp() {
		String[] args = new String[4];
		args[0] = "--cut";
		args[1] = "-f";
		args[2] = "file1";
		args[3] = "file2";
		ExecutionResultCode result = handler.execute(args);
		assertEquals("Wrong Execution Code obtained,",
				ExecutionResultCode.UNRECOGNIZED_OPERATION, result);
	}

	@Test
	public void testDefaultDestination() {
		String[] args = new String[4];
		args[0] = "--merge";
		args[1] = "-f";
		args[2] = "file1";
		args[3] = "file2";
		ExecutionResultCode result = handler.execute(args);
		assertEquals("Wrong Execution Code obtained,", ExecutionResultCode.SUCCESS, result);
	}
}
