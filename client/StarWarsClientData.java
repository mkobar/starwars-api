package org.acme;

import java.util.List;

public class PeopleResponse {
    public int count;
    public String next;
    public String previous;
    public List<Person> results;
}

public class Person {
    public String name;
    public String height;
    public String mass;
    public String hair_color;
    public String skin_color;
    public String eye_color;
    public String birth_year;
    public String gender;

    // Default constructor required for JSON-B
    public Person() {}

    public Person(String name, String height, String mass, String hair_color, String skin_color,
                  String eye_color, String birth_year, String gender) {
        this.name = name;
        this.height = height;
        this.mass = mass;
        this.hair_color = hair_color;
        this.skin_color = skin_color;
        this.eye_color = eye_color;
        this.birth_year = birth_year;
        this.gender = gender;
    }
}
