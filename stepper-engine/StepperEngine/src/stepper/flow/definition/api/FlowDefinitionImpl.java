package stepper.flow.definition.api;

import javafx.util.Pair;
import stepper.alias.AliasMapping;
import stepper.step.api.DataCapsuleImpl;
import stepper.step.api.DataDefinitionDeclaration;

import java.util.*;

public class FlowDefinitionImpl implements FlowDefinition {

    private final String name;
    private final String description;
    private final List<DataCapsuleImpl> flowOutputs;
    private final List<String> flowFreeOutputs;
    private final Set<String> formalOutputs;
    private final List<StepUsageDeclaration> steps;
    private final Map<String,DataCapsuleImpl> flowFreeInputs;
    private final AliasMapping aliasingMapping;
    private  final Map<Pair<String,String>, Pair<String, String>> customMapping;
    private final Set<DataCapsuleImpl> allDataCapsules;
    private boolean isFlowReadOnly;
    private final Map<String,Map<String,String>> flowContinuationMap;
    private final Map<String,Object> initialValues;

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        flowFreeInputs = new HashMap<>();
        flowFreeOutputs = new ArrayList<>();
        aliasingMapping = new AliasMapping();
        customMapping= new HashMap<>();
        formalOutputs= new HashSet<>();
        allDataCapsules = new HashSet<>();
        flowContinuationMap=new HashMap<>();
        initialValues =new HashMap<>();
        isFlowReadOnly = true;
    }
    public void addCustomMapping(String sourceStep, String sourceData, String targetStep, String targetData){
        customMapping.put(new Pair<>(targetStep, targetData),new Pair<>(sourceStep, sourceData));
    }
    public List<DataCapsuleImpl> getAllDataCapsules(){
        return new ArrayList<>(allDataCapsules);
    }
    public Pair<String, String> getCustomMapping(String targetStep, String targetData){
        return customMapping.get(new Pair<>(targetStep, targetData));
    }
    public void addAliasingMapping(String step, String source, String alias){
        aliasingMapping.addAliasingMapping(step, source, alias);
    }
    public void addFlowOutput(List<String> outputName) {
        formalOutputs.addAll(outputName);
    }

    private void addFlowInputs( List<DataCapsuleImpl> inputs) throws InvalidPropertiesFormatException {
        for(DataCapsuleImpl input : inputs){
            if(input.getDataDefinitionDeclaration().dataDefinition().isUserFriendly()) {
                flowFreeInputs.put(input.getFinalName(), input);
            }
            else
                throw new InvalidPropertiesFormatException("input "+input.getFinalName()+" is not user friendly");
        }
    }
    private void addFlowOutputs( List<DataCapsuleImpl> Outputs) throws  InvalidPropertiesFormatException{
        for (DataCapsuleImpl output : Outputs) {
            if(flowOutputs.contains(output)){
                throw new InvalidPropertiesFormatException("Contains two outputs with the same name");
            }
            flowOutputs.add(output);
            flowFreeOutputs.add(output.getFinalName());
        }

    }
    private DataCapsuleImpl getCapsule(String stepName, String output) {
        for (DataCapsuleImpl data : flowOutputs) {
            if (data.getParentStepName().equals(stepName) && data.getFinalName().equals(output)) {
                return data;
            }
        }
        return null;
    }
    private boolean outputExists(DataCapsuleImpl targetOutput){
        for(DataCapsuleImpl output : flowOutputs){
            String outputName = output.getFinalName();
            String targetOutputName = targetOutput.getFinalName();
            Class<?> outputType = output.getDataDefinitionDeclaration().dataDefinition().getType();
            Class<?> targetType = targetOutput.getDataDefinitionDeclaration().dataDefinition().getType();
            if(outputName.equals(targetOutputName) && outputType == targetType ){
                return true;
            }
        }
        return false;
    }
    private void addOutputsToInputs(List<DataCapsuleImpl> inputs) throws  InvalidPropertiesFormatException {
        for (int i = 0; i < inputs.size(); i++) {
            String stepName = inputs.get(i).getParentStepName();
            DataCapsuleImpl targetOutput = inputs.get(i);
            Pair<String, String> target = new Pair<>(stepName, inputs.get(i).getFinalName());
            if (customMapping.containsKey(target)) {
                Pair<String, String> source = customMapping.get(target);
                targetOutput = getCapsule(source.getKey(), source.getValue());
                if (targetOutput != null && targetOutput.getDataDefinitionDeclaration().dataDefinition().getType() !=
                        inputs.get(i).getDataDefinitionDeclaration().dataDefinition().getType())
                    throw new InvalidPropertiesFormatException("input " + targetOutput.getFinalName() + " doesn't match output " + inputs.get(i).getFinalName() + " type");
            }
            if (outputExists(targetOutput)) {
                flowFreeOutputs.remove(inputs.get(i).getFinalName());
                inputs.remove(i);
                i--;
            }
        }
    }
    private List<DataCapsuleImpl> encapsulateData(List<DataDefinitionDeclaration> dataDefinitions, String stepFinalName){
        List<DataCapsuleImpl> dataCap = new ArrayList<>();
        for(DataDefinitionDeclaration data : dataDefinitions){
            String finalName = aliasingMapping.getDataAliasName(stepFinalName, data.getOriginalName());
            dataCap.add(new DataCapsuleImpl(data, stepFinalName, finalName));
        }
        allDataCapsules.addAll(dataCap);
        return  dataCap;
    }

    @Override
    public void validateFlowStructure() throws InvalidPropertiesFormatException {
        // Step Logic:
        // output(step x) -> input(step y) <=> step y after step x
        // Output can be linked to input only if they have the same name and value
        // One input can be connected to a single output at most
        // Inputs which are not connected to an output are considered free inputs and their values are given from the user
        // Outputs which are not connected to an input are considered free outputs and their values are given to the use
        for (StepUsageDeclaration step : steps) {
            if(isFlowReadOnly && !step.getStepDefinition().isReadonly()){
                isFlowReadOnly = false;
            }

            List<DataDefinitionDeclaration> inputs = new ArrayList<>(step.getStepDefinition().inputs());
            List<DataCapsuleImpl> inputsCap =  encapsulateData(inputs, step.getFinalStepName());
            addOutputsToInputs(inputsCap);
            addFlowInputs(inputsCap);

            List<DataDefinitionDeclaration> outputs = step.getStepDefinition().outputs();
            List<DataCapsuleImpl> outputsCap =  encapsulateData(outputs, step.getFinalStepName());
            addFlowOutputs(outputsCap);

        }
        formalOutputs.addAll(flowFreeOutputs);
    }
    @Override
    public Map<String, DataCapsuleImpl> getFlowFreeInputs() {return flowFreeInputs;}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<StepUsageDeclaration> getFlowSteps() {
        return steps;
    }

    @Override
    public List<String> getFlowFormalOutputs() {
        return new ArrayList<>(formalOutputs);
    }

    @Override
    public List<DataCapsuleImpl> getAllFlowOutputs(){
        return  flowOutputs;
    }
    public Map<Pair<String,String>, Pair<String, String>> getFullCustomMapping(){
        return customMapping;
    }
    @Override
    public boolean isFlowReadOnly(){

        return isFlowReadOnly;
    }
    @Override
    public String GetDataDefinitionAfterAliasing(String dataName, String stepName){
        return aliasingMapping.getDataAliasName(stepName,dataName);
    }
    public  DataCapsuleImpl getOutputDataCapsule(String outputName){
        for (DataCapsuleImpl data : getAllFlowOutputs()){
            if(data.getFinalName().equals(outputName))
                return data;
        }
        return null;
    }
    public void AddFlowContinuation(String targetFlow, Map<String,String> targetMapping){
        flowContinuationMap.put(targetFlow, targetMapping);
    }
    public Map<String,String> GetFlowContinuationMap(String flowName){
        return  flowContinuationMap.get(flowName);
    }
    public Set<String> GetFlowContinuationsFlows(){
        return  flowContinuationMap.keySet();
    }
    public void AddInitialValue(String name, Object Value){
        initialValues.put(name, Value);
    }
    public Map<String,Object> GetInitialValues(){
        return initialValues;
    }
}
