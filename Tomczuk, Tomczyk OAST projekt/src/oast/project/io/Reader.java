package oast.project.io;

import oast.project.model.Demand;
import oast.project.model.Link;
import oast.project.model.Network;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Reader {
    private final String SEPARATOR = "-1";

    public Network readFile(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        int separatorLine = lines.indexOf(SEPARATOR);

        List<String> linksLines = lines.subList(0, separatorLine).stream()
                .filter(Predicate.not(String::isBlank)).collect(Collectors.toList());
        List<String> demandsLines = lines.subList(separatorLine+1, lines.size());

        return new Network(parseLinks(linksLines), parseDemands(demandsLines));
    }

    private List<Demand> parseDemands(List<String> demandsLines){
        while(demandsLines.get(0).isBlank())
            demandsLines.remove(0);

        int numberOfDemands = Integer.parseInt(demandsLines.get(0));
        demandsLines.remove(0);
        while(demandsLines.get(0).isBlank())
            demandsLines.remove(0);

        List<List<String>> demandGroups = splitDemandGroups(demandsLines);
        if(demandGroups.size()!=numberOfDemands)
            throw new RuntimeException("Incorrect number of demands");

        return IntStream.range(0, demandGroups.size()).boxed()
                .map(i->new Demand(demandGroups.get(i), i+1))
                .collect(Collectors.toList());
    }

    private List<List<String>> splitDemandGroups(List<String> demandsLines){
        List<List<String>> result = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();
        for(String line: demandsLines){
            if(line.isBlank()){
                result.add(currentGroup);
                currentGroup = new ArrayList<>();
            }
            else
                currentGroup.add(line);
        }
        result.add(currentGroup);

        result.remove(Collections.EMPTY_LIST);
        return result;
    }

    private List<Link> parseLinks(List<String> linksLines){
        int numberOfLinks = Integer.parseInt(linksLines.get(0));
        linksLines.remove(0);

        if(linksLines.size()!=numberOfLinks)
            throw new RuntimeException("Incorrect number of links");

        return linksLines.stream().map(Link::new).collect(Collectors.toList());
    }
}
