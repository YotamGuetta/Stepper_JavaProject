package stepper.flow.Tasks;

import javafx.concurrent.Task;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.ReadFlowFromFile;
import stepper.flow.execution.FlowFullDetails;

import java.util.List;
import java.util.function.Consumer;

public class CollectFlowsDataTask extends Task<Boolean> {
    private  String fileName;
    private  Consumer<FlowDefinition> AddFlow;
    private  Consumer<String> onError;
    private  Consumer<Runnable> onSuccess;
    public CollectFlowsDataTask(String fileName, Consumer<FlowDefinition> AddFlow, Consumer<String> onError, Consumer<Runnable> onSuccess) {
        this.fileName =fileName;
        this.AddFlow = AddFlow;
        this.onError = onError;
        this.onSuccess = onSuccess;

    }

    @Override
    protected Boolean call() throws Exception {
        ReadFlowFromFile readFlowFromFile = new ReadFlowFromFile();
        try{
            List<FlowDefinition> flows = readFlowFromFile.getFlowDefinitions(fileName);
            flows.forEach( (flow) -> AddFlow.accept(flow));
            onSuccess.accept(null);
        }
        catch (Exception e){
            onError.accept(e.getMessage());
        }
        return Boolean.TRUE;
    }
}