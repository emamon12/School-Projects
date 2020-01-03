package app;

import java.util.*;

class NeighborJoin {

    protected void neighborJoin(double[][] matrixCluster, int numClusters, String[] clustNames) {

        Map<String, Map<String, Double>> pCluster = new HashMap<String, Map<String, Double>>();
        Map<String, Map<String, Double>> nextCluster = new HashMap<String, Map<String, Double>>();

        int clusters = numClusters;
        String[] nameToPrint = clustNames;
        double[][] mCluster = matrixCluster;

        for (int x = 0; x < clusters; x++) {
            nextCluster.put(clustNames[x], new HashMap<String, Double>());
            for (int y = 0; y < clusters; y++) {
                nextCluster.get(clustNames[x]).put(clustNames[y], mCluster[x][y]);
            }
        }

        for (int x = 0; x < clustNames.length; x++) {
            System.out.printf("%11s", clustNames[x]);
        }

        System.out.println();

        for (int x = 0; x < clusters; x++) {
            for (int y = 0; y < clusters; y++) {
                System.out.printf("%11.5f", nextCluster.get(clustNames[x]).get(clustNames[y]));
            }
            System.out.println();
        }

        while (clusters > 2) {
            double[] rValues = new double[clusters];

            for (int x = 0; x < clusters; x++) {
                int sum = 0;
                for (int y = 0; y < clusters; y++) {
                    sum += nextCluster.get(clustNames[x]).get(clustNames[y]);
                }
                rValues[x] = sum;
            }

            System.out.println("r Values: ");

            for (int x = 0; x < clustNames.length; x++) {
                System.out.printf("%11s", clustNames[x]);
            }

            System.out.println();

            for (int x = 0; x < clusters; x++) {
                System.out.printf("%11.5f", rValues[x]);
            }

            double[][] transitionMatrix = new double[clusters][clusters];

            double min = 0;
            int minx = 0;
            int miny = 0;
            for (int x = 0; x < clusters; x++) {
                for (int y = 0; y < clusters; y++) {
                    if (x != y) {
                        transitionMatrix[x][y] = (clusters - 2) * nextCluster.get(clustNames[x]).get(clustNames[y])
                                - rValues[x] - rValues[y];
                        if (transitionMatrix[x][y] < min) {
                            min = transitionMatrix[x][y];
                            minx = x;
                            miny = y;
                        }
                    } else {
                        transitionMatrix[x][y] = 0;
                    }
                }
            }
            System.out.println();
            System.out.println("Transition Matrix: ");

            for (int x = 0; x < clustNames.length; x++) {
                System.out.printf("%11s", clustNames[x]);
            }

            System.out.println();
            for (int x = 0; x < clusters; x++) {
                for (int y = 0; y < clusters; y++) {
                    System.out.printf("%11.5f", transitionMatrix[x][y]);
                }
                System.out.println();
            }

            String[] prevNames = clustNames;
            clustNames = mergeNames(minx, miny, clustNames);
            nameToPrint = mergeNamesToPrint(minx, miny, nameToPrint);

            pCluster = nextCluster;
            nextCluster = MergeCluster(pCluster, clustNames, prevNames, minx, miny);

            for (int x = 0; x < nameToPrint.length; x++) {
                System.out.printf("%s", nameToPrint[x]);
            }

            System.out.println();

            clusters--;
        }
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

    private Map<String, Map<String, Double>> MergeCluster(Map<String, Map<String, Double>> originalDist,
            String[] clusterNames, String[] prevNames, int minX, int minY) {

        Map<String, Map<String, Double>> newClusterDist = new HashMap<String, Map<String, Double>>();

        for (int x = 0; x < clusterNames.length; x++) {
            newClusterDist.put(clusterNames[x], new HashMap<String, Double>());
            for (int y = 0; y < clusterNames.length; y++) {
                if (x == y) {
                    newClusterDist.get(clusterNames[x]).put(clusterNames[y], 0.0);
                } else if (x == minX || y == minX) {
                    if (x == minX) {
                        double sum = 0;
                        sum = originalDist.get(prevNames[minX]).get(clusterNames[y])
                                + originalDist.get(prevNames[minY]).get(clusterNames[y])
                                - originalDist.get(prevNames[minX]).get(prevNames[minY]);
                        sum = sum / (double) 2;
                        newClusterDist.get(clusterNames[x]).put(clusterNames[y], sum);
                    } else {
                        double sum = 0;
                        sum = originalDist.get(prevNames[minX]).get(clusterNames[x])
                                + originalDist.get(prevNames[minY]).get(clusterNames[x])
                                - originalDist.get(prevNames[minX]).get(prevNames[minY]);
                        sum = sum / (double) 2;
                        newClusterDist.get(clusterNames[x]).put(clusterNames[y], sum);
                    }
                } else {
                    newClusterDist.get(clusterNames[x]).put(clusterNames[y],
                            originalDist.get(clusterNames[x]).get(clusterNames[y]));
                }

            }
        }
        System.out.println();
        System.out.println();
        System.out.println("Number of Clusters: " + clusterNames.length);

        for (int x = 0; x < clusterNames.length; x++) {
            System.out.printf("%11s", clusterNames[x]);
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