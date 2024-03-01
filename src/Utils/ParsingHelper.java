package Utils;

import Domain.Car;
import Domain.CarReservation;
import Domain.Person;
import Repository.CarRepoDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParsingHelper {
    public static String getSubstringBetween2Strs(String src, int startSearchIndex, String leftBound, String rightBound)
    {
        int start = src.indexOf(leftBound, startSearchIndex);
        if (start == -1)
            return null;
        start += leftBound.length();

        startSearchIndex = start;
        int end = src.indexOf(rightBound, startSearchIndex);
        if (end == -1)
            return null;

        return src.substring(start, end);
    }

    public static boolean bracketsAreValid(String str)
    {
        int bracketCount = 0;
        for (char ch : str.toCharArray())
        {
            if (ch == '{')
                bracketCount++;
            else if (ch == '}')
                bracketCount--;
        }
        return  (bracketCount == 0);
    }

    public static class ScopeGroup
    {
        public int startScopeIndex;
        public int endScopeIndex;

        public ScopeGroup(int startScopeIndex, int endScopeIndex)
        {
            this.startScopeIndex = startScopeIndex;
            this.endScopeIndex = endScopeIndex;


        }
    }
    public static ScopeGroup findClosestScopeGroup(String str, char scopeOpen, char scopeClose, int startSearchAtIndex,
                                                   int endSearchAtIndex)
    {

        int bracketCount = 0;
        int bound = Math.min(str.length(), endSearchAtIndex);

        while (startSearchAtIndex < bound) {
             if (str.charAt(startSearchAtIndex) == scopeOpen) {
                 bracketCount = 1;
                 break;
             }
             else {
                 startSearchAtIndex++;
             }
        }

        //no open bracket found
        if (startSearchAtIndex == bound)
            return null;


        for (int i = startSearchAtIndex + 1; i < bound; i++) {
            if (str.charAt(i) == scopeOpen) {
                bracketCount++;
            }
            else if (str.charAt(i) == scopeClose){
                bracketCount--;
                if (bracketCount == 0)
                    return new ScopeGroup(startSearchAtIndex, i);
            }
        }
        return null;
    }

    public static CarReservation parseCarReservation(ResultSet rs, Connection conn, int carId) throws SQLException {
        try (PreparedStatement statementCar = conn.prepareStatement("SELECT * from Cars where c_id = ?");)
        {

            statementCar.setInt(1, carId);
            ResultSet rsCar = statementCar.executeQuery();
            Car car = parseCar(rsCar);

            Person person = new Person(rs.getString("familyName"), rs.getString("firstName"),
                    rs.getString("personal_id"));
            if (person.getId() == null)
                return null;

            return new CarReservation(car.getId(), car, person);
        }
    }

    public static Car parseCar(ResultSet rs) throws SQLException {

        return new Car(rs.getInt("c_id"), rs.getBoolean("isReserved"), rs.getString("brand"),
                rs.getString("model"), rs.getInt("year"), rs.getFloat("mileage"), rs.getFloat("cost"));
    }

    public static class ParseException extends Exception
    {
        public ParseException(String message){
            super(message);
        }
    }

    public static class ParseError extends Error
    {
        public ParseError(String message){
            super(message);
        }
    }
}
