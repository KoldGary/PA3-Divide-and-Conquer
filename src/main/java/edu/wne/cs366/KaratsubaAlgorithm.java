package edu.wne.cs366;

import java.math.BigInteger;

/**
 * CS366 - PA3: Karatsuba Algorithm & Divide-and-Conquer
 * 
 * This assignment focuses on implementing the Karatsuba algorithm for fast integer multiplication
 * using the divide-and-conquer paradigm, alongside theoretical analysis problems.
 * 
 * @author Student Name
 * @date Due: October 23, 2025
 */
public class KaratsubaAlgorithm {
    
    /**
     * Implements the Karatsuba algorithm for multiplying two large integers.
     * 
     * @param x first integer to multiply
     * @param y second integer to multiply
     * @return the product of x and y
     */
    public static BigInteger karatsuba(BigInteger x, BigInteger y) {
        // Base case: use standard multiplication if small enough
        int THRESHOLD = 10; // cutoff for small numbers
        if (x.bitLength() < THRESHOLD || y.bitLength() < THRESHOLD) {
            return x.multiply(y);
        }

        int n = Math.max(getDigitCount(x), getDigitCount(y));
        int half = n / 2;

        // Split x and y into high and low parts
        BigInteger[] xParts = splitNumber(x, half);
        BigInteger[] yParts = splitNumber(y, half);

        BigInteger xHigh = xParts[0];
        BigInteger xLow = xParts[1];
        BigInteger yHigh = yParts[0];
        BigInteger yLow = yParts[1];

        // Recursive multiplications
        BigInteger A = karatsuba(xHigh, yHigh);
        BigInteger C = karatsuba(xLow, yLow);
        BigInteger B = karatsuba(xHigh.add(xLow), yHigh.add(yLow)).subtract(A).subtract(C);

        // Combine results: A*10^(2*half) + B*10^half + C
        BigInteger tenPowHalf = BigInteger.TEN.pow(half);
        BigInteger result = A.multiply(tenPowHalf.pow(2))
                             .add(B.multiply(tenPowHalf))
                             .add(C);

        return result;
    }
    
    /**
     * Helper method to get the number of digits in a BigInteger.
     */
    private static int getDigitCount(BigInteger num) {
        return num.abs().toString().length();
    }
    
    /**
     * Helper method to split a BigInteger into high and low parts.
     */
    private static BigInteger[] splitNumber(BigInteger num, int splitPosition) {
        BigInteger tenPow = BigInteger.TEN.pow(splitPosition);
        BigInteger high = num.divide(tenPow);
        BigInteger low = num.remainder(tenPow);
        return new BigInteger[]{high, low};
    }
    
    /**
     * Standard multiplication for comparison and verification.
     */
    public static BigInteger standardMultiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }
    
    /**
     * Main method for testing the Karatsuba implementation.
     */
    public static void main(String[] args) {
        System.out.println("CS366 - PA3: Karatsuba Algorithm Implementation");
        System.out.println("================================================");
        
        // Test cases
        BigInteger[] testCasesX = {
            new BigInteger("1234"),
            new BigInteger("5678"),
            new BigInteger("123456789"),
            new BigInteger("987654321"),
            new BigInteger("12345678901234567890")
        };
        
        BigInteger[] testCasesY = {
            new BigInteger("5678"),
            new BigInteger("1234"), 
            new BigInteger("987654321"),
            new BigInteger("123456789"),
            new BigInteger("98765432109876543210")
        };
        
        System.out.println("\nTesting Karatsuba Implementation:");
        System.out.println("---------------------------------");
        
        for (int i = 0; i < testCasesX.length; i++) {
            BigInteger x = testCasesX[i];
            BigInteger y = testCasesY[i];
            
            System.out.printf("\nTest %d: %s x %s\n", i + 1, x, y);
            
            // Time standard multiplication
            long startTime = System.nanoTime();
            BigInteger standardResult = standardMultiply(x, y);
            long standardTime = System.nanoTime() - startTime;
            
            // Time Karatsuba multiplication
            startTime = System.nanoTime();
            BigInteger karatsubaResult = karatsuba(x, y);
            long karatsubaTime = System.nanoTime() - startTime;
            
            System.out.printf("Standard result:  %s\n", standardResult);
            System.out.printf("Karatsuba result: %s\n", karatsubaResult);
            System.out.printf("Results match: %s\n", standardResult.equals(karatsubaResult));
            System.out.printf("Standard time:  %d ns\n", standardTime);
            System.out.printf("Karatsuba time: %d ns\n", karatsubaTime);
            
            if (karatsubaTime > 0) {
                double speedup = (double) standardTime / karatsubaTime;
                System.out.printf("Speedup factor: %.2fx\n", speedup);
            }
        }
        
        System.out.println("\n================================================");
        System.out.println("Complete the theoretical analysis in ANALYSIS.md");
        System.out.println("and hand-trace the algorithm using the provided worksheet.");
    }
}
