package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;

public class IOFile {
    static Random rand = new Random();

    public static void readMatrixFromFile(int n, int m, int[][]matrix,String file) throws FileNotFoundException{
        Scanner s = new Scanner(new File(file));
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                matrix[i][j] = s.nextInt();
            //    System.out.print(matrix[i][j] + " ");
            }
      //      System.out.println();
        }
   //     System.out.println();
    }

    public static void writeMatrixInFile(int n, int m, String file) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                writer.print(rand.nextInt(10));
                writer.print(" ");
            }
            writer.println();
        }

        writer.close();
    }
}
