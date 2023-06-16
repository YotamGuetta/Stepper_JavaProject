package stepper.dataStorage;

import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.FlowExecution;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.step.api.DataDefinitionDeclaration;

import java.util.*;

public class stepDetails {
    private final String  name;
    private final String result;
    private final String log;
    private final String summery;
    private final Calendar startTime;
    private final Calendar EndTime;
    private final String runTime;
    private final Map<String, Object> inputs;
    private final Map<String, Object> outputs;

    public stepDetails(StepUsageDeclaration step, FlowExecution flowExecution, StepExecutionContext context, Calendar startTime) {
        Map<String, String> summery = flowExecution.getSummeryOfAFlowRun();
        name = step.getFinalStepName();
        runTime = Long.toString(step.getStepRunTime());
        result = step.getStepResult();
        this.summery = summery.get(step.getFinalStepName());
        this.log = context.getStepLogLines(step.getFinalStepName());


        inputs = new HashMap<>();
        List<DataDefinitionDeclaration> stepInputs = step.getStepDefinition().inputs();
        for (DataDefinitionDeclaration input : stepInputs) {
            Object value = context.GetDataValueAsObject(input.getName(), step.getFinalStepName());

            inputs.put(flowExecution.getFlowDefinition()
                            .GetDataDefinitionAfterAliasing(input.getName(),
                                    step.getFinalStepName()), value);
        }

        outputs = new HashMap<>();
        List<DataDefinitionDeclaration> stepOutputs = step.getStepDefinition().outputs();

        for (DataDefinitionDeclaration output : stepOutputs) {
            Object value = context.GetDataValueAsObject( output.getName(), step.getFinalStepName());

            outputs.put(flowExecution.getFlowDefinition()
                            .GetDataDefinitionAfterAliasing(output.getName(),
                                    step.getFinalStepName()), value);
        }

        this.startTime = startTime;
        EndTime = Calendar.getInstance();
        EndTime.setTimeInMillis(startTime.getTimeInMillis()+ Long.parseLong(runTime));
    }
    public Map<String, Object> GetStepInfo(){
        Map<String,Object> stepInfo = new HashMap<>();
        stepInfo.put("Name", name);
        stepInfo.put("Result", result);
        stepInfo.put("Start Time", startTime.getTime().toString());
        stepInfo.put("Run Time", runTime + "ms");
        stepInfo.put("End Time", EndTime.getTime().toString());
        if(log != null) {
            stepInfo.put("Logs", log);
        }
        if(summery != null) {
            stepInfo.put("Summery", summery);
        }
        return stepInfo;
    }
    public  Map<String,Object> GetStepInputs(){
        return inputs;
    }
    public  Map<String,Object> GetStepOutputs(){
        return outputs;
    }

    public String getName() {
        return name;
    }

    public String getResult() {
        return result;
    }
}
