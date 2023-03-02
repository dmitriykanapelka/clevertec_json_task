package example;

import java.util.List;
import java.util.Set;

public class Floor {

    private int number;
    private List<Integer> flatNumbers;
    private List<Passport> passports;

    @Override
    public String toString() {
        return "Floor{" +
                "number=" + number +
                ", flatNumbers=" + flatNumbers +
                ", passports=" + passports +
                '}';
    }
}
