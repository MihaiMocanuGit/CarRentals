package Domain;

import Utils.ParsingHelper;

import java.io.Serializable;
import java.util.Objects;

public class Person implements Identifiable<String>, Serializable {

    private String familyName, firstName;
    private String id;

    public Person(String familyName, String firstName, String id)
    {
        this.familyName = familyName;
        this.firstName = firstName;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "familyName=" + familyName +
                ", firstName=" + firstName +
                ", id=" + id +
                '}';
    }
    static public Person parsePerson(String str)
    {
        if (str.indexOf("Person{") != 0)
            return null;

        if (!ParsingHelper.bracketsAreValid(str))
            return null;

        int startSearchIndex = "Person{".length();
        String familyNameStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "familyName=", ", ");
        if (familyNameStr == null)
            return null;
        startSearchIndex += familyNameStr.length();

        String firstNameStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "firstName=", ", ");
        if (firstNameStr == null)
            return null;
        startSearchIndex += firstNameStr.length();

        String idStr = ParsingHelper.getSubstringBetween2Strs(str, startSearchIndex, "id=", "}");
        if (idStr == null)
            return null;
        startSearchIndex += idStr.length();

        String familyName = familyNameStr;
        String firstName = firstNameStr;
        String id = idStr;

        return new Person(familyName, firstName, id);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /*
    @Override
    public void setId(String id) {
        this.id = id;
    }
     */

    @Override
    public String getId() {
        return id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
