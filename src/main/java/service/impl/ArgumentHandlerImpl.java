package service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import service.ArgumentHandler;
import service.MergerService;
import utils.Utils;
import utils.Utils.ExecutionResultCode;

public class ArgumentHandlerImpl implements ArgumentHandler {

	final MergerService mergerService;

	@Inject
	public ArgumentHandlerImpl(MergerService ms) {
		this.mergerService = ms;
	}

	@Override
	public List<String> readArgumentParams(String[] args, Integer i) {
		List<String> params = new ArrayList<>();
		String nextArg = args[++i];
		while ((i < args.length) && !nextArg.substring(0, 1).equals("-")) {
			params.add(nextArg);
			nextArg = args[i++];
		}
		return params;
	}

	@Override
	public List<String> getFilesByExtension(String extension) {
		List<String> result = new ArrayList<>();
		File currentDir = new File(".");
		for (File fileEntry : currentDir.listFiles()) {
			if (fileEntry.isFile() && fileEntry.getName().endsWith(extension)) {
				result.add(fileEntry.getName());
			}
		}
		return result;
	}

	@Override
	public ExecutionResultCode execute(String[] args) {
		if (args == null) {
			return ExecutionResultCode.NO_ARGS;
		}

		String operation = null;
		List<String> filesToRead = null;
		String destination = "";
		int numArgs = args.length;
		for (Integer i = 0; i < numArgs; i++) {
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

		if (filesToRead == null) {
			filesToRead = getFilesByExtension(Utils.EXT_TCX);

		} else if (filesToRead.size() < 2) {
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

}
