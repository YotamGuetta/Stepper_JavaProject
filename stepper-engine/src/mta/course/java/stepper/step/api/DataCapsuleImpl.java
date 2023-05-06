package mta.course.java.stepper.step.api;

public class DataCapsuleImpl implements DataCapsule {
    private final DataDefinitionDeclaration dataDefinitionDeclaration;
    private final String finalName;
    private final String parentStepName;

    public DataCapsuleImpl(DataDefinitionDeclaration dataDefinition, String parentStepName){
        this(dataDefinition, parentStepName, dataDefinition.getName());
    }
    public DataCapsuleImpl(DataDefinitionDeclaration dataDefinition, String parentStepName, String alias){
        this.dataDefinitionDeclaration = dataDefinition;
        finalName = alias;
        this.parentStepName = parentStepName;
    }
    public String getFinalName(){
        return finalName;
    }
    public String getParentStepName(){
        return parentStepName;
    }
    public DataDefinitionDeclaration getDataDefinitionDeclaration(){
        return dataDefinitionDeclaration;
    }
}
