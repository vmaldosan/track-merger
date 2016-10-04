package main;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import service.MergerService;
import utils.Utils;
import utils.Utils.ExecutionResultCode;

public final class TrackMerger {

	private static MergerService mergerService;

	@Inject
	public static void setMergerService(MergerService ms) {
		mergerService = ms;
	}

	/**
	 * @param args
	 * @param i
	 * @return List of params after found.
	 */
	private static List<String> readArgumentParams(String[] args, Integer i) {

		List<String> params = new ArrayList<String>();
		String nextArg = args[++i];
		while ((i < args.length) && !nextArg.substring(0, 1).equals("-")) {
			params.add(nextArg);
			nextArg = args[i++];
		}
		return params;
	}

	/**
	 * @param args
	 * @return ExecutionResultCode
	 */
	public static ExecutionResultCode execute(String[] args) {
		if (args == null) {
			return ExecutionResultCode.NO_ARGS;
		}

		String operation = null;
		List<String> filesToRead = null;
		String destination = "";
		for (Integer i = 0; i < args.length; i++) {
			if (args[i].substring(0, 1).startsWith("-")) {
				if (args[i].substring(1, 2).startsWith("-")) {
					operation = args[i].substring(2);
					continue;
				}
				if (args[i].substring(1, 2).equals("f")) {
					filesToRead = readArgumentParams(args, i);
					continue;
				}
				if (args[i].substring(1, 2).equals("d")) {
					destination = readArgumentParams(args, i).get(0);
				}
			}
		}

		if ((filesToRead == null) || (filesToRead.size() > 0) && (filesToRead.size() < 2)) {
			return ExecutionResultCode.NOT_ENOUGH_FILES;
		}

		if (destination.isEmpty()) {
			destination = Utils.DEFAULT_DEST;
		}

		if (operation.equals("merge")) {
			mergerService.mergeTcxFiles(filesToRead, destination);

		} else {
			return ExecutionResultCode.UNRECOGNIZED_OPERATION;
		}

		return ExecutionResultCode.SUCCESS;
	}

	public static void main(String[] args) {

		String usage = "Usage: java -jar track-merger.jar --merge [-f <tcx_file_1> <tcx_file_2> ... [<tcx_file_n>]]"
				+ " [-d <destination_file>]\n"
				+ "If no tcx files are specified, current dir will be searched for them.\n"
				+ "If -f is specified, at least two file names need to be provided right after it.\n"
				+ "If no destinantion file is specified, it will be saved in the current dir as \"merged.tcx\"";

		switch (execute(args)) {
		case SUCCESS:
			System.out.println("Merged successful.");
			break;

		case NO_ARGS:
			System.out.println("No arguments provided.\n" + usage);
			break;

		case NOT_ENOUGH_FILES:
			System.out.println("Please provide at least 2 files.\n" + usage);
			break;

		case UNRECOGNIZED_OPERATION:
			System.out.println("Operation not recognized.\n" + usage);
			break;

		default:
			System.out.println("Unexpected result.\n" + usage);
			break;
		}
	}
}
