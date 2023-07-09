package stepper.dd.impl.enumerator;

import stepper.dd.api.AbstractDataDefinition;


public class EnumeratorDataDefinition extends AbstractDataDefinition {
    public EnumeratorDataDefinition() {
        super("Enumerator", true, EnumeratorData.class);
    }
}
