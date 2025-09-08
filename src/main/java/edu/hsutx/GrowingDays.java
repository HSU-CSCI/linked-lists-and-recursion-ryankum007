package edu.hsutx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code GrowingDays} class calculates and tracks cumulative growing degree days
 * based on average daily temperatures and a specified threshold.
 */

public class GrowingDays {
    double [] degreeDays;
    double [] cumulativeDegreeDays;

    /**
     * Constructs a {@code GrowingDays} object with an array of average daily temperatures
     * and a threshold value. Calculates the degree days and cumulative degree days.
     *
     * @param degrees  An array of average daily temperatures.
     * @param threshold The temperature threshold for calculating growing degree days.
     */
    GrowingDays(double[] degrees, double threshold) {
    // make arrays for daily degree days and cumulative degree days
    this.degreeDays = new double[degrees.length];
    this.cumulativeDegreeDays = new double[degrees.length];

    double running = 0.0; // keeps track of total so far

    for (int i = 0; i < degrees.length; i++) {
        // daily degree day = temp - threshold (can be negative)
        this.degreeDays[i] = degrees[i] - threshold;

        // add to running total
        running += this.degreeDays[i];

        // total cannot go below 0
        if (running < 0) running = 0;

        // save the running total into the array
        this.cumulativeDegreeDays[i] = running;
    }
}


    /**
     * Constructs a {@code GrowingDays} object by reading average daily temperatures from
     * a CSV file and applying a threshold value. Uses the other constructor internally.
     *
     * @param csvFile  The name of the CSV file containing average daily temperatures.
     * @param threshold The temperature threshold for calculating growing degree days.
     */
    GrowingDays(String csvFile, double threshold) {
        // Read the CSV file and extract average temperatures into an array
        double[] avgTemperatures = readAvgTemperaturesFromCsv(csvFile);

        // Use the existing constructor
        GrowingDays growingDays = new GrowingDays(avgTemperatures, threshold);

        // Initialize the fields
        this.degreeDays = growingDays.degreeDays;
        this.cumulativeDegreeDays = growingDays.cumulativeDegreeDays;
    }

    /**
     * Reads the 'Avg' column from a CSV file into an array of doubles.
     *
     * @param csvFile The name of the CSV file containing average daily temperatures.
     * @return An array of average daily temperatures.
     */
    private double[] readAvgTemperaturesFromCsv(String csvFile) {
        String line = "";
        String csvSeparator = ",";

        // Create an ArrayList to hold the average values
        ArrayList<Double> avgValuesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Read the header line (skip it)
            br.readLine();

            // Read the CSV file line by line
            while ((line = br.readLine()) != null) {
                // Split the line into values
                String[] values = line.split(csvSeparator);

                // Assuming Avg is the third column (index 3 for the first Avg)
                double avg = Double.parseDouble(values[3]);
                avgValuesList.add(avg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the ArrayList to a double array
        double[] avgValuesArray = avgValuesList.stream().mapToDouble(Double::doubleValue).toArray();
        return avgValuesArray;
    }

    /**
     * Helper method to find the day when cumulative degree days first exceed the given threshold.
     *
     * @param left      The left index of the current search range.
     * @param right     The right index of the current search range.
     * @param threshold The cumulative degree days threshold to find.
     * @return The index of the first day where cumulative degree days exceed the threshold.
     */
    
    public int getDayOverCDHelper(int left, int right, double threshold) {
    // start with -1 (means not found yet)
    int ans = -1;

    // binary search between left and right
    while (left <= right) {
        int mid = left + (right - left) / 2; // middle index

        // if cumulative degree days at mid is >= threshold
        if (cumulativeDegreeDays[mid] >= threshold) {
            ans = mid;         // save this index as possible answer
            right = mid - 1;   // keep looking on the left side (earlier day)
        } else {
            left = mid + 1;    // otherwise look on the right side (later day)
        }
    }

    // return index of first day that reaches the threshold, or -1 if not found
    return ans;
}


    /**
     * Finds the first day where the cumulative degree days exceed the specified threshold.
     *
     * @param threshold The cumulative degree days threshold to find.
     * @return The index of the first day where cumulative degree days exceed the threshold,
     * or -1 if the threshold is not reached.
     */
    public int getDayOverCDThreshold(double threshold) {
        if (this.cumulativeDegreeDays[this.cumulativeDegreeDays.length-1] < threshold) return -1;
        return this.getDayOverCDHelper(0, degreeDays.length-1, threshold);
    }
}