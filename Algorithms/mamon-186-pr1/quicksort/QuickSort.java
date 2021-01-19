package quicksort;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author: Elijah Joshua Mamon
 * @version 1.0, 6 February 2019
 * @since 1.0, 6 February 2019 
 * Filename: QuickSort.java
 * 
 * <p>
 * Elementary implementation of Quicksort that picks the last element and 
 * a Quicksort that has a randomized pivot picker.</p>
 *
 * @copyright Elijah Joshua Mamon 
 * Developed on NetBeans IDE 8.2
 */
public class QuickSort {

    public int pivot(int unsortedArray[], int biggerElement, int smallerElement) {
        int smallIndex = (smallerElement-1);
        int elementToPivot = unsortedArray[biggerElement];
        
        for (int i = smallerElement; i < biggerElement; i++) {
            if (unsortedArray[i] <= elementToPivot) {
                smallIndex++;
                
                int swap = unsortedArray[smallIndex];
                unsortedArray[smallIndex] = unsortedArray[i];
                unsortedArray[i] = swap;
            }
        }
        
        int swap = unsortedArray[smallIndex+1];
        unsortedArray[smallIndex+1] = unsortedArray[biggerElement];
        unsortedArray[biggerElement] = swap;
        
        return (smallIndex+1); 
    }
    
    public int randomPivot(int unsortedArray[], int biggerElement, int smallerElement){
        int randomized = (int)(smallerElement + (Math.random()*((biggerElement - smallerElement) + 1)));
        
        int swap = unsortedArray[randomized];
        unsortedArray[randomized] = unsortedArray[biggerElement];
        unsortedArray[biggerElement] = swap;
        
        return pivot(unsortedArray, biggerElement, smallerElement);
    }
    
    public void quickSort(int unsortedArray[], int biggerElement, int smallerElement){
        if (smallerElement < biggerElement) {
            int index = pivot(unsortedArray, biggerElement, smallerElement);
            
            quickSort(unsortedArray, index-1, smallerElement);
            quickSort(unsortedArray, biggerElement, index+1);
        }
    }
    
    public void randomQuickSort(int unsortedArray[], int biggerElement, int smallerElement){
        if (smallerElement < biggerElement) {
            int index = randomPivot(unsortedArray, biggerElement, smallerElement);
            
            randomQuickSort(unsortedArray, index-1, smallerElement);
            randomQuickSort(unsortedArray, biggerElement, index+1);
        }
    }
    
    static void print(int sortedArray[]){
        int n = sortedArray.length;
        for (int i = 0; i < n; ++i) {
            System.out.println(sortedArray[i] + " ");
        }
        System.out.println("");
    }
    
    public static void writeToFile(int sortedArray[], int numberOfInputs, long time,
            long timeRand)throws IOException{
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("sorted_output_" 
                + numberOfInputs +"items.txt"))) {
            toFile.write(numberOfInputs + " items");
            toFile.newLine();
            
            toFile.write("Quick Sort Time: " + time + "ns");
            toFile.newLine();
            
            toFile.write("Quick Sort Time with random pivoting: " + timeRand + "ns");
            toFile.newLine();
            
            for (int i = 0; i < sortedArray.length; i++) {
                toFile.write(String.valueOf(sortedArray[i]));
                toFile.newLine();
            }
        }
    }
    
    public static void main(String[] args) throws Exception{
        long beginningTime = 0;
        long endingTime = 0;
        long beginningTimeRand = 0;
        long endingTimeRand = 0;
        
        int currInput = 0;
        int numOfInputs = 0;
        int inputCounter = 0;
        
        File inputFile = new File(args[0]);
        
        Scanner inputScanner;
        try (Scanner total = new Scanner(inputFile)) {
            inputScanner = new Scanner(inputFile);
            while(total.hasNextInt()){
                numOfInputs++;
                total.nextInt();
            }
            
            total.close();
        }
        
        int unsortedArray[] = new int[numOfInputs];
        int unsortedArrayRand[] = new int[numOfInputs];
        
        while(inputScanner.hasNextInt()){
            currInput = inputScanner.nextInt();
            unsortedArray[inputCounter] = currInput;
            unsortedArrayRand[inputCounter] = currInput;
            inputCounter++;
        }
        
        inputScanner.close();
        
        QuickSort sort = new QuickSort();
        QuickSort sortRand = new QuickSort();
        
        beginningTime = System.nanoTime();
        sort.quickSort(unsortedArray, (unsortedArray.length-1), 0);
        endingTime = System.nanoTime();
        
        System.out.println("List of: " + numOfInputs + " items");
        System.out.println("Quick Sort: " + (endingTime-beginningTime) + "ns / " 
                + (endingTime-beginningTime)/(double)(1000000000) + " seconds" );
        
        beginningTimeRand = System.nanoTime();
        sortRand.randomQuickSort(unsortedArrayRand, (unsortedArrayRand.length-1), 0);
        endingTimeRand = System.nanoTime();
        
        System.out.println("Quick Sort Random: " + (endingTimeRand-beginningTimeRand) 
                + "ns / " + (endingTimeRand-beginningTimeRand)/(double)(1000000000) + " seconds");
        
        System.out.println("Output printed in: sorted_output_" +
                + numOfInputs + "items.txt");
        
        writeToFile(unsortedArrayRand, numOfInputs, endingTime-beginningTime, 
                endingTimeRand-beginningTimeRand);
    }
}
