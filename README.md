# track-merger
Work in progress!!
Version: 0.0.2-SNAPSHOT

Simple tool to merge several TCX files, which is the standard format to track sport activities by Garmin, Fitbit, etc.

This project has simple mission: to fix those times when you are performing an activity using your Garmin/Fitbit/whatever-device-that-generates-tcx-files and it stopped by mistake. As a result you'll have two or more activities, when there should be only one.

Important: if you were looking for a tool to "magically improve" your performance so you can brag about it, sorry, this software won't do that.

For now I decided to generate the Java beans from the schema provided (xsd file) using JAXB. To do this, run ```xjc -d ../../java TrainingCenterDatabasev2.xsd``` from the preferred command line, inside the _src/main/resources/xsd_ directory (if you're on Windows, remember to swap the / to \\).

In the future I might consider another method that involved Jackson exclusively. Opened to suggestions :)