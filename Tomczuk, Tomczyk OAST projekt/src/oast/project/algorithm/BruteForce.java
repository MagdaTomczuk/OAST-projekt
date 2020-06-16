package oast.project.algorithm;

import oast.project.model.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BruteForce extends Algorithm {

    private final Network network;
    private Solution bestSolution;
    private final List<Solution> solutions;

    public BruteForce(Network network) {
        this.network = network;
        this.solutions = prepareSolutionsWithLinkCapacities();
    }

    @Override
    public Solution runDAP() {
        for (Solution s : solutions) {
            List<Integer> values = new ArrayList<>();
            for (int i = 0; i < s.getLinkCapacities().size(); i++) {
                values.add(Math.max(s.getLinkCapacities().get(i) - network.getLinks().get(i).getNumberOfFibrePairs(), 0));
            }
            s.setCapacityExceededLinks((int) values.stream().filter(x -> x > 0).count());
            if (Collections.max(values) == 0)
                return s;
        }
        return null;
    }

    @Override
    public Solution runDDAP() {
        double finalCost = Double.MAX_VALUE;
        double temporaryCost = 0.0;

        for (Solution s : solutions) {
            List<Integer> costsOfLinks = s.getLinkCapacities();

            for (int i = 0; i < costsOfLinks.size(); i++)
                temporaryCost += network.getLinks().get(i).getFibrePairCost() * costsOfLinks.get(i);

            if (costsOfLinks.stream().mapToInt(Integer::intValue).sum() == 0)
                temporaryCost = Double.MAX_VALUE;

            if (temporaryCost < finalCost) {
                finalCost = temporaryCost;
                bestSolution = s;
            }

            temporaryCost = 0.0;
        }

        bestSolution.setNetworkCost(finalCost);

        return bestSolution;
    }

    public int getTotalNumberOfSolutions(){
        return solutions.size();
    }

    private List<Solution> prepareSolutionsWithLinkCapacities() {
        List<Solution> solutions = prepareSolutions();
        for (Solution solution : solutions)
            solution.setLinkCapacities(calculateLinksCapacities(network, solution));

        return solutions;
    }

    private List<Solution> prepareSolutions() {
        List<List<Solution>> solutionsCombinations = network.getDemands().stream().map(this::prepareOneDemandPathCombinations).collect(Collectors.toList());
        List<List<Integer>> indexesCombination = solutionsCombinations.stream()
                .map(solutionCombination ->
                        IntStream.range(0, solutionCombination.size())
                                .boxed()
                                .collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<List<Integer>> indexesCombinationsCartesianProduct = cartesianProduct(indexesCombination);

        return indexesCombinationsCartesianProduct.stream()
                .map(element -> prepareSolutions(solutionsCombinations, element))
                .collect(Collectors.toList());

    }

    private List<Solution> prepareOneDemandPathCombinations(Demand demand) {
        List<Solution> oneDemandPathCombinations = new ArrayList<>();

        var combinationsNumber = getBinaryCoefficient(demand.getNumberOfPaths() + demand.getVolume() - 1, demand.getVolume());
        List<List<Integer>> combinations = prepareCombinations(demand.getVolume(), demand.getNumberOfPaths());

        for (int i = 0; i < combinationsNumber; i++) {
            Map<P, Integer> xes = new HashMap<>();
            for (Path path : demand.getPaths()) {
                xes.put(new P(demand.getId(), path.getId()), combinations.get(i).get(path.getId() - 1));
            }
            oneDemandPathCombinations.add(new Solution(xes));
        }

        return oneDemandPathCombinations;
    }

    private List<List<Integer>> prepareCombinations(int demandVolume, int numberOfPaths) {
        List<List<Integer>> combinations = new ArrayList<>();
        List<Integer> singleCombination = new ArrayList<>();

        for (int i = 0; i <= demandVolume; i++)
            singleCombination.add(i);

        for (int i = 0; i < numberOfPaths; i++)
            combinations.add(singleCombination);

        return cartesianProduct(combinations).stream()
                .filter(list -> list.stream().mapToInt(Integer::intValue).sum() == demandVolume)
                .collect(Collectors.toList());
    }

    private Solution prepareSolutions(List<List<Solution>> solutionsCombinations, List<Integer> indexesCombination) {
        Solution result = new Solution(new HashMap<>());

        for (int i = 0; i < solutionsCombinations.size(); i++)
            for (Map.Entry<P, Integer> entry : solutionsCombinations.get(i).get(indexesCombination.get(i)).getXesMap().entrySet())
                result.getXesMap().put(entry.getKey(), entry.getValue());

        return result;
    }







}
