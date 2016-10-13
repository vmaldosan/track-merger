package utils;

public final class Utils {
	public enum ExecutionResultCode {
		SUCCESS, NO_ARGS, NOT_ENOUGH_FILES, UNRECOGNIZED_OPERATION
	}

	public static final String DEFAULT_DEST = "merged.tcx";
	public static final String EXT_TCX = ".tcx";
	public static final String LONG_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	public static final String ROOT_NAME = "TrainingCenterDatabase";
}
