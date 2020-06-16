package oast.project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Demand {

    private int id;
    private int startNodeId;
    private int endNodeId;
    private int volume;

    private List<Path> paths;

    public Demand(List<String> definingLineStrList, int demandId) {
        this.id = demandId;
        paths = new ArrayList<>();

        List<Integer> headerLine = Arrays.stream(definingLineStrList.get(0).split(" "))
                .filter(Predicate.not(String::isEmpty))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        definingLineStrList.remove(0);
        int numberOfPaths = Integer.parseInt(definingLineStrList.get(0));
        definingLineStrList.remove(0);

        this.startNodeId = headerLine.get(0);
        this.endNodeId = headerLine.get(1);
        this.volume = headerLine.get(2);

        int pathId = 0;
        for(String line: definingLineStrList)
            paths.add(new Path(line, ++pathId, demandId));

        if(paths.size()!=numberOfPaths)
            throw new RuntimeException("Incorrect number of paths");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getNumberOfPaths() {
        return paths.size();
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }
}
