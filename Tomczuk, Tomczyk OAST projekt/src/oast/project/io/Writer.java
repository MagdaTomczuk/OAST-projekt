package oast.project.io;

import oast.project.model.Demand;
import oast.project.model.Link;
import oast.project.model.Solution;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Writer {
    public void writeToFile(Solution solution, String fileName) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("Network cost: ");
        builder.append(solution.getNetworkCost());
        builder.append("\n");

        builder.append("Exceeded links: ");
        builder.append(solution.getCapacityExceededLinks());
        builder.append("\n");

        builder.append(getLinksStr(solution.getLinkCapacities()));
        builder.append("\n");
        //TODO: write demand

        Files.writeString(Paths.get(fileName), builder.toString());
    }

    private String getLinksStr(List<Integer> linkCapacities){
        StringBuilder builder = new StringBuilder();
        builder.append("Links:\n");

        int index = 1;
        for(Integer link: linkCapacities)
            builder.append("{").append(index++).append("} -> {").append(link).append("}\n");

        return builder.toString();
    }

    private String getDemandStr(List<Demand> demands){
        StringBuilder builder = new StringBuilder();
        builder.append("[Demand] {pathId} - > value;\n");
        return builder.toString();
    }
}
