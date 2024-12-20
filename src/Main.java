import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // initializing arrays
        String[] countries = null;
        String[] isoCodes = null;
        int[] years = null;
        SortedArrayList<YearlyWaterRecord> waterData = new SortedArrayList<>();

        // reading CountriesAndIsoCodes
        try {
            // java accessing CountriesAndIsoCodes.txt
            File countriesAndIsoCodes = new File("CountriesAndIsoCodes.txt");

            // scanner used to read file
            Scanner scan = new Scanner(countriesAndIsoCodes);

            // read number of countries and codes
            int count = Integer.parseInt(scan.nextLine().trim());

            // making arrays of size count
            countries = new String[count];
            isoCodes = new String[count];

            // skip header
            scan.nextLine();

            // read each line, split at #, store in arrays
            int index = 0;
            while (scan.hasNextLine() && index < count) {
                String line = scan.nextLine();
                String[] parts = line.split("#");
                countries[index] = parts[0]; // country name
                isoCodes[index] = parts[1]; // iso code
                index++;
            }

            // close scanner
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        // reading IsoYearWaterData
        try {
            // java accessing IsoYearWaterData.txt
            File isoYearWaterData = new File("IsoYearWaterData.txt");

            // scanner used to read file
            Scanner scan = new Scanner(isoYearWaterData);

            // skip header
            scan.nextLine();

            // making years list large because we don't know how many years-worth of data we have
            years = new int[100];

            // counting number of unique years inside int[] years
            int uniqueYearsCount = 0;

            // read each line, split at ",", store in waterData
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] parts = line.split(",");

                String isoYear = parts[0];

                // take the last chunk of isoYear and save as year
                int year = Integer.parseInt(isoYear.substring(3));

                // change values in each column if they aren't proper doubles (ex: >99, <1, -)
                double basicPlusPct = changeValue(parts[1]);
                double limitedPct = changeValue(parts[2]);
                double unimprovedPct = changeValue(parts[3]);
                double surfacePct = changeValue(parts[4]);

                // store data from IsoYearWaterData into YearlyWaterRecord
                YearlyWaterRecord record = new YearlyWaterRecord(isoYear, basicPlusPct, limitedPct, unimprovedPct,
                                                                 surfacePct);

                // add YearlyWaterRecord to waterData
                waterData.add(record);

                // if the years array doesn't contain the specified year after iterating from 0 to uniqueYearsCount
                if (!contains(years, year, uniqueYearsCount)) {
                    // at index uniqueYearsCount add the specified year to the years array
                    years[uniqueYearsCount] = year;

                    // increase the uniqueYearsCount
                    uniqueYearsCount++;
                }
            }

            // adjust years array to correct size (uniqueYearsCount)
            years = Arrays.copyOf(years, uniqueYearsCount);

            // close scanner
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        // instantiating gui with parallel arrays
        new WaterComparisonGui(waterData, countries, isoCodes, years);
    }

    // changing non-double values to their correct interpretations
    private static double changeValue(String value) {
        switch (value) {
            case ">99":
                return 100.0;
            case "<1":
                return 0.0;
            case "-":
                return -1.0;
            default:
                // read the value in the split up line as a normal double
                return Double.parseDouble(value);
        }
    }

    // check if an array of ints contains a specified value and return true or false
    private static boolean contains(int[] array, int value, int count) {
        for (int i = 0; i < count; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
}
