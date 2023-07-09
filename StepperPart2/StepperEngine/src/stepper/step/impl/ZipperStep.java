package stepper.step.impl;

import stepper.dd.impl.DataDefinitionRegistry;
import stepper.dd.impl.enumerator.EnumeratorData;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipperStep extends AbstractStepDefinition {
    public static final String[] operationsValues = {"ZIP","UNZIP"};
    static {
        EnumeratorData.values = operationsValues;
    }
    public ZipperStep(){
        super("Zipper", false);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("SOURCE",super.name(), DataNecessity.MANDATORY, "Source", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("OPERATION",super.name(), DataNecessity.MANDATORY, "Operation type", DataDefinitionRegistry.ENUMERATOR));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT",super.name(), DataNecessity.NA, "Operation type", DataDefinitionRegistry.STRING));

    }

    public StepResult invoke(StepExecutionContext context, String stepFinaleName) {
        String source = context.getDataValue("SOURCE",stepFinaleName, String.class);
        EnumeratorData operation = context.getDataValue("OPERATION",stepFinaleName, EnumeratorData.class);

        String FilePath = source;
        context.storeStepLogLine("About to perform operation "+operation+" on source "+source, stepFinaleName);
        if(operation.getCurrentValue().equals(operationsValues[0])) {
            try {
                File sourceFile = new File(source);
                if (sourceFile.isDirectory()) {
                    FilePath += "/" + sourceFile.getName() + ".zip";
                } else {
                    FilePath = FilePath.split("\\.")[0] + ".zip";
                }

                FileOutputStream fos = new FileOutputStream(FilePath);
                ZipOutputStream zipOut = new ZipOutputStream(fos);


                zipFileOrFolder(sourceFile, zipOut, sourceFile.getName());

                zipOut.close();
                fos.close();

            } catch (IOException e) {
                addSummery("FAILURE: "+source+" is not a file or directory");
                context.storeStepLogLine(getSummery(), stepFinaleName);
                return StepResult.FAILURE;
            }
        }
        else if(operation.getCurrentValue().equals(operationsValues[1])) {
            try {

                FilePath = FilePath.substring(0,FilePath.lastIndexOf("/"));
                if(!source.contains(".") || !source.split("\\.")[1].equals("zip")){
                    addSummery("FAILURE: The File "+source+" is not a zip file");
                    context.storeStepLogLine(getSummery(), stepFinaleName);
                    return StepResult.FAILURE;
                }
                unzip(source, FilePath);

            } catch (IOException e) {
                addSummery("FAILURE: "+source+" is not a file");
                context.storeStepLogLine(getSummery(), stepFinaleName);
                return StepResult.FAILURE;
            }
        }
        else {
            addSummery("FAILURE: "+operation+" is not a valid operation");
            context.storeStepLogLine(getSummery(), stepFinaleName);
            return StepResult.FAILURE;
        }

        return StepResult.SUCCESS;
    }
        public static void zipFileOrFolder(File source, ZipOutputStream zipOut, String zipEntryName) throws IOException {
            if (source.isDirectory()) {
                File[] files = source.listFiles();
                if (files != null) {
                    for (File file : files) {
                        zipFileOrFolder(file, zipOut, zipEntryName + "/" + file.getName());
                    }
                }
            } else {
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(source);
                zipOut.putNextEntry(new ZipEntry(zipEntryName));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zipOut.write(buffer, 0, length);
                }
                fis.close();
                zipOut.closeEntry();
            }

        }
    public static void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDir + File.separator + entry.getName();

                if (!entry.isDirectory()) {
                    // If the entry is a file, extract it
                    extractFile(zipIn, filePath);
                } else {
                    // If the entry is a directory, create it
                    File dirPath = new File(filePath);
                    dirPath.mkdirs();
                }

                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    public static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = zipIn.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
    }

}
