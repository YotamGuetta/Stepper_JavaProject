package stepper.step.api;

import stepper.dd.api.DataDefinition;

public class DataDefinitionDeclarationImpl implements DataDefinitionDeclaration {

    private String name;
    private  String parentStepName;
    private final DataNecessity necessity;
    private final String userString;
    private final DataDefinition dataDefinition;
    private  final  String originalName;
    public DataDefinitionDeclarationImpl(String name,String parentStepName,  DataNecessity necessity, String userString, DataDefinition dataDefinition) {
        originalName = this.name = name;
        this.necessity = necessity;
        this.userString = userString;
        this.dataDefinition = dataDefinition;
        this.parentStepName = parentStepName;
    }
    @Override
    public String getOriginalName(){
        return originalName;
    }
    @Override
    public void setName(String newName) {
        name = newName;
    }
    @Override
    public void setParentStepName(String parentStepName){
        this.parentStepName = parentStepName;
    }
    @Override
    public String getParentStepName(){
        return  parentStepName;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataNecessity necessity() {
        return necessity;
    }

    @Override
    public String userString() {
        return userString;
    }

    @Override
    public DataDefinition dataDefinition() {
        return dataDefinition;
    }

}
