package mta.course.java.stepper.main;

import jdk.internal.util.xml.impl.Input;
import mta.course.java.stepper.dd.api.DataDefinition;
import mta.course.java.stepper.dd.impl.DataDefinitionRegistry;
import mta.course.java.stepper.flow.definition.api.FlowDefinition;
import mta.course.java.stepper.flow.definition.api.FlowDefinitionImpl;
import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.flow.definition.api.StepUsageDeclarationImpl;
import mta.course.java.stepper.flow.execution.FlowExecution;
import mta.course.java.stepper.flow.execution.runner.FLowExecutor;
import mta.course.java.stepper.main.ui.FlowExecutionUI;
import mta.course.java.stepper.step.StepDefinitionRegistry;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;
import mta.course.java.stepper.step.api.DataNecessity;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Scanner scanner = new Scanner(System.in);

        FlowDefinition flow1 = new FlowDefinitionImpl("Flow 1", "Hello world");
        flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.HELLO_WORLD));
        flow1.validateFlowStructure();

        FlowDefinition flow2 = new FlowDefinitionImpl("Flow 2", "show two person details");
        flow2.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.HELLO_WORLD));
        flow2.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.PERSON_DETAILS, "Person 1 Details"));
        flow2.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.PERSON_DETAILS, "Person 2 Details"));
        flow2.getFlowFormalOutputs().add("DETAILS");
        flow2.validateFlowStructure();

        FLowExecutor fLowExecutor = new FLowExecutor();

        FlowExecution flow1Execution1 = new FlowExecution("1", flow1);
        // collect all user inputs and store them on the flow execution object

        //fLowExecutor.executeFlow(flow1Execution1);

        FlowExecution flow2Execution1 = new FlowExecution("2", flow2);
        // collect all user inputs and store them on the flow execution object
         //Object test = scanner.;
        try {
            DataDefinition theData = flow2.getFlowSteps().get(2).getStepDefinition().inputs().get(0).dataDefinition();
            theData = flow2.getFlowFreeInputs().get(0).dataDefinition();
            //boolean i =  int.class.isAssignableFrom(test.);
            //System.out.println(i);

            //System.out.println(theData.getType());
        }
        catch (Exception e){

        }
        FlowDefinition flow3 = new FlowDefinitionImpl("Flow 3", "show two person details");
        flow3.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.PERSON_DETAILS, "Person 1 Details"));
        //.getFlowFormalOutputs().add("DETAILS");
        flow3.validateFlowStructure();

        FlowExecution flow3Execution1 = new FlowExecution("3", flow3);


        FlowExecutionUI ui = new FlowExecutionUI(flow3Execution1);
        ui.GetFreeInputs();

        fLowExecutor.executeFlow(flow3Execution1);

    }
}
