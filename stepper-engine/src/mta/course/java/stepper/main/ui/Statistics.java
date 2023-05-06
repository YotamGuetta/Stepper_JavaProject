package mta.course.java.stepper.main.ui;

import mta.course.java.stepper.flow.definition.api.FlowDefinition;
import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.flow.execution.FlowExecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {

    private final Map<String, List<Long>> flowStatistics;
    private final Map<String, List<Long>> stepStatistics;

    public Statistics(){
        flowStatistics = new HashMap<>();
        stepStatistics = new HashMap<>();
    }
    public void setStatistics(List<FlowDefinition> flows){
        for (FlowDefinition flow : flows){
            flowStatistics.put(flow.getName(), new ArrayList<>(2));
            flowStatistics.get(flow.getName()).add((long)0);
            flowStatistics.get(flow.getName()).add((long)0);
            for (StepUsageDeclaration step : flow.getFlowSteps()){
                stepStatistics.put(step.getFinalStepName(), new ArrayList<>(2));
                stepStatistics.get(step.getFinalStepName()).add((long)0);
                stepStatistics.get(step.getFinalStepName()).add((long)0);
            }
        }

    }
    public void Show(){
        System.out.println("Statistics:");
        System.out.println("Flows: ");
        for (String key : flowStatistics.keySet()){
            System.out.println("    Flow name: "+key);
            System.out.print("    Flow average runtime: ");
            if(flowStatistics.get(key).get(0) != 0)
                System.out.println( ((double)flowStatistics.get(key).get(1) / flowStatistics.get(key).get(0))+"ms");
            else
                System.out.println(0.0);
        }System.out.println("Steps: ");
        for (String key : stepStatistics.keySet()){
            System.out.println("    Step name: "+key);
            System.out.print("    Step average runtime: ");
            if(stepStatistics.get(key).get(0) != 0)
                System.out.println( ((double)stepStatistics.get(key).get(1) / stepStatistics.get(key).get(0)) +"ms");
            else
                System.out.println(0.0);
        }
    }
    public void addToStatistics(FlowExecution flowExecution){
        List<Long> flowStat =  flowStatistics.get(flowExecution.getFlowDefinition().getName());
        flowStat.set(0, flowStat.get(0)+1);
        flowStat.set(1, flowStat.get(0)+flowExecution.getFlowRunTime());
        for(StepUsageDeclaration step : flowExecution.getFlowDefinition().getFlowSteps()) {
            List<Long> stepStat =  stepStatistics.get(step.getFinalStepName());
            stepStat.set(0, stepStat.get(0)+1);
            stepStat.set(1, stepStat.get(0)+step.getStepRunTime());
        }
    }

}
