package oast.project.algorithm;

import oast.project.model.Demand;
import oast.project.model.Network;
import oast.project.model.P;
import oast.project.model.Solution;

import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithm extends Algorithm{

    private Network network;
    private int maxTime;
    private float probabilityOfCrossing;
    private float probabilityOfMutation;
    private float percentOfBestChromosomes;
    private int numberOfChromosomes;
    private Random random;

    private long endTime;
    private int maxNumberOfContinuousNonBetterSolutions;
    private int maxNumberOfGenerations;
    private int maxMutationNumber;

    private int currentGeneration;
    private int currentMutation;
    private int currentNumberOfContinuousNonBetterSolutions;

    public GeneticAlgorithm(GeneticAlgorithmParams params, Network network)
    {
        this.probabilityOfCrossing =params.getProbabilityOfCrossing();
        this.maxTime=params.getMaxTime();
        this.probabilityOfMutation = params.getProbabilityOfMutation();
        this.numberOfChromosomes = params.getNumberOfChromosomes();
        this.percentOfBestChromosomes = params.getPercentOfBestChromosomes();
        this.maxNumberOfGenerations = params.getMaxNumberOfGenerations();
        this.maxMutationNumber = params.getMaxMutationNumber();
        this.maxNumberOfContinuousNonBetterSolutions = params.getMaxNumberOfContinuousNonBetterSolutions();
        this.random = new Random(params.getSeed());
        this.network = network;

        this.currentGeneration = 0;
        this.currentNumberOfContinuousNonBetterSolutions = 0;
        this.currentMutation = 0;
    }

    @Override
    public Solution runDDAP()
    {
        List<Solution> population = generateInitialPopulation(numberOfChromosomes);
        Solution bestSolution = new Solution(new HashMap<>());
        bestSolution.setNetworkCost(Double.MAX_VALUE);

        endTime = System.nanoTime() + maxTime * 1000000;
        while (checkCondition())
        {
            currentGeneration++;
            System.out.println("Obecna iteracja: " + currentGeneration);
            Solution bestSolutionOfGeneration = new Solution(new HashMap<>());
            bestSolutionOfGeneration.setNetworkCost(Double.MAX_VALUE);

            for (int i = 0; i < population.size(); i++) {
                double cost = 0;
                List<Integer> costsOfLinks = population.get(i).getLinkCapacities();

                for (int j = 0; j < population.get(i).getLinkCapacities().size(); j++)
                    cost += network.getLinks().get(j).getFibrePairCost() * costsOfLinks.get(j);

                if(population.get(i).getLinkCapacities().stream().mapToInt(Integer::intValue).sum() == 0)
                    cost = Double.MAX_VALUE;
                population.get(i).setNetworkCost(cost);


                if (population.get(i).getNetworkCost() < bestSolutionOfGeneration.getNetworkCost())
                    bestSolutionOfGeneration = population.get(i);

            }
            if (bestSolutionOfGeneration.getNetworkCost() < bestSolution.getNetworkCost()) {
                bestSolution = bestSolutionOfGeneration;
                currentNumberOfContinuousNonBetterSolutions = 0;
            }
            else
                currentNumberOfContinuousNonBetterSolutions++;

            population = getBestDDAP(population, percentOfBestChromosomes);
            population = crossover(population, probabilityOfCrossing);
            population = runMutate(population, probabilityOfMutation);
            population = setCapacitiesForNewResult(population);

            System.out.println("Koszt generacji: " + bestSolutionOfGeneration.getNetworkCost());
            System.out.println("Koszt najlepszego rozwiązania: " + bestSolution.getNetworkCost());

        }
        System.out.println("Koszt najlepszego rozwiązania: " + bestSolution.getNetworkCost());
        return bestSolution;
    }

    @Override
    public Solution runDAP()
    {
        List<Solution> population = generateInitialPopulation(numberOfChromosomes);

        var bestSolution = new Solution(new HashMap<>());
        bestSolution.setCapacityExceededLinks(Integer.MAX_VALUE);

        endTime = System.nanoTime() + maxTime * 1000000;
        while (checkCondition()) {
            currentGeneration++;
            Solution bestSolutionOfGeneration = new Solution(new HashMap<>());
            bestSolutionOfGeneration.setCapacityExceededLinks(Integer.MAX_VALUE);

            for (Solution solution : population) {
                int k = 0;
                for (int j = 0; j < solution.getLinkCapacities().size(); j++)
                    if (Math.max(0, solution.getLinkCapacities().get(j) - network.getLinks().get(j).getNumberOfFibrePairs()) > 0)
                        k++;

                solution.setCapacityExceededLinks(k);

                if (solution.getCapacityExceededLinks() < bestSolutionOfGeneration.getCapacityExceededLinks())
                    bestSolutionOfGeneration = solution;
            }

            if (bestSolutionOfGeneration.getCapacityExceededLinks() < bestSolution.getCapacityExceededLinks()) {
                bestSolution = bestSolutionOfGeneration;
                currentNumberOfContinuousNonBetterSolutions = 0;
                if (bestSolution.getCapacityExceededLinks() == 0) return bestSolution;
            }
            else
                currentNumberOfContinuousNonBetterSolutions++;

            population = getBestDAP(population, percentOfBestChromosomes);
            population = crossover(population, probabilityOfCrossing);
            population = runMutate(population, probabilityOfMutation);
            population = setCapacitiesForNewResult(population);

            System.out.println("Przeciążenie generacji " + currentGeneration + ": " + bestSolutionOfGeneration.getCapacityExceededLinks());
        }
        System.out.println("Przeciążenie najlepszego rozwiązania: " + bestSolution.getCapacityExceededLinks());
        return bestSolution;
    }

    public int getCurrentGeneration(){
        return currentGeneration;
    }

    private boolean checkCondition() {
        if (System.nanoTime() >= this.endTime)
            return false;

        if (this.currentGeneration >= this.maxNumberOfGenerations)
            return false;

        if (this.currentMutation >= this.maxMutationNumber)
            return false;

        if (this.currentNumberOfContinuousNonBetterSolutions >= this.maxNumberOfContinuousNonBetterSolutions)
            return false;

        return true;
    }

    private List<Solution> setCapacitiesForNewResult(List<Solution> solutions)
    {

        List<List<Integer>> linksCapacities = new ArrayList<>();
        for (Solution solution: solutions)
            linksCapacities.add(calculateLinksCapacities(network, solution));

        for (int i = 0; i < solutions.size(); i++)
            if (solutions.get(i).getLinkCapacities() == null || solutions.get(i).getLinkCapacities().size() == 0)
                solutions.get(i).setLinkCapacities(linksCapacities.get(i));

        return solutions;
    }

    private List<Solution> generateInitialPopulation(int numberOfChromosomes) {
        List<List<Solution>> allCombinations = new ArrayList<>();
        for(var demand : network.getDemands())
            allCombinations.add(getCombinationsOfOneDemand(demand));

        List<Solution> routingPossibilities = new ArrayList<>();

        for(int i=0; i<numberOfChromosomes; i++) {
            Solution chromosome = new Solution(new HashMap<>());
            Iterator<List<Solution>> iterator = allCombinations.iterator();
            while(iterator.hasNext()){
                List<Solution> solution = iterator.next();
                if (solution.size() == 0)
                    iterator.remove();
                else
                    for (var entry : solution.get(random.nextInt(solution.size())).getXesMap().entrySet())
                        chromosome.getXesMap().put(entry.getKey(), entry.getValue());
            }
            routingPossibilities.add(chromosome);
        }

        List<List<Integer>> linksCapacities = new ArrayList<>();

        for(var possibility: routingPossibilities){
            linksCapacities.add(calculateLinksCapacities(network, possibility));
        }

        List<Solution> list = new ArrayList<>();

        for (int i = 0; i < numberOfChromosomes; i++) {
            int rand = random.nextInt(routingPossibilities.size());
            routingPossibilities.get(rand).setLinkCapacities(linksCapacities.get(rand));
            list.add(routingPossibilities.get(rand));
        }

        System.out.println("list.size(): " + list.size());
        return list;
    }

    private List<List<Integer>> getCombinations(int sum, int numberOfElements) {
        List<List<Integer>> combinations = new ArrayList<>();
        List<Integer> singleCombination = new ArrayList<>();

        for (int i = 0; i <= sum; i++)
            singleCombination.add(i);

        for (int i = 0; i < numberOfElements; i++)
            combinations.add(singleCombination);

        return cartesianProduct(combinations).stream().filter(element -> element.stream().mapToInt(Integer::intValue).sum() == sum).collect(Collectors.toList());
    }


    private List<Solution> getCombinationsOfOneDemand(Demand demand) {
        List<Solution> list = new ArrayList<>();
        var numberOfCombinations = getBinaryCoefficient(demand.getNumberOfPaths() + demand.getVolume() - 1, demand.getVolume());
        List<List<Integer>> combinations = getCombinations(demand.getVolume(), demand.getNumberOfPaths());

        for (int i = 0; i < numberOfCombinations; i++) {
            Map<P, Integer> mapOfValuesForOneDemand = new HashMap<>();

            for (int j = 0; j < demand.getNumberOfPaths(); j++)
            {
                int pathId = demand.getPaths().get(j).getId();
                mapOfValuesForOneDemand.put(new P(demand.getId(), pathId), combinations.get(i).get(pathId - 1));
            }
            list.add(new Solution(mapOfValuesForOneDemand));
        }
        return list;
    }


    private List<Solution> getBestDAP(List<Solution> solutions, float percentOfBestChromosomes)
    {
        int subListEnd = (int)(solutions.size() * (percentOfBestChromosomes / 100));

        List<Solution> list0 = solutions.stream().sorted(Comparator.comparing(Solution::getCapacityExceededLinks)).collect(Collectors.toList());
        List<Solution> list = solutions.stream().sorted(Comparator.comparing(Solution::getCapacityExceededLinks)).collect(Collectors.toList()).subList(0, subListEnd);
        list.addAll(list0.subList(0, solutions.size() - subListEnd));

        return list;

    }

    private List<Solution> getBestDDAP(List<Solution> solutions, float percentOfBestChromosomes) {
        int subListEnd = (int)(solutions.size() * (percentOfBestChromosomes / 100));

        List<Solution> list0 = solutions.stream().sorted(Comparator.comparing(Solution::getNetworkCost)).collect(Collectors.toList());
        List<Solution> list = solutions.stream().sorted(Comparator.comparing(Solution::getNetworkCost)).collect(Collectors.toList()).subList(0, subListEnd);
        list.addAll(list0.subList(0, solutions.size() - subListEnd));

        return list;
    }

    private List<Solution> crossover(List<Solution> parents, float probabilityOfCrossover) {
        List<Solution> children = new ArrayList<>();

        int parentsSize = parents.size();
        for (int i = 0; i < parentsSize / 2; i++) {
            int index1 = random.nextInt(parents.size());
            Solution solution1 = parents.remove(index1);
            int index2 = random.nextInt(parents.size());
            Solution solution2 = parents.remove(index2);

            children.addAll(crossParents(solution1, solution2, probabilityOfCrossover));
        }
        return children;
    }

    private List<Solution> crossParents(Solution parent0, Solution parent1, float probabilityOfCrossover) {
        double rand = random.nextDouble();
        List<Solution> children = new ArrayList<>();

        if (rand < probabilityOfCrossover) {

            children.add(new Solution(new HashMap<>()));
            children.add(new Solution(new HashMap<>()));

            for (int i = 0; i < parent0.getNumberOfGenes(); i++) {
                rand = random.nextDouble();
                if (rand > 0.5){
                    for(var entry: parent0.getGene(i+1).entrySet())
                        children.get(0).getXesMap().put(entry.getKey(),entry.getValue());

                    for(var entry: parent1.getGene(i+1).entrySet())
                        children.get(1).getXesMap().put(entry.getKey(),entry.getValue());

                } else {
                    for(var entry: parent0.getGene(i+1).entrySet())
                        children.get(1).getXesMap().put(entry.getKey(),entry.getValue());

                    for(var entry: parent1.getGene(i+1).entrySet())
                        children.get(0).getXesMap().put(entry.getKey(),entry.getValue());
                }
            }
            return children;
        }
        List<Solution> solutions = new ArrayList<>();
        solutions.add(parent0);
        solutions.add(parent1);
        return solutions;
    }

    private List<Solution> runMutate(List<Solution> population, float probabilityOfMutation) {
        List<Solution> mutants = new ArrayList<>();

        double rand = random.nextDouble();

        for (Solution solution : population) {
            if (rand < probabilityOfMutation) {
                currentMutation++;
                Map<P, Integer> genes = new HashMap<>();
                for (int j = 0; j < solution.getNumberOfGenes(); j++) {
                    for (var entry : mutateGene(solution.getGene(j + 1)).entrySet())
                        genes.put(entry.getKey(), entry.getValue());

                }
                mutants.add(new Solution(genes));
            } else
                mutants.add(solution);
        }

        return mutants;
    }

    private Map<P, Integer> mutateGene(Map<P, Integer> gene) {
        Map<P, Integer> mutatedGene = new HashMap<>();
        List<P> points = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for(var element: gene.entrySet()){
            points.add(element.getKey());
            values.add(element.getValue());
        }

        for (int i = 0; i < values.size(); i++){
            int i0 = random.nextInt(values.size());
            int i1 = random.nextInt(values.size());

            if (values.get(i0) != 0){
                values.set(i0,values.get(i0) - 1);
                values.set(i1,values.get(i1) + 1);
                break;
            }
        }

        for (int i = 0; i < gene.size(); i++){
            mutatedGene.put(points.get(0), values.get(0));
            points.remove(0);
            values.remove(0);
        }
        return mutatedGene;
    }
}
