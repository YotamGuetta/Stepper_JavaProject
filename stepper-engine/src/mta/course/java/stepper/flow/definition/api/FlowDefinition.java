package mta.course.java.stepper.flow.definition.api;

import javafx.util.Pair;
import mta.course.java.stepper.step.api.DataCapsuleImpl;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;

import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;

public interface FlowDefinition {
    String getName();
    String getDescription();
    void addFlowOutput(List<String> outputName);
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();

    void validateFlowStructure() throws InvalidPropertiesFormatException;
    Map<String, DataCapsuleImpl> getFlowFreeInputs();
    void addAliasingMapping(String step, String source, String alias);
    void addCustomMapping(String sourceStep, String sourceData, String targetStep, String targetData);
    Pair<String, String> getCustomMapping(String sourceStep, String sourceData);
    List<DataCapsuleImpl> getAllFlowOutputs();
    Map<Pair<String,String>, Pair<String, String>> getFullCustomMapping();
    List<DataCapsuleImpl> getAllDataCapsules();
    boolean isFlowReadOnly();
    DataCapsuleImpl getOutputDataCapsule(String outputName);
    }
