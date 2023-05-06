package mta.course.java.stepper.alias;


import java.util.HashMap;
import java.util.Map;

public class AliasMapping {
    private static final Map<String, Map<String,String>> aliasingMappingByStep = new HashMap<>();

    public void addAliasingMapping(String step, String source, String alias){

        if(!aliasingMappingByStep.containsKey(step))
            aliasingMappingByStep.put(step,new HashMap<>());
        aliasingMappingByStep.get(step).put(source, alias);

    }
    public String getDataAliasName(String step, String source){
        String dataName;
        if(aliasingMappingByStep.containsKey(step)) {
            dataName = aliasingMappingByStep.get(step).get(source);
            if(dataName != null){
                return dataName;
            }
        }

        return  source;
    }

}
