package mta.course.java.stepper.step.impl;

import mta.course.java.stepper.dd.impl.DataDefinitionRegistry;
import mta.course.java.stepper.dd.impl.file.FileData;
import mta.course.java.stepper.dd.impl.list.ListData;
import mta.course.java.stepper.dd.impl.relation.RelationData;
import mta.course.java.stepper.flow.execution.context.StepExecutionContext;
import mta.course.java.stepper.step.api.AbstractStepDefinition;
import mta.course.java.stepper.step.api.DataDefinitionDeclarationImpl;
import mta.course.java.stepper.step.api.DataNecessity;
import mta.course.java.stepper.step.api.StepResult;

import java.util.ArrayList;
import java.util.List;

public class FilesRenamerStep extends AbstractStepDefinition {

    public FilesRenamerStep() {
        super("Files Renamer", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("FILES_TO_RENAME", DataNecessity.MANDATORY, "Files to rename", DataDefinitionRegistry.LIST));
        addInput(new DataDefinitionDeclarationImpl("PREFIX", DataNecessity.OPTIONAL, "Add this prefix", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("SUFFIX", DataNecessity.OPTIONAL, "Append this suffix", DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RENAME_RESULT", DataNecessity.NA, "Rename operation summary", DataDefinitionRegistry.RELATION));

    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        boolean allFilesRenamed = true;
        int countFiles = 1;
        String prevName;
        String filesFailedToRename = "WARNING: files failed to open: ";

        List<String> relationData = new ArrayList<>(3);
        List<String> columns = new ArrayList<>(3);

        columns.add("Index");
        columns.add("Original File Name");
        columns.add("New File Name");
        RelationData renameSummery = new RelationData(columns);

        ListData<FileData> listOfFiles = context.getDataValue("FILES_TO_RENAME", ListData.class);
        String prefix = context.getDataValue("PREFIX", String.class);
        String suffix = context.getDataValue("SUFFIX", String.class);

        context.storeStepLogLine("About to start rename " + listOfFiles.size() + " files. Adding prefix: " + prefix + "; adding suffix: " + suffix);


        for (FileData file : listOfFiles) {

            prevName = file.toString();
            if (!file.addPrefixAndPrefix(prefix, suffix)) {
                context.storeStepLogLine("Problem renaming file " + file);
                filesFailedToRename += file.toString() + ", ";
                allFilesRenamed = false;
            }

            relationData.set(0, countFiles + "");
            relationData.set(1, prevName);
            relationData.set(2, file.toString());

            renameSummery.addData(relationData);
            countFiles++;
        }

        // outputs
        context.storeDataValue("RENAME_RESULT", renameSummery);

        if (!allFilesRenamed) {
            StringBuilder sb= new StringBuilder(filesFailedToRename);
            sb.deleteCharAt(sb.length()-1);     //
            sb.deleteCharAt(sb.length()-1);     // Remove the ", " at the end
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
