package stepper.step.impl;

import stepper.dd.impl.DataDefinitionRegistry;
import stepper.dd.impl.file.FileData;
import stepper.dd.impl.list.ListData;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectFilesInFolderStep extends AbstractStepDefinition {
    public CollectFilesInFolderStep() {
        super("Collect Files In Folder", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME",super.name(), DataNecessity.MANDATORY, "Folder name to scan", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILTER",super.name(), DataNecessity.OPTIONAL, "Filter only these files", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST",super.name(), DataNecessity.NA, "Files list", DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND",super.name(), DataNecessity.NA, "Total files found", DataDefinitionRegistry.NUMBER));
    }

    private StepResult stepResult(StepResult result, StepExecutionContext context, String msg, String directory) {
        if (result == StepResult.FAILURE) {
            addSummery("FAILED: reading the folder " + directory + ", " + msg);
            context.storeStepLogLine(getSummery());

        } else if (result == StepResult.WARNING) {
            addSummery("WARNING: reading the folder " + directory + ", " + msg);
            context.storeStepLogLine(getSummery());
        } else {
            addSummery("SUCCESS: the folder " + directory + ", " + msg);
        }
        return result;
    }
    @Override
    public StepResult invoke(StepExecutionContext context, String stepFinaleName) {

            String directory = context.getDataValue("FOLDER_NAME", stepFinaleName, String.class);
            String filter = context.getDataValue("FILTER", stepFinaleName, String.class);


        int countFiles = 0;
        ListData<FileData> listOfFiles = new ListData<>();

        context.storeStepLogLine("Reading folder " + directory + " content with filter " + filter);
        if(directory == null)
            return stepResult(StepResult.FAILURE, context, "the folder does not exist", directory);
        Path directoryPath = Paths.get(directory);
        if (!Files.exists(directoryPath))
            return stepResult(StepResult.FAILURE, context, "the folder does not exist", directory);
        if (!Files.isDirectory(directoryPath))
            return stepResult(StepResult.FAILURE, context, "the folder is not a directory", directory);

        try (Stream<Path> paths = Files.walk(directoryPath)) {
            List<String> listOfPaths;      // collect all matched to a List
            listOfPaths = paths
                    .filter(Files::isRegularFile)   // not a directory
                    .map(p -> p.toString().toLowerCase())       // convert path to string
                    .filter(f -> filter == null || f.endsWith(filter))       // check end with filter if given
                    .collect(Collectors.toList());

            for (String path : listOfPaths) {
                FileData file = new FileData(path);
                listOfFiles.add(file);
                countFiles++;
            }

            context.storeStepLogLine("Found " + countFiles + " files in folder matching the filter");

            // outputs
            context.storeDataValue("FILES_LIST", stepFinaleName, listOfFiles);
            context.storeDataValue("TOTAL_FOUND", stepFinaleName, countFiles);

        } catch (Exception e) {
            return stepResult(StepResult.FAILURE, context, "the folder does not exist", directory);
        }

        if(countFiles == 0) {
            return stepResult(StepResult.WARNING, context, "contains no files with filter: " + filter, directory);
        }
        return stepResult(StepResult.SUCCESS, context, "contains "+countFiles+" files", directory);
    }
}
