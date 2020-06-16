package oast.project.algorithm;

public class GeneticAlgorithmParams {
    private float probabilityOfCrossing;
    private float probabilityOfMutation;
    private int maxTime;
    private int numberOfChromosomes;
    private float percentOfBestChromosomes;
    private int maxNumberOfGenerations;
    private int maxMutationNumber;
    private int maxNumberOfContinuousNonBetterSolutions;
    private int seed;

    public float getProbabilityOfCrossing() {
        return probabilityOfCrossing;
    }

    public void setProbabilityOfCrossing(float probabilityCross) {
        this.probabilityOfCrossing = probabilityCross;
    }

    public float getProbabilityOfMutation() {
        return probabilityOfMutation;
    }

    public void setProbabilityOfMutation(float probabilityOfMutation) {
        this.probabilityOfMutation = probabilityOfMutation;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getNumberOfChromosomes() {
        return numberOfChromosomes;
    }

    public void setNumberOfChromosomes(int numberOfChromosomes) {
        this.numberOfChromosomes = numberOfChromosomes;
    }

    public float getPercentOfBestChromosomes() {
        return percentOfBestChromosomes;
    }

    public void setPercentOfBestChromosomes(float percentOfBestChromosomes) {
        this.percentOfBestChromosomes = percentOfBestChromosomes;
    }

    public int getMaxNumberOfGenerations() {
        return maxNumberOfGenerations;
    }

    public void setMaxNumberOfGenerations(int maxNumberOfGenerations) {
        this.maxNumberOfGenerations = maxNumberOfGenerations;
    }

    public int getMaxMutationNumber() {
        return maxMutationNumber;
    }

    public void setMaxMutationNumber(int maxMutationNumber) {
        this.maxMutationNumber = maxMutationNumber;
    }

    public int getMaxNumberOfContinuousNonBetterSolutions() {
        return maxNumberOfContinuousNonBetterSolutions;
    }

    public void setMaxNumberOfContinuousNonBetterSolutions(int maxNumberOfContinuousNonBetterSolutions) {
        this.maxNumberOfContinuousNonBetterSolutions = maxNumberOfContinuousNonBetterSolutions;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}
