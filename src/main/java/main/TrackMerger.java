package main;

import service.impl.ArgumentHandlerImpl;
import service.impl.MergerServiceImpl;

public class TrackMerger {

	public static void main(String[] args) {
		ArgumentHandlerImpl argumentHandler = new ArgumentHandlerImpl(new MergerServiceImpl());

		String usage = "Usage: java -jar track-merger.jar --merge [-f <tcx_file_1> <tcx_file_2> ... [<tcx_file_n>]]"
				+ " [-d <destination_file>]\n"
				+ "If no tcx files are specified, current dir will be searched for them.\n"
				+ "If -f is specified, at least two file names need to be provided right after it.\n"
				+ "If no destinantion file is specified, it will be saved in the current dir as \"merged.tcx\"";

		switch (argumentHandler.execute(args)) {
		case SUCCESS:
			System.out.println("Merged successful.\n");
			break;

		case NO_ARGS:
			System.err.println("No arguments provided.\n" + usage);
			break;

		case NOT_ENOUGH_FILES:
			System.err.println("Please provide at least 2 files.\n" + usage);
			break;

		case UNRECOGNIZED_OPERATION:
			System.err.println("Operation not recognized.\n" + usage);
			break;

		default:
			System.err.println("Unexpected result.\n" + usage);
			break;
		}
	}
}
