package stepper.dd.impl.numbers;

import stepper.dd.api.AbstractDataDefinition;

public class DoubleDataDefinition extends AbstractDataDefinition {
    public DoubleDataDefinition() {
        super("Double", true, Double.class);
    }
}
