package mta.course.java.stepper.step.api;

import mta.course.java.stepper.step.api.DataDefinitionDeclaration;

public interface DataCapsule {
    String getFinalName();
    String getParentStepName();
    DataDefinitionDeclaration getDataDefinitionDeclaration();
}
