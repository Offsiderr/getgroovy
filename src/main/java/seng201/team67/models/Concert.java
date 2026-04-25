package seng201.team67.models;

import seng201.team67.models.questionmodels.Question;

import java.util.List;

public class Concert {

    private List<Question> questions;
    private double maximumRewards;
    private int energy;

    public int getEnergy()
    {
        return energy;
    }

    public void addEnergy(int toAdd)
    {
        energy = energy = toAdd;
    }
}
