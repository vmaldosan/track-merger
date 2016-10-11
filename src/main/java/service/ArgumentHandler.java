package service;

import java.util.List;

import utils.Utils.ExecutionResultCode;

public interface ArgumentHandler {

	ExecutionResultCode execute(String[] args);

	List<String> getFilesByExtension(String extension);

	List<String> readArgumentParams(String[] args, Integer i);
}
