package mta.course.java.stepper.flow.definition.api;

import mta.course.java.stepper.step.api.DataDefinitionDeclaration;

import java.util.InvalidPropertiesFormatException;
import java.util.List;

public interface FlowDefinition {
    String getName();
    String getDescription();
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();

    void validateFlowStructure() throws InvalidPropertiesFormatException;
    List<DataDefinitionDeclaration> getFlowFreeInputs();
}
