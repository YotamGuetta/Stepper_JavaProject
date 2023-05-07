package mta.course.java.stepper.flow.definition.api;

import mta.course.java.stepper.schema.*;
import mta.course.java.stepper.step.StepDefinitionRegistry;
import mta.course.java.stepper.step.api.StepDefinition;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ReadFlowFromFile {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "mta.course.java.stepper.schema";
    private final Set<String> flowsNames;
    public ReadFlowFromFile(){
        flowsNames = new HashSet<>();
    }
    private  STStepper deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (STStepper) u.unmarshal(in);
    }
    private  Map<String, StepDefinition> stepNameToDefinitionMap(){
        Map<String, StepDefinition> NameToDef = new HashMap<>();
        for(Object value : StepDefinitionRegistry.values()){
            // StepDefinition curr = StepDefinitionRegistry.valueOf(value.toString());
            NameToDef.put(StepDefinitionRegistry.valueOf(value.toString()).getName(), StepDefinitionRegistry.valueOf(value.toString()));
        }
        return NameToDef;
    }
    private  boolean isXMLFile(String xmlFile){
        if(xmlFile !=null) {
            int index = xmlFile.lastIndexOf('.');
            if (index > 0) {
                String extension = xmlFile.substring(index + 1);
                return extension.equals("xml");
            }
        }
        return false;
    }
    public  List<FlowDefinition> getFlowDefinitions(String xmlFile) throws IOException, JAXBException, NullPointerException {
        // Check if file is xml
        if(!isXMLFile(xmlFile))
            throw new InvalidPropertiesFormatException("File is not an xml");

        // Get File as schema
        InputStream inputStream = Files.newInputStream(Paths.get(xmlFile));
        STStepper stepper = deserializeFrom(inputStream);
        List<STFlow> flowsSchema = stepper.getSTFlows().getSTFlow();

        Map<String, StepDefinition> stepToDefinition = stepNameToDefinitionMap();
        List<FlowDefinition> flows = new ArrayList<>();

        int flowIndex = 0;
        for(STFlow flowSchema : flowsSchema){
            String flowName = flowSchema.getName();
            if(flowsNames.contains(flowName)){
                throw new InvalidPropertiesFormatException("Duplicate flow name: "+flowName);
            }
            flowsNames.add(flowName);

            String description = flowSchema.getSTFlowDescription();
            flows.add(new FlowDefinitionImpl(flowName, description));

            List<String> outputs = Arrays.asList(flowSchema.getSTFlowOutput().split(","));
            // Check if outputs has duplicates
            Set<String> outputSet = new HashSet<>(outputs);
            if(outputSet.size() < outputs.size()){
                throw new InvalidPropertiesFormatException("Duplicate outputNames names");
            }
            flows.get(flowIndex).addFlowOutput(outputs);

            List<STStepInFlow> stepsSchema = flowSchema.getSTStepsInFlow().getSTStepInFlow();
            for(STStepInFlow stepSchema : stepsSchema) {
                String stepAlias;
                Boolean stepCIF = false;
                String stepName = stepSchema.getName();

                if (stepSchema.getAlias() == null)
                    stepAlias = stepSchema.getName();
                else
                    stepAlias = stepSchema.getAlias();

                if (stepSchema.isContinueIfFailing() != null)
                    stepCIF = stepSchema.isContinueIfFailing();

                if(!stepToDefinition.containsKey(stepName))
                    throw  new InvalidPropertiesFormatException("Step "+stepName+"does not exist");
                flows.get(flowIndex).getFlowSteps().add(new StepUsageDeclarationImpl(stepToDefinition.get(stepName), stepCIF, stepAlias));
            }
            if(flowSchema.getSTFlowLevelAliasing() != null) {
                List<STFlowLevelAlias> aliasSchema = flowSchema.getSTFlowLevelAliasing().getSTFlowLevelAlias();
                for (STFlowLevelAlias alias : aliasSchema) {
                    String aliasStep = alias.getStep();
                    String aliasSource = alias.getSourceDataName();
                    String aliasAlias = alias.getAlias();
                    flows.get(flowIndex).addAliasingMapping(aliasStep, aliasSource, aliasAlias);
                }
            }

            if(flowSchema.getSTCustomMappings() != null) {
                List<STCustomMapping> customMappings = flowSchema.getSTCustomMappings().getSTCustomMapping();
                for (STCustomMapping customMapping : customMappings) {
                    String sourceStep = customMapping.getSourceStep();
                    String sourceData = customMapping.getSourceData();
                    String targetStep = customMapping.getTargetStep();
                    String targetData = customMapping.getTargetData();
                    flows.get(flowIndex).addCustomMapping(sourceStep, sourceData, targetStep, targetData);
                }
            }

            flows.get(flowIndex).validateFlowStructure();

            flowIndex++;

        }
        return flows;
    }
}
