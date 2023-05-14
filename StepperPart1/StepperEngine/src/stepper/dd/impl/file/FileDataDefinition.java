package stepper.dd.impl.file;

import stepper.dd.api.AbstractDataDefinition;

public class FileDataDefinition extends AbstractDataDefinition {
    public FileDataDefinition() {
        super("File", false, FileData.class);
    }

}
