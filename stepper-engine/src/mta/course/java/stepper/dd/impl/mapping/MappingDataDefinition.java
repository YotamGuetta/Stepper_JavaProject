package mta.course.java.stepper.dd.impl.mapping;

import mta.course.java.stepper.dd.api.AbstractDataDefinition;

public class MappingDataDefinition extends AbstractDataDefinition {
    public MappingDataDefinition() {
        super("Mapping", false, MappingData.class);
    }
}
