/**
 * Contains all the data from IsoYearWaterData.txt for every country and year
 * @author Maya Rao
 * @version 10-15-2024
 *
 * @param isoYear           ISO code and year data was gathered
 * @param basicPlusPct      percentage of people with basic or better water supply
 * @param limitedPct        percentage of people with limited water supply
 * @param unimprovedPct     percentage of people with unimproved water supply
 * @param surfacePct        percentage of people with surface water supply
 */

public record YearlyWaterRecord(
        String isoYear,
        double basicPlusPct,
        double limitedPct,
        double unimprovedPct,
        double surfacePct
) implements Comparable<YearlyWaterRecord> {

    /**
     * Compares which isoYear comes first
     * @param other the object to be compared.
     * @return positive number if this.isoYear is greater other.isoYear, 0 if this.isoYear equals other.isoYear,
     * negative number if this.isoYear is less than other.isoYear
     */

    @Override
    public int compareTo(YearlyWaterRecord other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }

        // lexicographically compare the isoYear of two YearlyWaterRecords
        // using String compareTo method
        return this.isoYear.compareTo(other.isoYear);
    }

    /**
     * Checks if two YearlyWaterRecords are the same based on isoYear
     * @param other   the reference object with which to compare.
     * @return true if YearlyWaterRecords are the same, false if YearlyWaterRecords are not the same
     */

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        } else if (this == other) {
            return true;
        } else {
            YearlyWaterRecord otherRecord = (YearlyWaterRecord) other;
            // Must be "harmonious" with compareTo method;
            // clients expect that when .equals --> true, that .compareTo() --> 0;
            // they expect to use them interchangeably
            return compareTo(otherRecord) == 0;
        }
    }
}
