package app;

import java.util.*;
//introduced in version 2.0
class FindShortestPairWiseDist {

    protected double[][] findDist(double[][] kValues, String[] alignTitles) {
        int numSeq = kValues[0].length;

        Map<String, Map<String, Double>> originalDist = new HashMap<String, Map<String, Double>>();
        Map<String, Map<String, Double>> clusterDist = new HashMap<String, Map<String, Double>>();

        for (int x = 0; x < numSeq; x++) {
            originalDist.put(alignTitles[x], new HashMap<String, Double>());
            clusterDist.put(alignTitles[x], new HashMap<String, Double>());
            for (int y = 0; y < numSeq; y++) {
                originalDist.get(alignTitles[x]).put(alignTitles[y], kValues[x][y]);
                clusterDist.get(alignTitles[x]).put(alignTitles[y], kValues[x][y]);
            }
        }

        int numClusters = numSeq;
        // made a clone of thee new String name array to use as later to format the output
        String[] newClusterNames = alignTitles;
        String[] clusterToPrint = newClusterNames;

        while (numClusters > 2) {
            double minValue = 999999;
            int minX = 0;
            int minY = 0;

            for (int x = 0; x < numClusters; x++) {
                for (int y = 0; y < numClusters; y++) {
                    double currValue = clusterDist.get(newClusterNames[x]).get(newClusterNames[y]);
                    if ((currValue < minValue) && (x != y)) {
                        minValue = currValue;
                        minX = x;
                        minY = y;
                    }
                }
            }

            numClusters--;

            System.out.println();
            System.out.println("Sequence " + clusterToPrint[minX] + " and " + clusterToPrint[minY]
                    + " are to be merged (cluster " + minX + " and " + minY + ")");

            newClusterNames = mergeNames(minX, minY, newClusterNames);
            clusterToPrint = mergeNamesToPrint(minX, minY, clusterToPrint);

            clusterDist = MergeCluster(originalDist, newClusterNames, clusterToPrint, minX, minY);

        }

        System.out.println();
        System.out.println("The last cluster to be merged are: " + clusterToPrint[0] + " and " + clusterToPrint[1]);

        System.out.println();
        String outPut = "UPGMA Output: (" + clusterToPrint[0] + ", " + clusterToPrint[1] + ")";
        System.out.println(outPut);
        System.out.println();

        WriteUPGMAToFile wf = new WriteUPGMAToFile();

        wf.write(outPut);

        return kValues;
    }

    private String[] mergeNames(int minX, int minY, String[] prevClusterNames) {
        String[] newClusterNames = new String[prevClusterNames.length - 1];

        int ncnCounter = 0;

        for (int x = 0; x < prevClusterNames.length; x++) {
            if (x != minY) {
                newClusterNames[ncnCounter] = prevClusterNames[x];
                ncnCounter++;
            }
        }

        newClusterNames[minX] = prevClusterNames[minX] + prevClusterNames[minY];

        return newClusterNames;
    }

    private String[] mergeNamesToPrint(int minX, int minY, String[] prevClusterNames) {
        String[] newClusterNames = new String[prevClusterNames.length - 1];

        int ncnCounter = 0;

        for (int x = 0; x < prevClusterNames.length; x++) {
            if (x != minY) {
                newClusterNames[ncnCounter] = prevClusterNames[x];
                ncnCounter++;
            }
        }

        newClusterNames[minX] = "(" + prevClusterNames[minX] + ", " + prevClusterNames[minY] + ")";

        return newClusterNames;
    }

    private Map<String, Map<String, Double>> MergeCluster(Map<String, Map<String, Double>> originalDist,
            String[] clusterNames, String[] clusterToPrint, int minX, int minY) {

        Map<String, Map<String, Double>> newClusterDist = new HashMap<String, Map<String, Double>>();

        for (int x = 0; x < clusterNames.length; x++) {
            newClusterDist.put(clusterNames[x], new HashMap<String, Double>());
            for (int y = 0; y < clusterNames.length; y++) {
                if (x == y) {
                    newClusterDist.get(clusterNames[x]).put(clusterNames[y], 0.0);
                } else if (x == minX || y == minX) {
                    double sum = 0;
                    for (int i = 0; i < (clusterNames[x].length()); i += 10) {
                        for (int j = 0; j < (clusterNames[y].length()); j += 10) {
                            sum += originalDist.get(clusterNames[x].substring(0 + i, 10 + i))
                                    .get(clusterNames[y].substring(0 + j, 10 + j));
                        }
                    }
                    sum = sum / ((clusterNames[x].length() * clusterNames[y].length()) / 100);
                    newClusterDist.get(clusterNames[x]).put(clusterNames[y], sum);
                } else {
                    newClusterDist.get(clusterNames[x]).put(clusterNames[y],
                            originalDist.get(clusterNames[x]).get(clusterNames[y]));
                }

            }
        }
        System.out.println();

        for (int x = 0; x < clusterNames.length; x++) {
            for (int y = 0; y < clusterNames.length; y++) {
                System.out.printf("%11.5f", newClusterDist.get(clusterNames[x]).get(clusterNames[y]));
            }
            System.out.println();
        }

        return newClusterDist;
    }
}