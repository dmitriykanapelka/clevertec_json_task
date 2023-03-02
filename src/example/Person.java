package example;

import java.util.List;

public class Person {
    private String name;
    private Integer age;
    private List<Pet> pets;
    private Passport passport;
    private List<String> phones;

    public Person(String name, Integer age, List<Pet> pets, Passport passport, List<String> phones) {
        this.name = name;
        this.age = age;
        this.pets = pets;
        this.passport = passport;
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}
