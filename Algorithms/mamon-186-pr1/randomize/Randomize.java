package randomize;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * @author: Elijah Joshua Mamon
 * @version 1.0, 6 February 2019
 * @since 1.0, 6 February 2019 
 * Filename: Randomize.java
 * 
 * <p>
 * An algorithm that prints out random sets of integers.</p>
 *
 * @copyright Elijah Joshua Mamon 
 * Developed on NetBeans IDE 8.2
 */
public class Randomize {

    public static void writeToFile()throws IOException{
        int n = 1600000;
        Random rand = new Random();
        
        try (BufferedWriter toFile = new BufferedWriter(new FileWriter("input" + n + ".txt"))) {
            for (int i = 0; i < n; i++) {
                toFile.write(String.valueOf(rand.nextInt(2000000) + 1));
                toFile.newLine();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        writeToFile();
    }
}
