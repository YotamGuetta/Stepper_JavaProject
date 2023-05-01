package mta.course.java.stepper.dd.impl.list;

import mta.course.java.stepper.dd.api.AbstractDataDefinition;
import mta.course.java.stepper.dd.impl.relation.RelationData;

public class ListDataDefinition extends AbstractDataDefinition {
    public ListDataDefinition() {
        super("List", false, ListData.class);
    }
}
