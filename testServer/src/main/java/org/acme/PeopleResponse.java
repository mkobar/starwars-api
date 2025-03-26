package org.acme.mock;

import java.util.List; /**
 * Represents the response from the Star Wars API for people.
 */
public class PeopleResponse {
    public int count;
    public String next;
    public String previous;
    public List<Person> results;
}
