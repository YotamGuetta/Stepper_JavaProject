package mta.course.java.stepper.dd.impl.file;

import mta.course.java.stepper.dd.api.AbstractDataDefinition;
import mta.course.java.stepper.dd.impl.list.ListData;

public class FileDataDefinition extends AbstractDataDefinition {
    public FileDataDefinition() {
        super("File", false, FileData.class);
    }

}
