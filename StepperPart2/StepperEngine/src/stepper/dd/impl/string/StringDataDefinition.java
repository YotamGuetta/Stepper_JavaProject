package stepper.dd.impl.string;

import stepper.dd.api.AbstractDataDefinition;

public class StringDataDefinition extends AbstractDataDefinition {

    public StringDataDefinition() {
        super("String", true, String.class);
    }
}
