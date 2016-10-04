# track-merger
Work in progress!!
Version: 0.0.4-SNAPSHOT

Simple tool to merge several TCX files, which is the standard format to track sport activities by Garmin, Fitbit, etc.

## Mission
This project has simple mission: to fix those times when you are performing an activity using your Garmin/Fitbit/whatever-device-that-generates-tcx-files and it stopped by mistake. As a result you'll have two or more activities, when there should be only one. This would merge all those TCX files into one, so you can upload it to any other activity tracker of your choice (Runtastic, Endomondo, etc).

Important: if you were looking for a tool to "magically improve" your results so you can brag about it, sorry, this software won't do that. :running:

## TODO
1. ~~Deserialize activity files.~~
2. ~~Remove 0 distance points from the last lap of every Activity.~~
3. ~~Get last valid distance of first training.~~
4. ~~Add last valid distance to all successive Trainings.~~
5. ~~Merge all Activities into the first Training.~~
6. Serialize bean into TCX file.
7. Tyding-up the code.

## User manual

### Requirements
- Java SE 6+: to execute jar file.

## Usage
Whenever a stable is ready, an executable jar file (with all its dependencies) will be available for downloading. Then you can put the TCX files you want to merge in the same directory and execute the jar file via command line, like this:
```
java -jar track-merger.jar --merge [-f <tcx_file_1> <tcx_file_2> ... [<tcx_file_n>]] [-d <destination_file>]
```

If no tcx files are specified, current dir will be searched for them.
If option -f is specified, at least two file names need to be provided right after it.
If no destinantion file is specified, it will be saved in the current dir as "merged.tcx".

## Dev manual
Simply clone or zip this repo. You can generate the jar file with a `mvn package`.

### Dependencies
- Jackson Dataformat XML: to process TCX files.
- Woodstox: helps performance of Jackson.
- JUnit: unit tests.
- Java EE Dependency Injection: (self-explanatory, might switch to Spring 4).

## How to generate required Java beans
For now I've decided to generate the Java beans from the Garmin schema (xsd file provided in the `src/main/resources/xsd` directory) using JAXB. To do this, run `xjc -d ../../java TrainingCenterDatabasev2.xsd` from the preferred command line, inside the _src/main/resources/xsd_ directory (if you're on Windows, remember to swap the / to \\).

In the future I might consider another method that involves Jackson exclusively. Opened to suggestions :eyeglasses: