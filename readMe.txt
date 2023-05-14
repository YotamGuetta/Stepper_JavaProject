									-------STEPPER--------

Submited By: Yotam Guetta		ID: 315243154		Email: yotamguetta1@gmail.com

Definition:
----------
A generic program that allows to define, run and append by untechnical people,
using a single language everyone can understand.

Implementation: 
---------------
steps - a small logic component that contain (optionally) inputs and outputs.
Steps outputs can be connected to other steps inputs if they “fit”(same data definition).
A bunch of connected steps is called a flow.
By connecting the steps in different ways the user can make different flows, and thus a stepper is made.

Summery:
--------
this project implements a stepper in javas a console application. It gets an xml file containing a list of flow and their steps logic and runs the flow a user picks with the arguments of his choosing.
Interface:
1. The program ask the user for a full path of an xml file and automatically store it's flows and check their correctness.
2. The program ask the user to pick a flow. Once chosen shows its details and ask if to continue.
3. Once the user chose to continue the user is shown all the free inputs he can enter. To start the flow the user must give all the mandatory inputs.
4. Once the flow ends, it shows some details about the flow run, including if it ended successfully goes back to part 3
5. During part 2 and 3 the user can choose if to show past runs details or statics.
5.1. If the user chose to see the past details he is given a list of his past runs and an option to show full run details.
5.2. If the user chose statistics, he is shown all the past flows and steps number of runs an average run time.

Data Values:
------------
The data types supported by the outputs/ inputs:
Number(an integer),
Double,
String,
File,
Relation(a string table with a column of keys),
List,
Mapping(a pair of values (car/ cdr)).
 
Steps:
-------
									<-Spend Some Time->
									
	description: activate sleep for a given amount of seconds.
	read only: true.
	Inputs:
		1) user name: Total sleeping time (sec)
		   step name: TIME_TO_SPEND
		   type: Number
		   necessety: Mandatory
		   description: the amount of seconds the step sleeps.
		   
								<-Collect Files In Folder->
								
	description: scans all files given folder with a given filter, and makes a list of files.
	read only: true.
	Inputs:
		1) user name: Folder name to scan
		   step name: FOLDER_NAME
		   type: String
		   necessety: Mandatory
		   description: the folder to scan's full path.
		2) user name: Filter only these files
		   step name: FILTER
		   type: String
		   necessety: Optional
		   description: filters the scanned files based their extention (example : ".txt")
	Outputs:
		1) user name: Files list
		   step name: FILES_LIST
		   type: List of Files
		   description: list of all the files scanned
		2) user name: Total files found
		   step name: TOTAL_FOUND
		   type: Number
		   description: number of files scanned
		   
									<-Files Deleter->
									
	description: deletes a list of files.
	read only: false.
	Inputs:
		1) user name: Files to delete.
		   step name: FILES_LIST
		   type: List of Files.
		   necessety: Mandatory
		   description: the list of files to delete.
	Outputs:
		1) user name: Files failed to be deleted
		   step name: DELETED_LIST
		   type: List of Strings
		   description: full path of all the files failed to delete
		2) user name: DELETION_STATS
		   step name: TOTAL_FOUND
		   type: Mapping of two Numbers
		   description: car: files successfully deleted, cdr: files failed to delete
									
									<-Files Renamer->
									
	description: adds a prefix and a suffix to a list of files names.
	read only: false.
	Inputs:
		1) user name: Files to rename.
		   step name: FILES_TO_RENAME
		   type: List of Files.
		   necessety: Mandatory
		   description: list of files to rename.
		2) user name: Add this prefix
		   step name: PREFIX
		   type: String
		   necessety: Optional
		   description: prefix to add to the files name.(if present)
		3) user name: append this suffix
		   step name: SUFFIX
		   type: String
		   necessety: Optional
		   description: suffix to add to the files name.(if present)
	Outputs:
		1) user name: Rename operation summary
		   step name: RENAME_RESULT
		   type: Relation
		   description: a table with each file index, original name and new name.
								
								<-Files Content Extractor->

	description: with a given file list and line gets every file's context in the given line.
	read only: true.
	Inputs:
		1) user name: Files to extract
		   step name: FILES_LIST
		   type: List of Files.
		   necessety: Mandatory
		   description: list of files to extract.
		2) user name: Line number to extract
		   step name: LINE
		   type: Number
		   necessety: Mandatory
		   description: line to extract
	Outputs:
		1) user name: Data extraction
		   step name: DATA
		   type: Relation
		   description: a table with each file index, original name and context in the given line.
									
									<-CSV Exporter->

	description: turns a given Relation table into a CSV format String.
	read only: true.
	Inputs:
		1) user name: Source data
		   step name: SOURCE
		   type: Relation
		   necessety: Mandatory
		   description: A Relation table.
	Outputs:
		1) user name: CSV export result
		   step name: RESULT
		   type: String
		   description: The table in CSV format.
		   
									<-Proporties Exporter->
									
	description: turns a given Relation table into a proporties format String.
	read only: true.
	Inputs:
		1) user name: Source data
		   step name: SOURCE
		   type: Relation
		   necessety: Mandatory
		   description: A Relation table.
	Outputs:
		1) user name: Properties export result
		   step name: RESULT
		   type: String
		   description: The table in proporties format.
									
									<-Files Dumper->

	description: makes a new file with given data.
	read only: true.
	Inputs:
		1) user name: Content
		   step name: CONTENT
		   type: String
		   necessety: Mandatory
		   description: the data to write.
		1) user name: Target file path
		   step name: FILE_NAME
		   type: String
		   necessety: Mandatory
		   description: the full path to the file you want to create (with it's name).
	Outputs:
		1) user name: File Creation Resul
		   step name: RESULT
		   type: String
		   description: The result of the attempt to create the file.
 
Logic And Features:
------------------

Steps input/output rules:
 For each step1 and step2 (assuming step1 came before step2)
1)  step2 output cannot be connected to step1 input.
2)  If step1 output and step2 input are the same name and data type their connected
3)  One output can be connected to multiple inputs.
4) One input can be connected to only one output at most
5) Outputs that are not connected to any input are called “Free Outputs” and will added to the flow             formal outputs.
6) Inputs that are not connected to any output are called “Free Inputs” and will be given by the user
7) Each input is Mandatory or Optional. If Mandatory, the flow can’t run without a value.
8) No two output with the same name and type.

Custom mapping:
*The user can define the flow with custom connections between steps inputs and outputs regardless of their names.
**NOTE: custom mapping between different data definitions or violation of steps input/output rule 1 are prohibited.

Aliasing:
*Unique names given to steps and steps inputs/outputs which are used reputedly on a flow, in order to differentiate between them.

Xml File Format:
----------------

<ST-Stepper xmlns:xsi=”…">
	<ST-Flows>
		<ST-Flow name="Flow Name">
		<ST-FlowDescription>Flow Description</ST-FlowDescription>
		<ST-FlowOutput>Flow Formal Outputs</ST-FlowOutput>
			<ST-StepsInFlow>
				<ST-StepInFlow name="Step Name" alias="Step Alias" continue-if-failing="true/false"/>
				…
			</ST-StepsInFlow>
			<ST-FlowLevelAliasing>
				<ST-FlowLevelAlias step="StepName(afther alias)" source-data-name="Data Member Original Name" alias=" Data Member Alias"/>
				…
			</ST-FlowLevelAliasing>
			<ST-CustomMappings>
				<ST-CustomMapping source-step="Source Step" source-data="Source Data" target-step="Target Step" target-data="Target Data"/>
				…
			</ST-CustomMappings>
		</ST-Flow>
		…
	</ST-Flows>
</ST-Stepper>

