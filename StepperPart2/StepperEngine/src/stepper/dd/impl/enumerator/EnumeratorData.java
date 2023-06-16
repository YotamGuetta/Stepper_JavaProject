package stepper.dd.impl.enumerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.*;

public class EnumeratorData {
    public static String[] values;
    private  Set<String> stringValues;
    private String currentValue;
    public EnumeratorData(){
        this.stringValues = new HashSet<>(Arrays.asList(values));
    }
    protected  void setStringValues(String[] stringValues){
        this.stringValues = new HashSet<>(Arrays.asList(stringValues));
    }
    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        if(stringValues != null && stringValues.contains(currentValue)) {
            this.currentValue = currentValue;
        }
        else{
            throw new InvalidParameterException("The Enumerator does not contain "+ currentValue);
        }
    }

    @Override
    public String toString() {
        return currentValue;
    }
}
