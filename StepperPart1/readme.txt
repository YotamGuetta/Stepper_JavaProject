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
The data types supported by the outputs/ inputs.
 Number(an integer),
Double,
 String,
 File,
 Re(a string table with a column of keys),
List,
 Mapping(a pair of values (cur/ car))
 
Steps:
-------
Spend Some Time
Collect Files In Folder
Files Deleter
Files Renamer
Files Content Extractor    
CSV Exporter
Proporties Exporter
Files Dumper
 
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

