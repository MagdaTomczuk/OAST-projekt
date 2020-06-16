package oast.project.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Solution {
    private double networkCost;
    private int capacityExceededLinks;
    private List<Integer> linkCapacities;
    private Map<P, Integer> xesMap;

    public Solution(Map<P, Integer> xesMap) {
        this.xesMap = xesMap;
    }

    public Map<P, Integer> getGene(int geneId){
        return xesMap.entrySet().stream()
                .filter(entry-> entry.getKey().getDemandId() == geneId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public int getNumberOfGenes(){
        return (int) xesMap.keySet().stream().map(P::getDemandId).distinct().count();
    }

    public double getNetworkCost() {
        return networkCost;
    }

    public void setNetworkCost(double networkCost) {
        this.networkCost = networkCost;
    }

    public int getCapacityExceededLinks() {
        return capacityExceededLinks;
    }

    public void setCapacityExceededLinks(int capacityExceededLinks) {
        this.capacityExceededLinks = capacityExceededLinks;
    }

    public List<Integer> getLinkCapacities() {
        return linkCapacities;
    }

    public void setLinkCapacities(List<Integer> linkCapacities) {
        this.linkCapacities = linkCapacities;
    }

    public Map<P, Integer> getXesMap() {
        return xesMap;
    }

    public void setXesMap(Map<P, Integer> xesMap) {
        this.xesMap = xesMap;
    }
}
