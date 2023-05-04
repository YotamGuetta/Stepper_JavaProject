package mta.course.java.stepper.main;

import jdk.internal.util.xml.impl.Input;
import mta.course.java.stepper.dd.api.DataDefinition;
import mta.course.java.stepper.dd.impl.DataDefinitionRegistry;
import mta.course.java.stepper.flow.definition.api.FlowDefinition;
import mta.course.java.stepper.flow.definition.api.FlowDefinitionImpl;
import mta.course.java.stepper.flow.definition.api.StepUsageDeclaration;
import mta.course.java.stepper.flow.definition.api.StepUsageDeclarationImpl;
import mta.course.java.stepper.flow.execution.FlowExecution;
import mta.course.java.stepper.flow.execution.FlowFullDetails;
import mta.course.java.stepper.flow.execution.runner.FLowExecutor;
import mta.course.java.stepper.main.ui.FlowExecutionUI;
import mta.course.java.stepper.step.StepDefinitionRegistry;
import mta.course.java.stepper.step.api.DataDefinitionDeclaration;
import mta.course.java.stepper.step.api.DataNecessity;
import mta.course.java.stepper.step.impl.FilesDeleterStep;

import java.sql.Timestamp;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        //Scanner scanner = new Scanner(System.in);
        String fileName="C:\\Users\\yotam\\StepperFileTest\\temp.txt";
        String msg = "Hello World";
        List<FlowFullDetails> details = new ArrayList<>();

        FLowExecutor fLowExecutor = new FLowExecutor();

        FlowDefinition flow3 = new FlowDefinitionImpl("Flow 3", "show two person details");
        flow3.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.SPEND_SOME_TIME,true, "time flies"));
        flow3.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.COLLECT_FILES_IN_FOLDER, "get files 1"));
        flow3.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.FILES_DELETER, "delete file 1"));
        flow3.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.SPEND_SOME_TIME,true, "time flies"));
        //.getFlowFormalOutputs().add("RESULT");
        try {
            flow3.validateFlowStructure();

            FlowExecution flow3Execution1 = new FlowExecution("3", flow3);


            //FlowExecutionUI ui = new FlowExecutionUI(flow3Execution1);
            //ui.GetFreeInputs();
            for(int i=0; i<2; i++) {
                flow3Execution1.addFreeInput("FILTER", ".txt");
                flow3Execution1.addFreeInput("FOLDER_NAME", "C:\\Users\\yotam\\StepperFileTest");
                flow3Execution1.addFreeInput("CONTENT", "hello world");
                flow3Execution1.addFreeInput("FILE_NAME", fileName);
                flow3Execution1.addFreeInput("TIME_TO_SPEND", 0);
                details.add(fLowExecutor.executeFlow(flow3Execution1));
                System.out.println(details.get(i).getHistoryData());
                flow3Execution1.clearFlowData();
            }
        }
        catch (InvalidPropertiesFormatException e){
            System.out.println("ERROR: "+e.getMessage());
        }


    }
}
