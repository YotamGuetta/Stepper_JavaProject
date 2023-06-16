package stepper.dd.impl.list;

import stepper.dd.api.AbstractDataDefinition;

public class ListDataDefinition extends AbstractDataDefinition {
    public ListDataDefinition() {
        super("List", false, ListData.class);
    }
}
