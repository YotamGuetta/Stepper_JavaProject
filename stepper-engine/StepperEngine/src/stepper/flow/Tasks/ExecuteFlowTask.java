package stepper.flow.Tasks;

import javafx.concurrent.Task;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.execution.FlowExecution;
import stepper.flow.execution.runner.FlowExecutor;

import java.util.UUID;

public class ExecuteFlowTask extends Task<Boolean> {
    private  final FlowExecution flowExecution;

    public ExecuteFlowTask(FlowDefinition flowDefinition){
        flowExecution = new FlowExecution(UUID.randomUUID().toString(), flowDefinition);
    }
    @Override
    protected Boolean call() throws Exception {
        return Boolean.TRUE;
    }
}
