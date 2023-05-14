package stepper.dd.impl;

import stepper.dd.api.DataDefinition;
import stepper.dd.impl.file.FileDataDefinition;
import stepper.dd.impl.list.ListDataDefinition;
import stepper.dd.impl.mapping.MappingDataDefinition;
import stepper.dd.impl.numbers.DoubleDataDefinition;
import stepper.dd.impl.numbers.NumberDataDefinition;
import stepper.dd.impl.relation.RelationDataDefinition;
import stepper.dd.impl.string.StringDataDefinition;

public enum DataDefinitionRegistry implements DataDefinition {
    STRING(new StringDataDefinition()),
    DOUBLE(new DoubleDataDefinition()),
    RELATION(new RelationDataDefinition()),
    NUMBER(new NumberDataDefinition()),
    FILE(new FileDataDefinition()),
    LIST(new ListDataDefinition()),
    MAPPING(new MappingDataDefinition());

    DataDefinitionRegistry(DataDefinition dataDefinition) {
        this.dataDefinition = dataDefinition;
    }

    private final DataDefinition dataDefinition;

    public String getName() {
        return dataDefinition.getName();
    }

    public boolean isUserFriendly() {
        return dataDefinition.isUserFriendly();
    }

    public Class<?> getType() {
        return dataDefinition.getType();
    }

}
