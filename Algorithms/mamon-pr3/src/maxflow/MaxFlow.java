package maxflow;

import java.util.*;
import java.io.*;

/**
 * @author: Elijah Joshua Mamon
 * @version 1.0, 23 April 2019
 * @since 1.0, 17 April 2019 Filename: MaxFlow.java
 *
 * <p>
 * Uses Fold-Fulkerson(DFS), Dinic's algorithm, and Push-Relabel to
 * find the maximum flow. Test code are left in for review, .</p>
 *
 * @copyright Elijah Joshua Mamon Developed on NetBeans IDE 8.2
 */
public class MaxFlow {

    private MaxFlow() {

    }

    int[][] relableCapacityArray;

    public void initializeCapacityArray(int numNodes) {
        relableCapacityArray = new int[numNodes][numNodes];
    }

    public void relableAddEdge(int from, int to, int capacity) {
        this.relableCapacityArray[from][to] = capacity;
    }

    static class DiniczEdge {

        int vertex, //vertex of a directed edge currVertex-v
                reverse, //index of the reverse edge
                capacity,
                flow; //flow of data

        public DiniczEdge(int vertex, int reverse, int capacity) {
            this.vertex = vertex;
            this.reverse = reverse;
            this.capacity = capacity;
        }
    }

    public static class FordEdge {

        public FordEdge residual;
        public int to, capacity;
        public final int originalCapacity;

        public FordEdge(int to, int capacity) {
            this.to = to;
            this.capacity = capacity;
            this.originalCapacity = capacity;
        }
    }

    private int n, source, sink;

    private int isVisited = 1;
    private int[] visitedArray;
    private boolean isFinished;

    private int maxFlow;
    private boolean[] minCut;
    private List<List<FordEdge>> fordGraph;

    public MaxFlow(int n, int source, int sink) {
        this.n = n;
        initializeGraph();

        this.source = source;
        this.sink = sink;
    }

    public void addEdgeFord(int from, int to, int capacity) {
        FordEdge fordEdge1 = new FordEdge(to, capacity);
        FordEdge fordEdge2 = new FordEdge(from, 0);
        
        fordEdge1.residual = fordEdge2;
        fordEdge2.residual = fordEdge1;
        
        fordGraph.get(from).add(fordEdge1);
        fordGraph.get(to).add(fordEdge2);
    }

    public List<List<FordEdge>> getGraph() {
        solveFordRes();
        return fordGraph;
    }

    public int getMaxFlow() {
        solveFordRes();
        return maxFlow;
    }

    public boolean[] getMinCut() {
        solveFordRes();
        return minCut;
    }

    public void solveFordRes() {
        if (isFinished) {
            return;
        }

        maxFlow = 0;
        visitedArray = new int[n];
        minCut = new boolean[n];

        int flow;
        do {
            flow = dfsFord(source, Integer.MAX_VALUE);
            isVisited++;
            maxFlow += flow;
        } while (flow != 0);
        for (int i = 0; i < n; i++) {
            if (visitedArray[i] == isVisited - 1) {
                minCut[i] = true;
            }
        }

        isFinished = true;
    }

    private int dfsFord(int node, int flow) {
        if (node == sink) {
            return flow;
        }

        List<FordEdge> edges = fordGraph.get(node);
        visitedArray[node] = isVisited;

        for (FordEdge edge : edges) {
            if (visitedArray[edge.to] != isVisited && edge.capacity > 0) {

                if (edge.capacity < flow) {
                    flow = edge.capacity;
                }
                int dfsFlow = dfsFord(edge.to, flow);

                if (dfsFlow > 0) {
                    FordEdge residual = edge.residual;
                    edge.capacity -= dfsFlow;
                    residual.capacity += dfsFlow;
                    return dfsFlow;
                }

            }
        }
        return 0;
    }

    private void initializeGraph() {
        fordGraph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            fordGraph.add(new ArrayList<>());
        }
    }

    public static List<DiniczEdge>[] createDiniczGraph(int numEntries) {
        List<DiniczEdge>[] graph = new List[numEntries];

        for (int i = 0; i < numEntries; i++) {
            graph[i] = new ArrayList<>();
        }

        return graph;
    }

    public static void addEdgeDinicz(List<DiniczEdge>[] graph, int current, int next, int capacity) {
        graph[current].add(new DiniczEdge(next,
                graph[next].size(), capacity));
        graph[next].add(new DiniczEdge(current,
                graph[current].size() - 1, 0));
    }

    static boolean bfsDinicz(List<DiniczEdge>[] graph, int source, int next, int[] level) {
        Arrays.fill(level, -1);

        level[source] = 0;

        int[] vertexQueue = new int[graph.length];
        int sizeOfQueue = 0;
        vertexQueue[sizeOfQueue++] = source;
        for (int i = 0; i < sizeOfQueue; i++) {
            int currVertex = vertexQueue[i];
            for (DiniczEdge currEdge : graph[currVertex]) {
                if (level[currEdge.vertex] < 0 && currEdge.flow < currEdge.capacity) {
                    level[currEdge.vertex] = level[currVertex] + 1;
                    vertexQueue[sizeOfQueue++] = currEdge.vertex;
                }
            }
        }
        return level[next] >= 0;
    }

    static int dfsDinicz(List<DiniczEdge>[] diniczGraph, int[] nextEdge, int[] level, int sink, int currVertex, int flow) {
        if (currVertex == sink) {
            return flow;
        }
        for (; nextEdge[currVertex] < diniczGraph[currVertex].size(); ++nextEdge[currVertex]) {
            DiniczEdge next = diniczGraph[currVertex].get(nextEdge[currVertex]);
            if (level[next.vertex] == level[currVertex] + 1 && next.flow < next.capacity) {

                int tempFlow = dfsDinicz(diniczGraph, nextEdge, level, sink, next.vertex, Math.min(flow, next.capacity - next.flow));

                if (tempFlow > 0) {
                    next.flow += tempFlow;
                    diniczGraph[next.vertex].get(next.reverse).flow -= tempFlow;
                    return tempFlow;
                }
            }
        }
        return 0;
    }

    public static int findDinicMaxFlow(List<DiniczEdge>[] graph, int source, int sink) {
        int maxFlow = 0;

        int[] distance = new int[graph.length];

        while (bfsDinicz(graph, source, sink, distance)) {

            int[] ptr = new int[graph.length];

            while (true) {
                int diniczFlow = dfsDinicz(graph, ptr, distance, sink, source, Integer.MAX_VALUE);
                if (diniczFlow == 0) {
                    break;
                }
                maxFlow += diniczFlow;
            }
        }
        return maxFlow;
    }

    public int preFlowMaxFlow(int source, int sink) {
        int numNodes = relableCapacityArray.length;

        int[] verticesHeight = new int[numNodes];
        verticesHeight[source] = numNodes - 1;

        int[] maxVerticesHeight = new int[numNodes];

        int[][] tempCapacityArray = new int[numNodes][numNodes];
        int[] edgesArray = new int[numNodes];

        for (int i = 0; i < numNodes; ++i) {
            tempCapacityArray[source][i] = relableCapacityArray[source][i];
            tempCapacityArray[i][source] = -tempCapacityArray[source][i];
            edgesArray[i] = relableCapacityArray[source][i];
        }

        for (int currentVertex = 0;;) {
            if (currentVertex == 0) {
                for (int i = 0; i < numNodes; ++i) {
                    if (i != source && i != sink && edgesArray[i] > 0) {
                        if (currentVertex != 0 && verticesHeight[i] > verticesHeight[maxVerticesHeight[0]]) {
                            currentVertex = 0;
                        }
                        maxVerticesHeight[currentVertex++] = i;
                    }
                }
            }
            if (currentVertex == 0) {
                break;
            }
            while (currentVertex != 0) {
                int currMaxVertexHeight = maxVerticesHeight[currentVertex - 1];
                boolean isPushed = false;
                for (int j = 0; j < numNodes && edgesArray[currMaxVertexHeight] != 0; ++j) {
                    if (verticesHeight[currMaxVertexHeight] == verticesHeight[j] + 1
                            && relableCapacityArray[currMaxVertexHeight][j] - tempCapacityArray[currMaxVertexHeight][j] > 0) {

                        int flow = Math.min(relableCapacityArray[currMaxVertexHeight][j] - tempCapacityArray[currMaxVertexHeight][j],
                                edgesArray[currMaxVertexHeight]);

                        tempCapacityArray[currMaxVertexHeight][j] += flow;
                        tempCapacityArray[j][currMaxVertexHeight] -= flow;

                        edgesArray[currMaxVertexHeight] -= flow;
                        edgesArray[j] += flow;

                        if (edgesArray[currMaxVertexHeight] == 0) {
                            --currentVertex;
                        }
                        isPushed = true;
                    }
                }
                if (!isPushed) {
                    verticesHeight[currMaxVertexHeight] = Integer.MAX_VALUE;
                    for (int j = 0; j < numNodes; ++j) {
                        if (verticesHeight[currMaxVertexHeight] > verticesHeight[j] + 1
                                && relableCapacityArray[currMaxVertexHeight][j] - tempCapacityArray[currMaxVertexHeight][j] > 0) {
                            verticesHeight[currMaxVertexHeight] = verticesHeight[j] + 1;
                        }
                    }
                    if (verticesHeight[currMaxVertexHeight] > verticesHeight[maxVerticesHeight[0]]) {
                        currentVertex = 0;
                        break;
                    }
                }
            }
        }

        int maximizeFlow = 0;
        for (int i = 0; i < numNodes; i++) {
            maximizeFlow += tempCapacityArray[source][i];
        }

        return maximizeFlow;
    }

    public static void randomize(int n) throws IOException {
        Random rand = new Random();

        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("input" + n + ".txt"))) {
            for (int i = 0; i < n - 1; i++) {
                toFile.write(i + " ");

                for (int j = 0; j < n - 1; j += 5) {

                    if (j + 1 != i) {
                        toFile.write((j + 1) + ":");
                        toFile.write(String.valueOf(rand.nextInt(15) + 1));
                        toFile.write(" ");
                    }

                    toFile.write((i + 1) + ":");
                    toFile.write(String.valueOf(rand.nextInt(15) + 1));
                    toFile.write(" ");

                }
                toFile.newLine();
            }
            toFile.write(n - 1 + " ");
        }
    }

    public static void randomizeDense(int n) throws IOException {
        Random rand = new Random();

        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("inputRed" + n + ".txt"))) {
            for (int i = 0; i < n - 1; i++) {
                toFile.write(i + " ");
                toFile.write(i + 1 + ":");
                toFile.write(String.valueOf(rand.nextInt(100) + 1));
                toFile.write(" ");

                for (int j = i; j < n - 1; j += rand.nextInt(n - 1)) {
                    toFile.write((j + 1) + ":");
                    toFile.write(String.valueOf(rand.nextInt(15) + 1));
                    toFile.write(" ");
                }
                toFile.newLine();
            }
            toFile.write(n - 1 + " ");
        }
    }

    public static void randomizeLinear(int n) throws IOException {
        Random rand = new Random();

        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("inputBlue" + n + ".txt"))) {
            for (int i = 0; i < n - 1; i++) {
                toFile.write(i + " " + (i + 1) + ":");
                toFile.write(String.valueOf(rand.nextInt(55) + 1));
                toFile.write(" ");
                toFile.newLine();
            }
            toFile.write(n - 1 + " ");
        }
    }

    public static void writeToFileFF(long timeArray[], int numberOfItems) throws IOException {
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("Ford_Fulkerson" + (numberOfItems) + ".txt"))) {
            for (int i = 0; i < timeArray.length; i++) {
                toFile.write(String.valueOf(timeArray[i]));
                toFile.newLine();
            }
        }
    }

    public static void writeToFileDD(long timeArray[], int numberOfItems) throws IOException {
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("Dinics" + (numberOfItems) + ".txt"))) {
            for (int i = 0; i < timeArray.length; i++) {
                toFile.write(String.valueOf(timeArray[i]));
                toFile.newLine();
            }
        }
    }

    public static void writeToFilePR(long timeArray[], int numberOfItems) throws IOException {
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("Push_Relabel" + (numberOfItems) + ".txt"))) {
            for (int i = 0; i < timeArray.length; i++) {
                toFile.write(String.valueOf(timeArray[i]));
                toFile.newLine();
            }
        }
    }

    public static void writeToFile(int FF, int DD, int PR, int numberOfInputs,
            long startTimeFF, long endTimeFF,
            long startTimeDD, long endTimeDD,
            long startTimePR, long endTimePR,
            String inputFile) throws IOException {
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter(inputFile + "Out.txt"))) {
            toFile.write(numberOfInputs + " items");
            toFile.newLine();
            toFile.write("Ford-Fulkerson Maximum Flow: " + FF);
            toFile.newLine();
            toFile.write("Ford-Fulkerson Time: " + (endTimeFF - startTimeFF) / (double) (1000000000) + " seconds");
            toFile.newLine();
            toFile.write("Dinics Maximum Flow: " + DD);
            toFile.newLine();
            toFile.write("Dinics Time: " + (endTimeDD - startTimeDD) / (double) (1000000000) + " seconds");
            toFile.newLine();
            toFile.write("Push-Relabel Maximum Flow: " + PR);
            toFile.newLine();
            toFile.write("Push-Relabel Time: " + (endTimePR - startTimePR) / (double) (1000000000) + " seconds");
            toFile.newLine();

        }
    }

    /**
     * Testing code are left in the main to see how automation of the testing was made.
     */
    public static void main(String[] args) throws IOException {
        //long[] FFArray = new long[20];
        //long[] DDArray = new long[20];
        //long[] PRArray = new long[20];
        // int indexx = 0;
        //for (int r = 50; r < 1050; r += 50) {
        String usrIn = null;
        boolean isNotValidInput = true;
        int FF;
        int DD;
        int PR;

        String filename;

        int source = 0;
        int sink;
        String sinkString = "0";

        try (BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter File Name (Leave the .txt extension out");
            filename = userIn.readLine();

            int numOfInputs = 0;

            //File inputFile = new File("input" + r + ".txt");
            //filename = "input" + r;
            File inputFile = new File(filename + ".txt");

            Scanner inputScanner;
            try (Scanner total = new Scanner(inputFile)) {
                inputScanner = new Scanner(inputFile);
                source = total.nextInt();
                while (total.hasNextLine()) {
                    numOfInputs++;
                    sinkString = total.nextLine();
                }
                total.close();
            }

            sink = Integer.parseInt(sinkString.trim());

            int inp = numOfInputs;
            System.out.println(inp);

            String[] inputString = new String[numOfInputs];
            MaxFlow fordSolver = new MaxFlow(inp, source, sink);
            List<DiniczEdge>[] diniczSolver = createDiniczGraph(inp);

            MaxFlow preFlowSolver = new MaxFlow();
            preFlowSolver.initializeCapacityArray(inp);

            System.out.println("Scanning Inputs...");
            try (Scanner inputStr = new Scanner(inputFile)) {
                inputScanner = new Scanner(inputFile);
                for (int i = 0; i < numOfInputs; i++) {
                    inputString[i] = inputStr.nextLine();
                }
                inputStr.close();
            }
            System.out.println("Done Scanning Inputs...");

            int currVertex;
            int toVertex;
            int capacity;

            String[] arrayOfStr = null;
            String[] arrayOfArrayOf = null;

            System.out.println("Now Populating Graphs...");
            for (int i = 0; i < numOfInputs - 1; i++) {
                arrayOfStr = inputString[i].split(" ");
                currVertex = Integer.parseInt(arrayOfStr[0]);

                for (int j = 1; j < arrayOfStr.length; j++) {
                    arrayOfArrayOf = arrayOfStr[j].split(":");
                    toVertex = Integer.parseInt(arrayOfArrayOf[0]);
                    capacity = Integer.parseInt(arrayOfArrayOf[1]);

                    fordSolver.addEdgeFord(currVertex, toVertex, capacity);

                    addEdgeDinicz(diniczSolver, currVertex, toVertex, capacity);

                    preFlowSolver.relableAddEdge(currVertex, toVertex, capacity);
                }

            }

            System.out.println("Done Populating Graphs...");

            System.out.println("Now Initiating Ford-Fulkerson...");
            long beginningTimeFF = System.nanoTime();
            FF = fordSolver.getMaxFlow();
            System.out.println("Maximum Flow for Ford-Fulkerson: " + FF);
            long endingTimeFF = System.nanoTime();
            System.out.println("Ford-Fulkerson Done...");

            System.out.println("Now Initiating Dinics...");
            long beginningTimeDD = System.nanoTime();
            DD = findDinicMaxFlow(diniczSolver, source, sink);
            System.out.println("Max Flow using Dinics: " + DD);
            long endingTimeDD = System.nanoTime();
            System.out.println("Dinics Done...");

            System.out.println("Now Initiating Push-Relable...");
            long beginningTimePR = System.nanoTime();
            PR = preFlowSolver.preFlowMaxFlow(source, sink);
            System.out.println("Max Flow using Push-Relable: " + PR);
            long endingTimePR = System.nanoTime();
            System.out.println("Push-Relable Done...");

            System.out.println("Ford-Fulkerson: " + (endingTimeFF - beginningTimeFF) + "ns / "
                    + (endingTimeFF - beginningTimeFF) / (double) (1000000000) + " seconds");

            System.out.println("Dinics: " + (endingTimeDD - beginningTimeDD) + "ns / "
                    + (endingTimeDD - beginningTimeDD) / (double) (1000000000) + " seconds");

            System.out.println("Push-Relable: " + (endingTimePR - beginningTimePR) + "ns / "
                    + (endingTimePR - beginningTimePR) / (double) (1000000000) + " seconds");

            //FFArray[indexx] = endingTimeFF - beginningTimeFF;
            //DDArray[indexx] = endingTimeDD - beginningTimeDD;
            ///PRArray[indexx] = endingTimePR - beginningTimePR;
            //indexx++;
            writeToFile(FF, DD, PR, numOfInputs, beginningTimeFF, endingTimeFF, beginningTimeDD, endingTimeDD,
                    beginningTimePR, endingTimePR, filename);

        } catch (Exception e) {
            System.out.println("File Not Found");
        }

    }

    //writeToFileFF(FFArray, 1000);
    //writeToFileDD(DDArray, 1000);
    //writeToFilePR(PRArray, 1000);
    //}
}
