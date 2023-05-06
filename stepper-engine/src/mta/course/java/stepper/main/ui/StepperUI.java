package mta.course.java.stepper.main.ui;

import mta.course.java.stepper.flow.definition.api.FlowDefinition;
import mta.course.java.stepper.flow.definition.api.ReadFlowFromFile;
import mta.course.java.stepper.flow.execution.FlowExecution;
import mta.course.java.stepper.flow.execution.runner.FLowExecutor;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class StepperUI {


    private void printTitle(){
        System.out.println("$$\\      $$\\ $$$$$$$$\\ $$\\       $$$$$$\\   $$$$$$\\  $$\\      $$\\ $$$$$$$$\\ ");
       System.out.println("$$ | $\\  $$ |$$  _____|$$ |     $$  __$$\\ $$  __$$\\ $$$\\    $$$ |$$  _____|");
       System.out.println("$$ |$$$\\ $$ |$$ |      $$ |     $$ /  \\__|$$ /  $$ |$$$$\\  $$$$ |$$ |");
       System.out.println("$$ $$ $$\\$$ |$$$$$\\    $$ |     $$ |      $$ |  $$ |$$\\$$\\$$ $$ |$$$$$\\");
       System.out.println("$$$$  _$$$$ |$$  __|   $$ |     $$ |      $$ |  $$ |$$ \\$$$  $$ |$$  __|");
       System.out.println("$$$  / \\$$$ |$$ |      $$ |     $$ |  $$\\ $$ |  $$ |$$ |\\$  /$$ |$$ | ");
       System.out.println("$$  /   \\$$ |$$$$$$$$\\ $$$$$$$$\\\\$$$$$$  | $$$$$$  |$$ | \\_/ $$ |$$$$$$$$\\");
       System.out.println("\\__/     \\__|\\________|\\________|\\______/  \\______/ \\__|     \\__|\\________|");


       System.out.println("$$$$$$$$\\  $$$$$$\\         $$$$$$\\ $$$$$$$$\\ $$$$$$$$\\ $$$$$$$\\  $$$$$$$\\  $$$$$$$$\\ $$$$$$$\\ ");
       System.out.println("\\__$$  __|$$  __$$\\       $$  __$$\\\\__$$  __|$$  _____|$$  __$$\\ $$  __$$\\ $$  _____|$$  __$$\\");
       System.out.println("  $$ |   $$ /  $$ |      $$ /  \\__|  $$ |   $$ |      $$ |  $$ |$$ |  $$ |$$ |      $$ |  $$ |");
       System.out.println("  $$ |   $$ |  $$ |      \\$$$$$$\\    $$ |   $$$$$\\    $$$$$$$  |$$$$$$$  |$$$$$\\    $$$$$$$  |");
       System.out.println("  $$ |   $$ |  $$ |       \\____$$\\   $$ |   $$  __|   $$  ____/ $$  ____/ $$  __|   $$  __$$< ");
       System.out.println("  $$ |   $$ |  $$ |      $$\\   $$ |  $$ |   $$ |      $$ |      $$ |      $$ |      $$ |  $$ |");
       System.out.println("  $$ |    $$$$$$  |      \\$$$$$$  |  $$ |   $$$$$$$$\\ $$ |      $$ |      $$$$$$$$\\ $$ |  $$ |");
       System.out.println();

    }
    private void printFlowExecutionResult(FlowExecution flowExecution) {

        System.out.println("Flow unique ID:" + flowExecution.getThisRunUniqueID());
        System.out.println("Flow name: " + flowExecution.getFlowDefinition().getName());
        System.out.println("Flow result: " + flowExecution.getFlowExecutionResult());
        System.out.println("Outputs:");

        Map<String, String> formalOutputs = flowExecution.getFlowFormalOutputs();

        for (String key : formalOutputs.keySet()) {
            System.out.println(key + " : " + formalOutputs.get(key));
        }
    }
    public void RunStepper() {
        FLowExecutor flowExecutor = new FLowExecutor();
        ReadFlowFromFile readFlowFromFile = new ReadFlowFromFile();
        Scanner scanner = new Scanner(System.in);
        Statistics statistics = new Statistics();
        FlowExecutionDetails details = new FlowExecutionDetails();
        List<FlowDefinition> flows;
        int flowExecutionIndex = 0;
        while (true) {
            printTitle();
            System.out.println("Please enter an xml file full path to start :");
            System.out.println("If you wish to exit press 0");
            String xmlFile = scanner.nextLine();
            System.out.println(xmlFile);

            if (xmlFile.equals("0"))
                return;

            try {
                 flows = readFlowFromFile.getFlowDefinitions(xmlFile);
            } catch (JAXBException | FileNotFoundException | InvalidPropertiesFormatException e) {
                System.out.println("Invalid file format"+ e.getMessage());
                continue;
            } catch (IOException e) {
                System.out.println("File not found");
                continue;
            }


            while (true) {
                System.out.println("Using: " + xmlFile.substring(0, xmlFile.lastIndexOf(".")));
                System.out.println("Pick a flow to use: ");
                statistics.setStatistics(flows);
                FlowsDetailsUI flowsDetailsUI = new FlowsDetailsUI(flows, scanner, statistics, details);
                FlowDefinition chosenFlow = flowsDetailsUI.PickAFlow();
                if (chosenFlow == null) {
                    break;
                }
                FlowExecution flowExecution = new FlowExecution(flowExecutionIndex + "", chosenFlow);
                flowExecutionIndex++;
                while (true) {

                    FlowExecutionUI flowExecutionUI = new FlowExecutionUI(flowExecution, scanner, statistics, details);

                    if (flowExecutionUI.GetFreeInputs()) {
                        details.addDetails(flowExecutor.executeFlow(flowExecution));
                        printFlowExecutionResult(flowExecution);
                        statistics.addToStatistics(flowExecution);
                        //flowExecution.clearFlowData();
                    } else {
                        break;
                    }
                }

            }
        }
    }
}
