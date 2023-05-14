package stepper.dd.impl.mapping;

import stepper.dd.api.AbstractDataDefinition;

public class MappingDataDefinition extends AbstractDataDefinition {
    public MappingDataDefinition() {
        super("Mapping", false, MappingData.class);
    }
}
