package oast.project.model;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Link {
    private int startNodeId;
    private int endNodeId;
    private int numberOfFibrePairs;
    private int fibrePairCost;
    private int numberOfLambdasInFibre;

    public Link(String definingLineStr) {
        List<Integer> linkDefiningValues = Arrays.stream(definingLineStr.split(" "))
                .filter(Predicate.not(String::isEmpty))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        this.startNodeId = linkDefiningValues.get(0);
        this.endNodeId = linkDefiningValues.get(1);
        this.numberOfFibrePairs = linkDefiningValues.get(2);
        this.fibrePairCost = linkDefiningValues.get(3);
        this.numberOfLambdasInFibre = linkDefiningValues.get(4);
    }

    public int getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(int startNodeId) {
        this.startNodeId = startNodeId;
    }

    public int getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(int endNodeId) {
        this.endNodeId = endNodeId;
    }

    public int getNumberOfFibrePairs() {
        return numberOfFibrePairs;
    }

    public void setNumberOfFibrePairs(int numberOfFibrePairs) {
        this.numberOfFibrePairs = numberOfFibrePairs;
    }

    public int getFibrePairCost() {
        return fibrePairCost;
    }

    public void setFibrePairCost(int fibrePairCost) {
        this.fibrePairCost = fibrePairCost;
    }

    public int getNumberOfLambdasInFibre() {
        return numberOfLambdasInFibre;
    }

    public void setNumberOfLambdasInFibre(int numberOfLambdasInFibre) {
        this.numberOfLambdasInFibre = numberOfLambdasInFibre;
    }
}
