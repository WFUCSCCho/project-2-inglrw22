/**
 * @file gdp2025.java
 * @description Represents an entry from the GDP dataset.
 * @author Ravi Ingle
 * @date October 21, 2025
 */

public class gdp2025 implements Comparable<gdp2025> {
    private String country; // this is the name of the country
    private int gdp; // this is the country's GDP value

    // Default constructor
    public gdp2025() {
        this.country = "";
        this.gdp = 0;
    }

    // Parametrized constructor
    public gdp2025(String country, int gdp) {
        this.country = country;
        this.gdp = gdp;
    }

    // Copy constructor
    public gdp2025(gdp2025 other) {
        this.country = other.country;
        this.gdp = other.gdp;
    }

    // returns the country name
    public String getCountry() {
        return country;
    }

    // returns the gdp value
    public int getGdp() {
        return gdp;
    }

    // This method is used to return a string representation for printing
    @Override
    public String toString() {
        return country + ": $" + gdp;
    }

    // This method is crucial for 'search' and 'remove'. It tells the BST
    // when two objects are considered equal based on their GDP value.
    @Override
    public boolean equals(Object obj) {
        // checks if the object is the exact same instance
        if (this == obj) return true;
        // checks if the object is null or not the same class
        if (obj == null || getClass() != obj.getClass()) return false;
        gdp2025 other = (gdp2025) obj;
        return gdp == other.gdp; // returns true if the gdp values are the same
    }

    // This method is the most important for the BST. It dictates how
    // objects are sorted and where they should be placed in the tree
    // based on their GDP.
    @Override
    public int compareTo(gdp2025 other) {
        return Integer.compare(this.gdp, other.gdp); // compares the gdp of this object to another
    }
}