package stepper.dd.impl.numbers;

import stepper.dd.api.AbstractDataDefinition;

public class NumberDataDefinition extends AbstractDataDefinition {
    public NumberDataDefinition() {
        super("Number", true, Integer.class);
    }
}
