package oast.project.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Network {
    private List<Demand> demands;
    private List<Link> links;

    public Network(List<Link> links, List<Demand> demands) {
        this.demands = demands;
        this.links = links;
    }

    public int getCountOfDemands() {
        return demands.size();
    }

    public int getCountOfLinks() {
        return links.size();
    }

    public List<Demand> getDemands() {
        return demands;
    }

    public void setDemands(List<Demand> demands) {
        this.demands = demands;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
