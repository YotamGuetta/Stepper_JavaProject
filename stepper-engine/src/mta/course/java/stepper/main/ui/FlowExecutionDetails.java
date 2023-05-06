package mta.course.java.stepper.main.ui;

import mta.course.java.stepper.flow.execution.FlowFullDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlowExecutionDetails {
    final List<FlowFullDetails> details;
    public FlowExecutionDetails(){
        details = new ArrayList<>();
    }
    public void addDetails(FlowFullDetails details){
        this.details.add(details);
    }
    public void ChooseDetailsToShow(Scanner scanner){
        int input;
        while (true) {
            System.out.println("Pick a past action:");
            for (int i = 0; i < details.size(); i++) {
                System.out.println("Option " + (i + 1));
                System.out.println("Flow name: " + details.get(i).getFlowName());
                System.out.println("Flow unique ID: " + details.get(i).getUuid());
                System.out.println("Flow start time: " + details.get(i).getStartTime());
                System.out.println();
            }
            System.out.println("Option 0 - Go back");
            try {
                input= scanner.nextInt();
            } catch (Exception e) {
                String buffer = scanner.next();
                System.out.println("Please enter a valid choice");
                continue;
            }
            if(input == 0){
                return;
            }
            if(input < 0 || input > details.size()){
                System.out.println("input out of range");
                continue;
            }
            FlowFullDetails choice = details.get(input-1);
            System.out.println(choice.getHistoryData());
            System.out.println();
        }
    }
}
