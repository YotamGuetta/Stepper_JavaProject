package stepper.flow.execution.runner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.util.Pair;
import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.FlowExecution;
import stepper.flow.execution.FlowExecutionResult;
import stepper.dataStorage.FlowFullDetails;
import stepper.flow.execution.context.StepExecutionContext;
import stepper.flow.execution.context.StepExecutionContextImpl;
import stepper.step.api.DataCapsuleImpl;
import stepper.step.api.StepResult;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class FlowExecutor extends Task<Boolean> {
    FlowExecution flowExecution;
    Consumer<List<String>> OnStart;
    Consumer<FlowFullDetails> SaveFlowDetails;
    Consumer<Pair<String,String>> stepUpdate;
    public FlowExecutor(FlowExecution flowExecution, Consumer<List<String>> OnStart,
                        Consumer<FlowFullDetails> SaveFlowDetails, Consumer<Pair<String,String>> stepUpdate){
        this.flowExecution = flowExecution;
        this.OnStart = OnStart;
        this.SaveFlowDetails = SaveFlowDetails;
        this.stepUpdate = stepUpdate;
    }
    public FlowFullDetails executeFlow(FlowExecution flowExecution) {
        long startTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(startTime);
        flowExecution.setTimeStamp(timestamp);
        UUID uuid = UUID.randomUUID();
        flowExecution.setThisRunUniqueID(uuid.toString());

        StepExecutionContext context = new StepExecutionContextImpl(flowExecution.getFlowDefinition()); // actual object goes here...

        FlowExecutionResult result = FlowExecutionResult.SUCCESS;
        StringBuilder logs = new StringBuilder();
        StringBuilder summery = new StringBuilder();
        StepResult stepResult;
        context.assignCustomMapping(flowExecution.getFlowDefinition().getFullCustomMapping());
        for (String key : flowExecution.getFreeInputs().keySet()) {
            try {
                context.storeDataValue(key, "", flowExecution.getFreeInputs().get(key));
            } catch (Exception e) {
                flowExecution.setFlowExecutionResult(FlowExecutionResult.FAILURE);
                return new FlowFullDetails();
            }
        }
        // start actual execution
        for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
            StepUsageDeclaration stepUsageDeclaration = flowExecution.getFlowDefinition().getFlowSteps().get(i);

            long singleStepRunTime = System.currentTimeMillis();

            try {
                stepResult = stepUsageDeclaration.getStepDefinition().invoke(context, stepUsageDeclaration.getFinalStepName());
            } catch (Exception e) {
                stepResult = StepResult.FAILURE;
                if (!stepUsageDeclaration.skipIfFail()) {
                    break;
                }
            }
            singleStepRunTime = System.currentTimeMillis() - singleStepRunTime;
            stepUsageDeclaration.setStepRunTime(singleStepRunTime);
            stepUsageDeclaration.setStepResult(stepResult.toString());

            String stepSummery = stepUsageDeclaration.getStepDefinition().getSummery();
            if (!(stepSummery == null))
                summery.append(stepSummery).append("\n");

            if (stepResult == StepResult.FAILURE) {
                result = FlowExecutionResult.FAILURE;
                if (!stepUsageDeclaration.skipIfFail()) {
                    break;
                }
            }
            if (stepResult == StepResult.WARNING) {
                result = FlowExecutionResult.WARNING;
            }
        }

        flowExecution.storeSummeryOfAFlowRun(flowExecution.getUniqueId(), summery.toString());
        flowExecution.setFlowExecutionResult(result);
        try {
            List<String> outputsNames = flowExecution.getFlowDefinition().getFlowFormalOutputs();
            List<String> formalOutputs = context.getAllOutputs(outputsNames);

            for (int i = 0; i < outputsNames.size(); i++) {
                DataCapsuleImpl outputData = flowExecution.getFlowDefinition().getOutputDataCapsule(outputsNames.get(i));
                if (outputData != null) {
                    flowExecution.storeFormalOutput(outputData.getDataDefinitionDeclaration().userString(), formalOutputs.get(i));
                } else
                    throw new Exception("Failed to get output");
            }
        } catch (Exception e) {
            System.out.println("Failed to get outputs");
        }
        long endTime = System.currentTimeMillis();
        flowExecution.setFlowRunTime(endTime - startTime);
        FlowFullDetails details = new FlowFullDetails();
        details.SaveData(flowExecution, context);
        return details;
    }

    @Override
    protected Boolean call() throws Exception {
        long startTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(startTime);
        flowExecution.setTimeStamp(timestamp);
        UUID uuid = UUID.randomUUID();
        flowExecution.setThisRunUniqueID(uuid.toString());

        OnStart.accept(new ArrayList<>(Arrays.asList(flowExecution.getFlowDefinition().getName(), uuid.toString(), timestamp.toString())));
        StepExecutionContext context = new StepExecutionContextImpl(flowExecution.getFlowDefinition()); // actual object goes here...

        FlowExecutionResult result = FlowExecutionResult.SUCCESS;
        StringBuilder logs = new StringBuilder();
        StringBuilder summery = new StringBuilder();
        StepResult stepResult;
        context.assignCustomMapping(flowExecution.getFlowDefinition().getFullCustomMapping());
        for (String key : flowExecution.getFreeInputs().keySet()) {
            try {
                context.storeDataValue(key, "", flowExecution.getFreeInputs().get(key));
            } catch (Exception e) {
                flowExecution.setFlowExecutionResult(FlowExecutionResult.FAILURE);
                return Boolean.FALSE;
            }
        }
        // start actual execution
        for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
            StepUsageDeclaration stepUsageDeclaration = flowExecution.getFlowDefinition().getFlowSteps().get(i);
            stepResult = StepResult.FAILURE;

            updateTitle(stepUsageDeclaration.getFinalStepName());

            String stepName = stepUsageDeclaration.getFinalStepName();

            long singleStepRunTime = System.currentTimeMillis();

            try {
                stepResult = stepUsageDeclaration.getStepDefinition().invoke(context, stepUsageDeclaration.getFinalStepName());
            } catch (Exception e) {
                if (!stepUsageDeclaration.skipIfFail()) {
                    for(; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++){
                        stepUsageDeclaration = flowExecution.getFlowDefinition().getFlowSteps().get(i);
                        updateTitle(stepUsageDeclaration.getFinalStepName());

                        final String stepResultAsFinal = stepResult.toString();
                        Platform.runLater(
                                () -> stepUpdate.accept(new Pair<>(stepName, stepResultAsFinal))
                        );
                    }
                    break;
                }
            }

            final String stepResultAsFinal = stepResult.toString();
            Platform.runLater(
                    () -> stepUpdate.accept(new Pair<>(stepName, stepResultAsFinal))
            );

            singleStepRunTime = System.currentTimeMillis() - singleStepRunTime;
            stepUsageDeclaration.setStepRunTime(singleStepRunTime);
            stepUsageDeclaration.setStepResult(stepResult.toString());

            logs.append(context.getStepLogLines(stepUsageDeclaration.getFinalStepName()));
            flowExecution.storeLogsOfAFlowRun(logs.toString());

            String stepSummery = stepUsageDeclaration.getStepDefinition().getSummery();
            if (!(stepSummery == null))
                summery.append(stepSummery).append("\n");

            if (stepResult == StepResult.FAILURE) {
                result = FlowExecutionResult.FAILURE;
                if (!stepUsageDeclaration.skipIfFail()) {
                    break;
                }
            }
            if (stepResult == StepResult.WARNING) {
                result = FlowExecutionResult.WARNING;
            }
        }
        flowExecution.storeSummeryOfAFlowRun(flowExecution.getUniqueId(), summery.toString());
        flowExecution.setFlowExecutionResult(result);
        try {
            List<String> outputsNames = flowExecution.getFlowDefinition().getFlowFormalOutputs();
            List<String> formalOutputs = context.getAllOutputs(outputsNames);

            for (int i = 0; i < outputsNames.size(); i++) {
                DataCapsuleImpl outputData = flowExecution.getFlowDefinition().getOutputDataCapsule(outputsNames.get(i));
                if (outputData != null) {
                    flowExecution.storeFormalOutput(outputData.getDataDefinitionDeclaration().userString(), formalOutputs.get(i));
                } else
                    throw new Exception("Failed to get output");
            }
        } catch (Exception e) {
            System.out.println("Failed to get outputs");
        }
        long endTime = System.currentTimeMillis();
        flowExecution.setFlowRunTime(endTime - startTime);
        FlowFullDetails details = new FlowFullDetails();
        details.SaveData(flowExecution, context);
        Platform.runLater(
                () -> SaveFlowDetails.accept(details)
        );
        return Boolean.TRUE;
    }
}
