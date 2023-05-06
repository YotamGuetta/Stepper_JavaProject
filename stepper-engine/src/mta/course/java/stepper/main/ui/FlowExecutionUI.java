package mta.course.java.stepper.main.ui;

import mta.course.java.stepper.flow.execution.FlowExecution;
import mta.course.java.stepper.flow.execution.FlowFullDetails;
import mta.course.java.stepper.step.api.DataCapsule;
import mta.course.java.stepper.step.api.DataCapsuleImpl;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;

import java.util.*;

public class FlowExecutionUI {
    private Scanner scanner;
    private FlowExecution flowExecution;
    private Statistics statistics;
    private FlowExecutionDetails details;

    public FlowExecutionUI( FlowExecution flowExecution, Scanner scanner, Statistics statistics, FlowExecutionDetails details){
        this.flowExecution = flowExecution;
        this.scanner = scanner.useDelimiter("\n");
        this.statistics = statistics;
        this.details = details;
    }
    public boolean GetFreeInputs() {
        boolean executable = false;
        int inputNumber = -1;
        Map<String, DataCapsuleImpl> inputs = flowExecution.getFlowDefinition().getFlowFreeInputs();
        List<String> inputsKeys = new ArrayList<>(inputs.keySet());
        while (true) {
            if (!executable) {
                executable = flowExecution.CheckIfExecutable();
            }
            System.out.println("Pick an input out of the list:");

            System.out.println("1) Start flow");

            int i = 2;
            for (String key : inputsKeys) {
                DataDefinitionDeclaration inputData = inputs.get(key).getDataDefinitionDeclaration();
                System.out.println(i + ") " + inputData.userString() + " = " + flowExecution.getFreeInputs().get(key) + " (Class: " + inputData.dataDefinition().getType().getSimpleName() + ", Necessity: " + inputData.necessity() + ") .");
                i++;
            }
            System.out.println("0) Go back.");
            System.out.println("-1) Show history details");
            System.out.println("-2) Show statistics");

            try {
                inputNumber = scanner.nextInt();
            } catch (InputMismatchException e) {
                String buffer = scanner.next();
                System.out.println("Please enter a valid number");
                continue;
            }
            if (inputNumber == 0) {
                return false;
            }
            if (inputNumber == 1) {
                if (executable)
                    return true;
                else {
                    System.out.println("Not all mandatory inputs are given");
                    continue;
                }
            }
            if(inputNumber == -2){
                statistics.Show();
                continue;
            }
            if(inputNumber == -1){
                details.ChooseDetailsToShow(scanner);
                continue;
            }
            if (inputNumber > inputsKeys.size() + 2 || inputNumber < 0) {
                System.out.println("Please enter a valid choice");
                continue;
            }

            DataCapsuleImpl chosenInput = inputs.get(inputsKeys.get(inputNumber - 2));
            DataDefinitionDeclaration dataDefinition = chosenInput.getDataDefinitionDeclaration();
            if (dataDefinition.dataDefinition().isUserFriendly()) {

                System.out.println("Enter a " + dataDefinition.dataDefinition().getType().getSimpleName() + " :");

                if (dataDefinition.dataDefinition().getType() == Integer.class) {
                    try {
                        int inputInt = scanner.nextInt();
                        flowExecution.addFreeInput(chosenInput.getFinalName(), inputInt);
                    } catch (Exception e) {
                        String buffer = scanner.next();
                        System.out.println("Not a number");
                    }
                }
                if (dataDefinition.dataDefinition().getType() == Double.class) {
                    try {
                        double inputDouble = scanner.nextDouble();
                        flowExecution.addFreeInput(chosenInput.getFinalName(), inputDouble);
                    } catch (Exception e) {
                        String buffer = scanner.next();
                        System.out.println("Not a double");
                    }
                }
                if (dataDefinition.dataDefinition().getType() == String.class) {
                    String inputStr = "";
                    inputStr += scanner.skip("\n").nextLine();
                    flowExecution.addFreeInput(chosenInput.getFinalName(), inputStr);
                }

            }

        }
    }
}
