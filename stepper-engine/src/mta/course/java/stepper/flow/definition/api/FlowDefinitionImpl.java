package mta.course.java.stepper.flow.definition.api;

import mta.course.java.stepper.step.api.DataDefinitionDeclaration;
import mta.course.java.stepper.step.api.StepResult;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;

public class FlowDefinitionImpl implements FlowDefinition {

    private final String name;
    private final String description;
    private final List<String> flowOutputs;
    private final List<String> flowFreeOutputs;
    private final List<StepUsageDeclaration> steps;

    private final List<DataDefinitionDeclaration> flowFreeInputs;

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        flowFreeInputs = new ArrayList<>();
        flowFreeOutputs = new ArrayList<>();
    }

    public void addFlowOutput(String outputName) {
        flowOutputs.add(outputName);
        flowFreeOutputs.add(outputName);
    }

    private void addFlowInputs( List<DataDefinitionDeclaration> inputs) throws InvalidPropertiesFormatException {
        for(DataDefinitionDeclaration input : inputs){
            if(input.dataDefinition().isUserFriendly())
                flowFreeInputs.add(input);
            else
                throw new InvalidPropertiesFormatException("input "+input.getName()+" is not user friendly");
        }

    }
    private void addFlowOutputs( List<DataDefinitionDeclaration> Outputs) {
        for (DataDefinitionDeclaration output : Outputs) {
            flowOutputs.add(output.getName());
            flowFreeOutputs.add(output.getName());
        }

    }

    private void addOutputsToInputs(List<DataDefinitionDeclaration> inputs) {
        for (int i = 0; i < inputs.size(); i++) {
            if (flowOutputs.contains(inputs.get(i).getName())) {
                flowFreeOutputs.remove(inputs.get(i).getName());
                inputs.remove(i);
                i--;
            }
        }
    }
    @Override
    public void validateFlowStructure() throws InvalidPropertiesFormatException {
        // do some validation logic...
        //•	 output(step x) -> input(step y) <=> step y after step x
        // input יכול להיות מוצמד ל Output
        //•	רק אם יש להם שם זהה ואם הם מאותו הסוג (2 התנאים גם יחד)
        //•	ל Input אחד יכול להיות מוצמד output אחד לכל היותר
        //•	ייתכנו output'ים שאינם מוצמדים לשום input'ים. הם ייקראו output'ים חופשיים.
        //•	ייתכנו input'ים שאינם מוצמדים לשום output'ים. הם ייקראו input'ים חופשיים.

        for (StepUsageDeclaration step : steps) {

            List<DataDefinitionDeclaration> inputs = step.getStepDefinition().inputs();
            addOutputsToInputs(inputs);
            addFlowInputs(inputs);

            List<DataDefinitionDeclaration> outputs = step.getStepDefinition().outputs();
            addFlowOutputs(outputs);

        }

    }
    @Override
    public List<DataDefinitionDeclaration> getFlowFreeInputs() {return flowFreeInputs;}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<StepUsageDeclaration> getFlowSteps() {
        return steps;
    }

    @Override
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }

}
