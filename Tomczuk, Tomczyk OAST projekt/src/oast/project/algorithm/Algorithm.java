package oast.project.algorithm;

import oast.project.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Algorithm {

    public abstract Solution runDAP();
    public abstract Solution runDDAP();

    public static List<Integer> calculateLinksCapacities(Network network, Solution solution) {
        List<Integer> linkCapacities = new ArrayList<>(Collections.nCopies(network.getCountOfLinks(), 0));

        for (Demand demand : network.getDemands())
            for (Path path : demand.getPaths())
                for (int edge : path.getEdges()) {
                    Integer value = solution.getXesMap().get(new P(demand.getId(), path.getId()));
                    if (value != null)
                        linkCapacities.set(edge - 1, linkCapacities.get(edge - 1) + value);
                }

        return IntStream
                .range(0, linkCapacities.size())
                .mapToObj(i->
                        (int)  Math.ceil((double) linkCapacities.get(i) / network.getLinks().get(i).getNumberOfLambdasInFibre()))
                .collect(Collectors.toList());
    }

    public static int getBinaryCoefficient(int n, int k) {
        int r = 1;
        int d;
        if (k > n) return 0;
        for (d = 1; d <= k; d++) {
            r *= n--;
            r /= d;
        }
        return r;
    }

    public static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
        List<List<T>> resultLists = new ArrayList<>();
        if (lists.size() == 0) {
            resultLists.add(new ArrayList<>());
            return resultLists;
        } else {
            List<T> firstList = lists.get(0);
            List<List<T>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
            for (T condition : firstList) {
                for (List<T> remainingList : remainingLists) {
                    ArrayList<T> resultList = new ArrayList<>();
                    resultList.add(condition);
                    resultList.addAll(remainingList);
                    resultLists.add(resultList);
                }
            }
        }
        return resultLists;
    }
}
