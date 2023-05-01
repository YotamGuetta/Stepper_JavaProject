package mta.course.java.stepper.dd.impl;

import mta.course.java.stepper.dd.api.DataDefinition;
import mta.course.java.stepper.dd.impl.file.FileDataDefinition;
import mta.course.java.stepper.dd.impl.list.ListDataDefinition;
import mta.course.java.stepper.dd.impl.mapping.MappingDataDefinition;
import mta.course.java.stepper.dd.impl.numbers.DoubleDataDefinition;
import mta.course.java.stepper.dd.impl.numbers.NumberDataDefinition;
import mta.course.java.stepper.dd.impl.relation.RelationDataDefinition;
import mta.course.java.stepper.dd.impl.string.StringDataDefinition;

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
