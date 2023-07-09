package stepper.dd.impl;

import stepper.dd.api.DataDefinition;
import stepper.dd.impl.enumerator.EnumeratorData;
import stepper.dd.impl.enumerator.EnumeratorDataDefinition;
import stepper.dd.impl.file.FileDataDefinition;
import stepper.dd.impl.list.ListDataDefinition;
import stepper.dd.impl.mapping.MappingDataDefinition;
import stepper.dd.impl.numbers.DoubleDataDefinition;
import stepper.dd.impl.numbers.NumberDataDefinition;
import stepper.dd.impl.relation.RelationDataDefinition;
import stepper.dd.impl.string.StringDataDefinition;
import stepper.step.impl.ZipperStep;

import java.util.List;

public enum DataDefinitionRegistry implements DataDefinition {
    STRING(new StringDataDefinition()),
    DOUBLE(new DoubleDataDefinition()),
    RELATION(new RelationDataDefinition()),
    NUMBER(new NumberDataDefinition()),
    FILE(new FileDataDefinition()),
    LIST(new ListDataDefinition()),
    MAPPING(new MappingDataDefinition()),
    ENUMERATOR(new EnumeratorDataDefinition());

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
