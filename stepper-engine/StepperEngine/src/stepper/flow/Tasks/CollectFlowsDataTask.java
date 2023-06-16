package stepper.flow.Tasks;

import javafx.concurrent.Task;
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.definition.api.ReadFlowFromFile;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class CollectFlowsDataTask extends Task<Boolean> {
    private final String fileName;
    private final Consumer<FlowDefinition> AddFlow;
    private final Consumer<Exception> onError;
    private final Consumer<ExecutorService>  onSuccess;
    public CollectFlowsDataTask(String fileName, Consumer<FlowDefinition> AddFlow, Consumer<Exception> onError, Consumer<ExecutorService> onSuccess) {
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
            onSuccess.accept(readFlowFromFile.threadExecutor);
            flows.forEach(AddFlow);
        }
        catch (Exception e){
            onError.accept(e);
        }
        return Boolean.TRUE;
    }
}