package com.langtool.dto;

public class CsvOutput {
    private String output;

    // Default constructor
    public CsvOutput() {}

    // Constructor with parameter
    public CsvOutput(String output) {
        this.output = output;
    }

    // Getter
    public String getOutput() {
        return output;
    }

    // Setter
    public void setOutput(String output) {
        this.output = output;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "CsvOutput{" +
                "output='" + output + '\'' +
                '}';
    }
}