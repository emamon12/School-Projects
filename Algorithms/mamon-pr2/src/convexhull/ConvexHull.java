package convexhull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.util.Collections.emptyList;

class Point implements Comparable<Point> {

    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Point pt) {
        return Integer.compare(x, pt.x);
    }
}

/**
 * @author: Elijah Joshua Mamon
 * @version 1.0, 25 March 2019
 * @since 1.0, 25 March 2019 Filename: ConvexHull.java
 *
 * <p>
 * Uses Graham Scan, Jarvis March, and Quick Hull to find the convex hull of a
 * set of points.</p>
 *
 * @copyright Elijah Joshua Mamon Developed on NetBeans IDE 8.2
 */
public class ConvexHull {

    private static List<Point> grahamScan(List<Point> curr) {
        if (curr.isEmpty()) {
            return emptyList();
        }

        curr.sort(Point::compareTo);

        List<Point> gScan = new ArrayList<>();

        curr.stream().map((p) -> {
            while (gScan.size() >= 2 && !(findOrientation(gScan.get(gScan.size() - 2), gScan.get(gScan.size() - 1), p) == 2)) {
                gScan.remove(gScan.size() - 1);
            }
            return p;
        }).forEachOrdered((p) -> {
            gScan.add(p);
        });

        int upperHalf = gScan.size() + 1;
        for (int i = curr.size() - 1; i >= 0; i--) {
            Point currentPoint = curr.get(i);
            while (gScan.size() >= upperHalf && !(findOrientation(gScan.get(gScan.size() - 2), gScan.get(gScan.size() - 1), currentPoint) == 2)) {
                gScan.remove(gScan.size() - 1);
            }
            gScan.add(currentPoint);
        }

        gScan.remove(gScan.size() - 1);
        return gScan;
    }

    //Jarvis March takes collinear pts to escape further problems with input randomizer
    public static void jarvisMarch(Point[] pts, int numInputs) {
        if (numInputs < 3) {
            return;
        }

        Vector<Point> cHull = new Vector<>();

        int left = 0;
        for (int i = 1; i < numInputs; i++) {
            if (pts[i].x < pts[left].x) {
                left = i;
            }
        }

        int smallest = left, largest;
        do {
            cHull.add(pts[smallest]);

            largest = (smallest + 1) % numInputs;

            for (int i = 0; i < numInputs; i++) {
                if (findOrientation(pts[smallest], pts[i], pts[largest])
                        == 2) {
                    largest = i;
                }
            }
            smallest = largest;

        } while (smallest != left);
    }

    public ArrayList<Point> quickHull(ArrayList<Point> pts) {
        ArrayList<Point> qh = new ArrayList<>();

        if (pts.size() < 3) {
            return pts;
        }

        int minXValue = Integer.MAX_VALUE;
        int maxXValue = Integer.MIN_VALUE;

        int min = 0;
        int max = 0;

        for (int i = 0; i < pts.size(); i++) {
            if (pts.get(i).x < minXValue) {
                minXValue = pts.get(i).x;
                min = i;
            }
            if (pts.get(i).x > maxXValue) {
                maxXValue = pts.get(i).x;
                max = i;
            }
        }

        Point p1 = pts.get(min);
        Point p2 = pts.get(max);

        qh.add(p1);
        qh.add(p2);

        pts.remove(p1);
        pts.remove(p2);

        ArrayList<Point> leftHand = new ArrayList<>();
        ArrayList<Point> rightHand = new ArrayList<>();

        for (int i = 0; i < pts.size(); i++) {
            Point curr = pts.get(i);
            if (findOrientation(p1, p2, curr) == 2) {
                leftHand.add(curr);
            } else if (findOrientation(p1, p2, curr) == 1) {
                rightHand.add(curr);
            }
        }

        findHull(p1, p2, rightHand, qh);
        findHull(p1, p2, leftHand, qh);

        return qh;
    }

    //start of auxillary functions
    public static Point findSmallestPoint(List<Point> pointList) {
        Point smallest = pointList.get(0);

        for (int i = 1; i < pointList.size(); i++) {
            Point tempSmallest = pointList.get(i);

            if (tempSmallest.y < smallest.y || (tempSmallest.x < smallest.x
                    && tempSmallest.y < smallest.y)) {

                smallest = tempSmallest;
            }
        }
        return smallest;
    }

    //0 = collinear, 1 = clockwise, 2 = counterclockwise
    public static int findOrientation(Point p1, Point p2, Point p3) {
        int diff;

        diff = (p2.y - p1.y) * (p3.x - p2.x) - (p2.x - p1.x) * (p3.y - p2.y);

        if (diff == 0) {
            return 0;
        } else if (diff > 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public int findDistance(Point p1, Point p2, Point p3) {
        int p1YToP2Y = p2.y - p1.y;
        int p1XToP2X = p2.x - p1.x;

        int difference = p1XToP2X * (p1.y - p3.y) - p1YToP2Y * (p1.x - p3.x);

        return Math.abs(difference);
    }

    public void findHull(Point p1, Point p2, ArrayList<Point> hullSet, ArrayList<Point> hull) {
        int insertAt = hull.indexOf(p2);

        if (hullSet.isEmpty()) {
            return;
        }

        if (hullSet.size() == 1) {
            Point curr = hullSet.get(0);
            hullSet.remove(curr);
            hull.add(insertAt, curr);
            return;
        }

        int prevDist = Integer.MIN_VALUE;
        int furthest = 0;
        for (int i = 0; i < hullSet.size(); i++) {
            Point curr = hullSet.get(i);
            int currDistance = findDistance(p1, p2, curr);
            if (currDistance > prevDist) {
                prevDist = currDistance;
                furthest = i;
            }
        }

        Point furthestPoint = hullSet.get(furthest);
        hullSet.remove(furthest);
        hull.add(insertAt, furthestPoint);

        ArrayList<Point> leftAToFurthest = new ArrayList<>();
        for (int i = 0; i < hullSet.size(); i++) {
            Point curr = hullSet.get(i);

            if (findOrientation(p1, furthestPoint, curr) == 1) {
                leftAToFurthest.add(curr);
            }
        }

        ArrayList<Point> leftFurthestToB = new ArrayList<>();
        for (int i = 0; i < hullSet.size(); i++) {
            Point curr = hullSet.get(i);

            if (findOrientation(furthestPoint, p2, curr) == 1) {
                leftFurthestToB.add(curr);
            }
        }

        findHull(p1, furthestPoint, leftAToFurthest, hull);
        findHull(furthestPoint, p2, leftFurthestToB, hull);
    }

    //make random points
    public static void randomize(int n) throws IOException {
        Random rand = new Random();

        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("input" + n + ".txt"))) {
            for (int i = 0; i < n; i++) {
                toFile.write(String.valueOf(rand.nextInt(n) + 1));
                toFile.write(" ");
                toFile.write(String.valueOf(rand.nextInt(n) + 1));
                toFile.newLine();
            }
        }
    }

    public static void randomWorstCase(int numPoints) throws IOException {
        double radius = 300;

        Point origin = new Point(0, 0);

        double polar = 2 * Math.PI / numPoints;

        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("worst_case_input" + numPoints + ".txt"))) {
            for (int i = 0; i < numPoints; i++) {
                double angle = polar * i;
                int circleX = (int) (origin.x + radius * Math.cos(angle));
                int circleY = (int) (origin.y + radius * Math.sin(angle));
                toFile.write(String.valueOf(circleX));
                toFile.write(" ");
                toFile.write(String.valueOf(circleY));
                toFile.newLine();
            }
        }
    }

    public static void writeToFile(List<Point> arrayLst, int numberOfInputs,
            long startTimeQH, long endTimeQH,
            long startTimeJM, long endTimeJM,
            long startTimeGS, long endTimeGS) throws IOException {
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("set"
                + numberOfInputs + ".txt"))) {
            toFile.write(numberOfInputs + " items");
            toFile.newLine();
            toFile.write("Number Of Outputs: " + arrayLst.size());
            toFile.newLine();
            toFile.write("Graham Scan: " + (endTimeGS - startTimeGS) / (double) (1000000000) + " seconds");
            toFile.newLine();
            toFile.write("Jarvis March: " + (endTimeJM - startTimeJM) / (double) (1000000000) + " seconds");
            toFile.newLine();
            toFile.write("QuickHull: " + (endTimeQH - startTimeQH) / (double) (1000000000) + " seconds");
            toFile.newLine();

            for (int i = 0; i < arrayLst.size(); i++) {
                toFile.write(String.valueOf(arrayLst.get(i).x));
                toFile.write(" ");
                toFile.write(String.valueOf(arrayLst.get(i).y));
                toFile.newLine();
            }
        }
    }

    public static void writeToFileJM(long timeArray[], int numberOfItems) throws IOException {
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("Jarvis_March" + (numberOfItems) + ".txt"))) {
            for (int i = 0; i < timeArray.length; i++) {
                toFile.write(String.valueOf(timeArray[i]));
                toFile.newLine();
            }
        }
    }

    public static void writeToFileGS(long timeArray[], int numberOfItems) throws IOException {
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("Graham_Scan" + (numberOfItems) + ".txt"))) {
            for (int i = 0; i < timeArray.length; i++) {
                toFile.write(String.valueOf(timeArray[i]));
                toFile.newLine();
            }
        }
    }

    public static void writeToFileQH(long timeArray[], int numberOfItems) throws IOException {
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("Quick_Hull" + (numberOfItems) + ".txt"))) {
            for (int i = 0; i < timeArray.length; i++) {
                toFile.write(String.valueOf(timeArray[i]));
                toFile.newLine();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String usrIn = null;
        boolean isNotValidInput = true;
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

        while (isNotValidInput) {
            System.out.println("Type: '1' to read from file \n"
                    + "Type: '2' to Randomly generate a graph");

            usrIn = userIn.readLine();

            switch (usrIn) {
                case "1":
                    isNotValidInput = false;
                    break;
                case "2":
                    isNotValidInput = false;
                    break;
                default:
                    System.out.println("Invalid Input.");
                    break;
            }
        }

        String fileName = null;
        File inputFile = null;

        boolean invalidFile = true;
        if (usrIn.equals("1")) {
            while (invalidFile) {
                System.out.println("Input File Name: ");
                fileName = userIn.readLine();
            }

            try (Scanner tot = new Scanner(inputFile = new File(fileName))) {
                invalidFile = false;
            } catch (Exception e) {
                System.out.println("Invalid File Name");
                invalidFile = true;
            }
        } else {
            System.out.println("How many elements? ");
            String elements = userIn.readLine();
            int numElements = Integer.parseInt(elements);
            randomize(numElements);

            inputFile = new File("input" + numElements + ".txt");
        }

        int numOfInputs = 0;

        Scanner inputScanner;
        try (Scanner total = new Scanner(inputFile)) {
            inputScanner = new Scanner(inputFile);
            while (total.hasNextInt()) {
                numOfInputs++;
                total.nextInt();
            }
            total.close();
        }
        numOfInputs = numOfInputs / 2;

        Point points[] = new Point[numOfInputs];
        ArrayList<Point> er = new ArrayList<>();
        List<Point> pts = new ArrayList<>();
        System.out.println(numOfInputs + " inputs");

        int currX;
        int currY;

        try (Scanner pointScanner = new Scanner(inputFile)) {

            int index = 0;

            System.out.println("Scanning...");
            while (pointScanner.hasNext()) {
                currX = pointScanner.nextInt();
                currY = pointScanner.nextInt();

                points[index] = new Point(currX, currY);
                Point p = new Point(currX, currY);

                er.add(index, p);
                pts.add(index, p);

                index++;
            }
            pointScanner.close();
            System.out.println("Scanning Done...");
        }
        ConvexHull ch = new ConvexHull();

        long beginningTimeJM = System.nanoTime();
        jarvisMarch(points, numOfInputs);
        long endingTimeJM = System.nanoTime();
        System.out.println("JM Done...");

        long beginningTimeQH = System.nanoTime();
        ArrayList<Point> quickHull = ch.quickHull(er);
        long endingTimeQH = System.nanoTime();
        System.out.println("QH Done...");

        long beginningTimeGS = System.nanoTime();
        List<Point> grahamScan = grahamScan(pts);
        long endingTimeGS = System.nanoTime();
        System.out.println("GS Done...");

        System.out.println("Graham Scan: " + (endingTimeGS - beginningTimeGS) + "ns / "
                + (endingTimeGS - beginningTimeGS) / (double) (1000000000) + " seconds");

        System.out.println("Quick Hull: " + (endingTimeQH - beginningTimeQH) + "ns / "
                + (endingTimeQH - beginningTimeQH) / (double) (1000000000) + " seconds");

        System.out.println("Jarvis March: " + (endingTimeJM - beginningTimeJM) + "ns / "
                + (endingTimeJM - beginningTimeJM) / (double) (1000000000) + " seconds");

        System.out.println("Number of Outputs: " + quickHull.size());
        //which algorithm writes to file
        writeToFile(quickHull, numOfInputs, beginningTimeQH, endingTimeQH, beginningTimeJM, endingTimeJM,
                beginningTimeGS, endingTimeGS);

        quickHull.clear();
        grahamScan.clear();
        userIn.close();
    }
}
