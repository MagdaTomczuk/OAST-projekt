package oast.project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Path {
    private int id;
    private int demandId;
    private List<Integer> edges;

    public Path(String definingLineStr, int pathId, int demandId) {
        this.id = pathId;
        this.demandId = demandId;
        this.edges = new ArrayList<>();

        Arrays.stream(definingLineStr.split(" "))
                .filter(Predicate.not(String::isEmpty))
                .skip(1)
                .forEach(edge -> edges.add(Integer.parseInt(edge)));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDemandId() {
        return demandId;
    }

    public void setDemandId(int demandId) {
        this.demandId = demandId;
    }

    public List<Integer> getEdges() {
        return edges;
    }

    public void setEdges(List<Integer> edges) {
        this.edges = edges;
    }
}
