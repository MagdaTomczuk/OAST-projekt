package oast.project;

import oast.project.algorithm.Algorithm;
import oast.project.algorithm.BruteForce;
import oast.project.algorithm.GeneticAlgorithm;
import oast.project.algorithm.GeneticAlgorithmParams;
import oast.project.io.Reader;
import oast.project.model.Network;
import oast.project.model.P;
import oast.project.model.Solution;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Reader reader = new Reader();
        Main main = new Main();
        GeneticAlgorithmParams params = null;
        Algorithm algorithm;
        Solution solution;
        try {
            int algorithmChoice = main.scanAlgorithmType();
            int problemChoice = main.scanProblemType();
            String fileNameChoice = main.scanFileName();
            if(algorithmChoice == 2)
                params = main.scanParams();

            System.out.println("Wczytywanie pliku");
            Network network = reader.readFile(fileNameChoice);
            System.out.println("Wczytano "+fileNameChoice);

            if(algorithmChoice==1)
                algorithm = new BruteForce(network);
            else if(algorithmChoice==2)
                algorithm = new GeneticAlgorithm(params, network);
            else
                throw new Exception("Nieobsługiwany algorytm");


            long startTime = System.nanoTime();
            if(problemChoice == 1)
                solution = algorithm.runDAP();
            else if(problemChoice == 2)
                solution = algorithm.runDDAP();
            else
                throw new Exception("Nieobsługiwany problem");
            long stopTime = System.nanoTime();

            System.out.println("Zakończono obliczenia w "+(stopTime-startTime)+"ns.");

            main.printSolution(solution, algorithm, problemChoice);
            main.writeSolutionToFile(solution);

        } catch (IOException e) {
            System.out.println("Nie można otworzyć pliku o wskazanej nazwie");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int scanAlgorithmType() {
        System.out.println("Wybierz algorytm:\n1. Brute force\n2. Ewolicyjny\n");
        int result = scanner.nextInt();
        if(result<1 || result>2)
            throw new RuntimeException("Nieprawidłowy wybór");
        return result;
    }

    private int scanProblemType(){
        System.out.println("Wybierz tryb:\n1. DAP\n2. DDAP\n");
        int result = scanner.nextInt();
        if(result<1 || result>2)
            throw new RuntimeException("Nieprawidłowy wybór");
        return result;
    }

    private String scanFileName(){
        System.out.println("Wybierz plik:\n1. net4.txt\n2. net12_1.txt\n3. net12_2.txt\n");
        int result = scanner.nextInt();
        if(result<1 || result>3)
            throw new RuntimeException("Nieprawidłowy wybór");

        String fileName = switch(result){
            case 1 -> "net4.txt";
            case 2 -> "net12_1.txt";
            case 3 -> "net12_2.txt";
            default -> null;
        };
        return fileName;
    }

    private GeneticAlgorithmParams scanParams(){
        GeneticAlgorithmParams params = new GeneticAlgorithmParams();

        System.out.println("Podaj ziarno (seed) generatora liczb pseudolosowych:");
        params.setSeed(scanner.nextInt());

        System.out.println("\nPodaj parametry algorytmu ewolucyjego");
        System.out.println("Rozmiar populacji startowej: ");
        params.setNumberOfChromosomes(scanner.nextInt());

        System.out.println("Podaj prawdopodobieństwo krzyżowania [0-1]: ");
        params.setProbabilityOfCrossing((float)scanner.nextDouble());

        System.out.println("Podaj prawdopodobieństwo mutacji [0-1]: ");
        params.setProbabilityOfMutation((float)scanner.nextDouble());

        System.out.println("Podaj procent najlepszych chromosomówc[%]:");
        params.setPercentOfBestChromosomes((float)scanner.nextDouble());

        System.out.println("\nWarunki stopu");
        System.out.println("Maksymalny czas poszukiwania rozwiązania[ms]: ");
        params.setMaxTime(scanner.nextInt());

        System.out.println("Maksymalna liczba generacji: ");
        params.setMaxNumberOfGenerations(scanner.nextInt());

        System.out.println("Maksymalna liczba mutacji: ");
        params.setMaxMutationNumber(scanner.nextInt());

        System.out.println("Maksymalna liczba prób poprawy najlepszego znanago rozwiązania: ");
        params.setMaxNumberOfContinuousNonBetterSolutions(scanner.nextInt());


        return params;
    }

    private void printSolution(Solution solution, Algorithm algorithm, int problemChoice){
        if(algorithm instanceof GeneticAlgorithm)
            System.out.println("Liczba iteracji: "+((GeneticAlgorithm)algorithm).getCurrentGeneration());
        else if(algorithm instanceof BruteForce)
            System.out.println("Całkowita liczba rozwiązań: "+((BruteForce) algorithm).getTotalNumberOfSolutions());

        if(problemChoice == 1)
            System.out.println("Przeciążenie DAP: "+ solution.getCapacityExceededLinks());
        else if(problemChoice == 2)
            System.out.println("Koszt DDAP: " + solution.getNetworkCost());

        System.out.println();

        //sort result by demandId
        List<P> ordered = solution.getXesMap().keySet().stream().sorted(Comparator.comparing(P::getDemandId)).collect(Collectors.toList());

        int demandId = ordered.get(0).getDemandId();
        System.out.print("{"+demandId+"}");
        for(var p: ordered){
            if(p.getDemandId() != demandId){
                System.out.println();
                demandId=p.getDemandId();
                System.out.print("{"+demandId+"}");
            }
            System.out.print(p.getPathId()+" -> "+ solution.getXesMap().get(p)+";");
        }
    }

    private void writeSolutionToFile(Solution solution){
        //TODO
    }
}
