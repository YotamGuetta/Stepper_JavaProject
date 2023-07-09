package mta.course.java.stepper.dd.impl.file;

import mta.course.java.stepper.dd.api.AbstractDataDefinition;

public class FileDataDefinition extends AbstractDataDefinition {
    public FileDataDefinition() {
        super("File", false, FileData.class);
    }

}
