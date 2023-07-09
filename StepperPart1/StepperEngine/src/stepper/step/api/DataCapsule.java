package stepper.step.api;

import stepper.step.api.DataDefinitionDeclaration;

public interface DataCapsule {
    String getFinalName();
    String getParentStepName();
    DataDefinitionDeclaration getDataDefinitionDeclaration();
}
