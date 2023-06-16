package stepper.step.api;

import stepper.dd.api.DataDefinition;

public interface DataDefinitionDeclaration {
    String getName();
    DataNecessity necessity();
    String userString();
    DataDefinition dataDefinition();
    void setName(String newName);
    void setParentStepName(String parentStepName);
    String getParentStepName();
    String getOriginalName();
}
