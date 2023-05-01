package mta.course.java.stepper.dd.impl.mapping;

import mta.course.java.stepper.dd.api.AbstractDataDefinition;
import mta.course.java.stepper.dd.impl.list.ListData;

public class MappingDataDefinition extends AbstractDataDefinition {
    public MappingDataDefinition() {
        super("Mapping", false, MappingData.class);
    }
}
