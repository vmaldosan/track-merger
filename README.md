# track-merger
Work in progress!!
Version: 0.0.3-SNAPSHOT

Simple tool to merge several TCX files, which is the standard format to track sport activities by Garmin, Fitbit, etc.

## Mission
This project has simple mission: to fix those times when you are performing an activity using your Garmin/Fitbit/whatever-device-that-generates-tcx-files and it stopped by mistake. As a result you'll have two or more activities, when there should be only one. This would merge all those TCX files into one, so you can upload it to any other activity tracker of your choice (Runtastic, Endomondo, etc).

Important: if you were looking for a tool to "magically improve" your performance so you can brag about it, sorry, this software won't do that.

## Usage
The idea is to use it as a jar file ()
```
java -jar track-merger.jar --merge [-f <tcx_file_1> <tcx_file_2> ... [<tcx_file_n>]] [-d <destination_file>]
```

If no tcx files are specified, current dir will be searched for them.
If -f is specified, at least two file names need to be provided right after it.
If no destinantion file is specified, it will be saved in the current dir as \"merged.tcx\"";

## How to generate required Java beans
For now I decided to generate the Java beans from the schema provided (xsd file) using JAXB. To do this, run ```xjc -d ../../java TrainingCenterDatabasev2.xsd``` from the preferred command line, inside the _src/main/resources/xsd_ directory (if you're on Windows, remember to swap the / to \\).

In the future I might consider another method that involved Jackson exclusively. Opened to suggestions :)