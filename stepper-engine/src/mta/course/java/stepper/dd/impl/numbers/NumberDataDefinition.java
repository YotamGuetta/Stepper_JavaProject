package mta.course.java.stepper.dd.impl.numbers;

import mta.course.java.stepper.dd.api.AbstractDataDefinition;

public class NumberDataDefinition extends AbstractDataDefinition {
    public NumberDataDefinition() {
        super("Number", true, Integer.class);
    }
}
