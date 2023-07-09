package stepper.step.impl;

import stepper.dd.impl.DataDefinitionRegistry;
import stepper.dd.impl.file.FileData;
import stepper.dd.impl.list.ListData;
import stepper.dd.impl.relation.RelationData;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilesRenamerStep extends AbstractStepDefinition {

    public FilesRenamerStep() {
        super("Files Renamer", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_TO_RENAME",super.name(), DataNecessity.MANDATORY, "Files to rename", DataDefinitionRegistry.LIST));
        addInput(new DataDefinitionDeclarationImpl("PREFIX",super.name(), DataNecessity.OPTIONAL, "Add this prefix", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("SUFFIX",super.name(), DataNecessity.OPTIONAL, "Append this suffix", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RENAME_RESULT",super.name(), DataNecessity.NA, "Rename operation summary", DataDefinitionRegistry.RELATION));

    }

    @Override
    public StepResult invoke(StepExecutionContext context, String stepFinaleName) {
        boolean allFilesRenamed = true;
        int countFiles = 1;
        String prevName;
        StringBuilder filesFailedToRename = new StringBuilder("WARNING: files failed to open: ");

        List<String> relationData = new ArrayList<>(Arrays.asList("", "", ""));
        List<String> columns = new ArrayList<>(3);

        columns.add("Index");
        columns.add("Original File Name");
        columns.add("New File Name");
        RelationData renameSummery = new RelationData(columns);

        ListData listOfFiles = context.getDataValue("FILES_TO_RENAME",stepFinaleName, ListData.class);
        String prefix = context.getDataValue("PREFIX",stepFinaleName, String.class);
        String suffix = context.getDataValue("SUFFIX",stepFinaleName, String.class);

        context.storeStepLogLine("About to start rename " + listOfFiles.size() + " files. Adding prefix: " + prefix + "; adding suffix: " + suffix);


        for (Object fileObj : listOfFiles) {
            FileData file = (FileData)fileObj;
            prevName = file.toString();
            if (!file.addPrefixAndSuffix(prefix, suffix)) {
                context.storeStepLogLine("Problem renaming file " + file);
                filesFailedToRename.append(file).append(", ");
                allFilesRenamed = false;
            }

            relationData.set(0, countFiles + "");
            relationData.set(1, prevName);
            relationData.set(2, file.toString());

            renameSummery.addData(relationData);
            countFiles++;
        }

        // outputs
        context.storeDataValue("RENAME_RESULT", stepFinaleName, renameSummery);

        if (!allFilesRenamed) {
            StringBuilder sb= new StringBuilder(filesFailedToRename.toString());
            if(sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);     //
                sb.deleteCharAt(sb.length() - 1);     // Remove the ", " at the end
            }
            context.storeStepLogLine(sb.toString());
            return StepResult.WARNING;
        }

        addSummery("SUCCESS: All Files are renamed successfully");
        if (listOfFiles.isEmpty()) {
            addSummery("SUCCESS: the list is empty");
        }

        return StepResult.SUCCESS;
    }
}
