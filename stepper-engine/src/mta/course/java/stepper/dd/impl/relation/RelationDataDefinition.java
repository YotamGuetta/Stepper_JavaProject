package mta.course.java.stepper.dd.impl.relation;

import mta.course.java.stepper.dd.api.AbstractDataDefinition;

public class RelationDataDefinition extends AbstractDataDefinition {
    public RelationDataDefinition() {
        super("Relation", false, RelationData.class);
    }
}
