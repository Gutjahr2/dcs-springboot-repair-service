package de.dicos.springboot.repairservice.restful.model;

public enum CarModel {
    AUDI_A3("Audi A3"),
    AUDI_A4("Audi A4"),
    AUDI_A6("Audi A6"),
    AUDI_Q3("Audi Q3"),
    AUDI_Q5("Audi Q5"),
    AUDI_Q7("Audi Q7"),
    BMW_3_SERIES("BMW 3 Series"),
    BMW_5_SERIES("BMW 5 Series"),
    BMW_7_SERIES("BMW 7 Series"),
    BMW_X1("BMW X1"),
    BMW_X3("BMW X3"),
    BMW_X5("BMW X5");

    private final String csvLabel;

    CarModel(String csvLabel) {
        this.csvLabel = csvLabel;
    }

    public String getCsvLabel() {
        return csvLabel;
    }
}
