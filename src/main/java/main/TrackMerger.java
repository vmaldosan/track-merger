package main;
import java.util.ArrayList;
import java.util.List;

import service.MergerService;
import utils.Utils.ExecutionResultCode;

public class TrackMerger {

	public static ExecutionResultCode main(String[] args) {
		String usage = "Usage: java -jar track-merger.jar --merge [-f <tcx_file_1> <tcx_file_2> ... [<tcx_file_n>]]"
				+ " [-d <destination_file>]\n"
				+ "If no tcx files are specified, current dir will be searched for them.\n"
				+ "If -f is specified, at least two file names need to be provided right after it.\n"
				+ "If no destinantion file is specified, it will be saved in the current dir as \"merged.tcx\"";

		if (args == null) {
			System.out.println("No arguments provided.\n" + usage);
			return ExecutionResultCode.NO_ARGS;
		}

		String operation = null;
		List<String> files = new ArrayList<String>();
		boolean readingFiles = false;
		for (String arg : args) {
			if (arg.substring(0, 1).startsWith("-")) {
				if (arg.substring(1, 2).startsWith("-")) {
					operation = arg.substring(2);
					continue;
				}
				if (arg.substring(1, 2).equals("f")) {
					readingFiles = true;
					continue;
				}
			}
			if (readingFiles) {
				files.add(arg);
			}
		}

		if ((files.size() > 0) && (files.size() < 2)) {
			System.out.println("Please provide at least 2 files.\n" + usage);
			return ExecutionResultCode.NOT_ENOUGH_FILES;
		}

		if (operation.equals("merge")) {
			MergerService mergerService = new MergerService();
			mergerService.mergeTcxFiles(files);

		} else {
			System.out.println("Operation not recognized.\n" + usage);
			return ExecutionResultCode.UNRECOGNIZED_OPERATION;
		}

		return ExecutionResultCode.SUCCESS;
	}
}
