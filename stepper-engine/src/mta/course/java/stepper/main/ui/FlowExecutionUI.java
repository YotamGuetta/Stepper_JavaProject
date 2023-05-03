package mta.course.java.stepper.main.ui;

import mta.course.java.stepper.flow.execution.FlowExecution;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;

import java.util.List;
import java.util.Scanner;

public class FlowExecutionUI {
    Scanner scanner;
    FlowExecution flowExecution;

    public FlowExecutionUI( FlowExecution flowExecution){
        this.flowExecution = flowExecution;
        this.scanner = new Scanner(System.in).useDelimiter("\n");
    }
    public boolean GetFreeInputs(){
        boolean executable = false;
        int inputNumber = -1;
        List<DataDefinitionDeclaration> inputs = flowExecution.getFlowDefinition().getFlowFreeInputs();
        while(true) {
            if(!executable){
                executable = flowExecution.CheckIfExecutable();
            }
            System.out.println("Pick an input out of the list:");

            System.out.println("1) Start flow");

            for (int i = 0; i < inputs.size(); i++) {
                System.out.println((i + 2) + ") " + inputs.get(i).getName() +"= "+flowExecution.getInputValue(inputs.get(i).getName())+ " (Class: " + inputs.get(i).dataDefinition().getType().getSimpleName() + ", Necessity: "  + inputs.get(i).necessity() + ") .");
            }
            System.out.println("0) Go back.");


            inputNumber = scanner.nextInt();
            if (inputNumber == 0) {
                return false;
            }
            if (inputNumber == 1) {
                if(executable)
                    return true;
                else {
                    System.out.println("Not all mandatory inputs are given");
                    continue;
                }
            }
            DataDefinitionDeclaration chosenInput = inputs.get(inputNumber - 2);
            if (chosenInput.dataDefinition().isUserFriendly()) {

                System.out.println("Enter a " + chosenInput.dataDefinition().getType().getSimpleName() + " :");

                if (chosenInput.dataDefinition().getType() == Integer.class) {
                    int inputInt = scanner.nextInt();
                    flowExecution.addFreeInput(chosenInput.getName(), inputInt);
                }
                if (chosenInput.dataDefinition().getType() == Double.class) {
                    try {
                        double inputDouble = scanner.nextDouble();
                        flowExecution.addFreeInput(chosenInput.getName(), inputDouble);
                    } catch (Exception e) {
                        System.out.println("Not a double");
                    }
                }
                if (chosenInput.dataDefinition().getType() == String.class) {
                    String inputStr="";

                    //inputStr += scanner.nextLine();
                    inputStr += scanner.skip("\n").nextLine();
                   // }
                    //String inputStr = scanner.nextLine();

                    flowExecution.addFreeInput(chosenInput.getName(), inputStr);
                }

            }


        }
    }
}
