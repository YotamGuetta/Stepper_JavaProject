package mta.course.java.stepper.step.api;

import mta.course.java.stepper.dd.api.DataDefinition;

public interface DataDefinitionDeclaration {
    String getName();
    DataNecessity necessity();
    String userString();
    DataDefinition dataDefinition();
}
