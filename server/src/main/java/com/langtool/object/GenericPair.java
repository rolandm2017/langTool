package com.langtool.object;

public class GenericPair {
    private String en;
    private String fr;

    // Default constructor
    public GenericPair() {
    }

    // Constructor with parameters
    public GenericPair(String en, String fr) {
        this.en = en;
        this.fr = fr;
    }

    // Getters and setters
    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "GenericPair{" +
                "en='" + en + '\'' +
                ", fr='" + fr + '\'' +
                '}';
    }

    // Optionally, you might want to add equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericPair that = (GenericPair) o;
        return java.util.Objects.equals(en, that.en) &&
               java.util.Objects.equals(fr, that.fr);
    }

  
}