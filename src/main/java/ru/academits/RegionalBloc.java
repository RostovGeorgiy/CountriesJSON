package ru.academits;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionalBloc {
    private String acronym;
    private String name;
    private List<String> otherAcronyms;
    private List<String> otherNames;

    public RegionalBloc() {}

    // Геттеры и сеттеры
    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOtherAcronyms() {
        return otherAcronyms;
    }

    public void setOtherAcronyms(List<String> otherAcronyms) {
        this.otherAcronyms = otherAcronyms;
    }

    public List<String> getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(List<String> otherNames) {
        this.otherNames = otherNames;
    }
}
