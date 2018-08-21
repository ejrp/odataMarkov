----------------------------
---- OpenMarkov 0.1.3 ------
----------------------------

This package contains the source code of OpenMarkov 0.1.3, which is distributed with a EUPL 1.1 license. More information on licensing is available in the LICENSE.txt file in this folder.

Content of the package
----------------------
* bin/ - Placeholder folder for compiled class files
* lib/ - Folder containing external dependencies of OpenMarkov, downloaded from the Maven repository.
* src/ - Folder containing the source code.
* jar.args - Configuration file used to generate the executable jar for OpenMarkov
* javac.args - Configuration file used to compile the source code of OpenMarkov
* LICENSES.txt - Information about OpenMarkov's license and its dependencies.
* Manifest.txt - Stub for Manifest file to be included in the jar.
* README.txt - This file.
* tutorial.pdf - Tutorial on how to use the main features of OpenMarkov.

Compilation instructions
------------------------
JDK 1.7 needs to be installed in order to compile OpenMarkov.

To launch compilation through the command line set your working directory to be the one in which this file is, and run the following command:

> javac @javac.args

NOTE: If running on Windows the javac command is not recognized, make sure JDK's bin folder is included in the PATH environment variable.

Building an executable JAR
--------------------------
To build an executable JAR of OpenMarkov, set your working directory to be the one in which this file is and run the following command:

>jar @jar.args

This will create the file OpenMarkov.jar. In order to launch OpenMarkov through the command line, run the following command:

>java -jar OpenMarkov.jar

