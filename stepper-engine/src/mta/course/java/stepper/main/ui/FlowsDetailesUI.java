package mta.course.java.stepper.main.ui;

import mta.course.java.stepper.flow.definition.api.FlowDefinition;
import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.flow.execution.FlowExecution;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class FlowsDetailesUI {
    private Scanner scanner;
    private List<FlowDefinition> flowDefinitions;
    private Statistics statistics;
    private FlowExecutionDetails details;
    public FlowsDetailesUI(List<FlowDefinition> flowDefinitions, Scanner scanner, Statistics statistics, FlowExecutionDetails details){
        this.flowDefinitions = flowDefinitions;
        this.scanner = scanner;
        this.statistics =statistics;
        this.details = details;
    }
    public FlowDefinition PickAFlow() {
        int userInput;
        while (true) {
            System.out.println("Pick a flow to use: ");
            for (int i = 0; i < flowDefinitions.size(); i++) {
                System.out.println((i+1) + ") Flow: " + flowDefinitions.get(i).getName());
            }
            System.out.println("0) Go back");
            System.out.println("-1) Show history details ");
            System.out.println("-2) Show Statistics");

            try {
                userInput = UtilitiesUI.getNumberFromUser(scanner);
            }
            catch (IndexOutOfBoundsException |NumberFormatException e){
                System.out.println("Please enter a valid number");
                continue;
            }
            if(userInput == 0)
                return null;
            if(userInput == -1){
                details.ChooseDetailsToShow(scanner);
                continue;
            }
            if(userInput == -2){
                statistics.Show();
                continue;
            }
            if(userInput > flowDefinitions.size() || userInput < 0){
                System.out.println("Please enter a valid choice");
                continue;
            }
            FlowDefinition chosenFlow = flowDefinitions.get(userInput - 1);
            showFlowDetails(chosenFlow);
            System.out.println();
            while(true) {
                System.out.println("Choose this flow?");
                System.out.println("1) continue");
                System.out.println("0) Go back");
                try {
                    userInput = UtilitiesUI.getNumberFromUser(scanner);
                }
                catch (IndexOutOfBoundsException |NumberFormatException e){
                    System.out.println("Please enter a valid number");
                    continue;
                }
                if (userInput == 0)
                    break;
                if (userInput == 1)
                    return chosenFlow;

                System.out.println("Please enter a valid choice");
            }

        }
    }
    private void showFlowDetails(FlowDefinition chosenFlow) {
        System.out.println("FLOW DETAILS :");
        System.out.println("    Flow Name: " + chosenFlow.getName());
        System.out.println("    Flow Formal Outputs: ");
        for (String output : chosenFlow.getFlowFormalOutputs())
            System.out.println("        "+output);
        System.out.print("    Is Flow Readonly: ");
        if(chosenFlow.isFlowReadOnly())
            System.out.println("Yes");
        else
            System.out.println("No");

        System.out.println("    Flow Steps: ");
        for (StepUsageDeclaration step : chosenFlow.getFlowSteps()) {
            System.out.print("      Step Name: " + step.getStepDefinition().name());
            if (!step.getStepDefinition().name().equals(step.getFinalStepName())) {
                System.out.println(" (alias: " + step.getFinalStepName() + ")");
            }
            System.out.print("        Is Step Readonly: ");
            if(step.getStepDefinition().isReadonly())
                System.out.println("Yes");
            else
                System.out.println("No");
        }
        System.out.println("    Flow Free Inputs: ");
        for (String inputName : chosenFlow.getFlowFreeInputs().keySet()) {
            System.out.println("        Input Name: " + inputName);
            System.out.println("        Input Type: " + chosenFlow.getFlowFreeInputs().get(inputName).getDataDefinitionDeclaration().dataDefinition().getType().getSimpleName());
            System.out.println("        Input's Step: "+chosenFlow.getFlowFreeInputs().get(inputName).getParentStepName());
            System.out.println();
        }
    }
}
