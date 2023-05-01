package mta.course.java.stepper.dd.impl.mapping;

import javafx.util.Pair;
import mta.course.java.stepper.dd.api.DataDefinition;

import java.util.HashMap;
import java.util.Map;

public class MappingData<car, cdr> {
    private final Pair<car, cdr> map;

    public MappingData(Pair<car, cdr> map){
        this.map = map;
    }
    public MappingData(car car, cdr cdr ){
        this.map = new Pair<>(car, cdr);
    }

    public car getCar(){
        return map.getKey();
    }
    public cdr getCdr(){
        return map.getValue();
    }
    @Override
    public String toString() {
        return "car: " + getCar().toString() + "\n cdr: " + getCdr().toString();
    }
}
