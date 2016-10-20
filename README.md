# track-merger
Work in progress!!
Current version: 0.1.1-SNAPSHOT

Simple tool to merge several TCX (Training Centre XML) files, which is the standard format to track sport activities by Garmin, Fitbit, etc.

## Mission
This project has a simple goal: to fix those times when you are performing an activity using your Garmin/Fitbit/whatever-device-that-generates-tcx-files and it stopped by mistake. As a result you'll have two or more activities, when there should be only one. This would merge all those TCX files into one, so you can upload it to any activity tracker that accepts this format (Runtastic, Endomondo, etc).

Important: if you were looking for a tool to "magically improve" your results so you can brag about it, sorry, this software won't do that. :running:

## TODO until version 1.0.0-RELEASE
1. ~~Deserialize activity files.~~
2. ~~Remove 0 distance points from the last lap of every Activity.~~
3. ~~Get last valid distance of first training.~~
4. ~~Add last valid distance to all successive Trainings.~~
5. ~~Merge all Activities into the first Training.~~
6. ~~Serialize bean into TCX file: 0.1.0-SNAPSHOT~~
    * ~~Write activity into file.~~
    * ~~Write full datetimes (currently epoch timestamps).~~
    * ~~Indent output file.~~
    * ~~Ignore empty fields.~~
    * ~~Fix root element.~~
7. Improve deserialization: 0.2.0-SNAPSHOT
    * ~~Case insensitive when reading literals<sup>(1)</sup>.~~
    * Read `Creator` element<sup>(2)</sup>.
8. Tyding-up the code.
    * Check unit test coverage.

    <sup>(1)</sup> Currently if TCX files contain literals like _"Running"_ and _"Active"_, deserialization throws errors, so files need to be preprocessed to make those literals uppercase.

    <sup>(2)</sup> Jackson raises an error when reading `Creator` element, which is placed after all the laps. This is due to this element being abstract, because there are some elements extending it (to be exact, `Device` and `Application`).

## User manual

### Requirements
- Java SE 6+: to execute jar file.

### Usage
Whenever a stable version is ready, an executable jar file (with all its dependencies) will be available for downloading. Then you can put the TCX files you want to merge in the same directory and execute the jar file via command line, like this:
```
java -jar track-merger.jar --merge [-f <tcx_file_1> <tcx_file_2> ... [<tcx_file_n>]] [-d <destination_file>]
```
- If no tcx files are specified, current dir will be searched for them.
- If option -f is specified, at least two file names need to be provided right after it.
- If no destinantion file is specified, it will be saved in the current dir as "merged.tcx".

## Dev manual
Simply clone or zip this repo. You can generate the jar file with a `mvn package`.

### Dependencies
- JRE 1.7 as compiler (to use try-with-resources).
- Jackson Dataformat XML: to process TCX files.
- Woodstox: boosts performance of Jackson.
- Java EE Dependency Injection: (self-explanatory).
- JUnit: unit tests.

## How to generate required Java beans
For now I've decided to generate the Java beans from the Garmin schema (XSD file provided in the `src/main/resources/xsd` directory) using JAXB. To do this, run `xjc -d ../../java TrainingCenterDatabasev2.xsd` from the preferred command line, inside the _src/main/resources/xsd_ directory (if you're on Windows, remember to swap the / to \\).

In the future I might consider another method that involves Jackson exclusively. Opened to suggestions! :eyeglasses:
