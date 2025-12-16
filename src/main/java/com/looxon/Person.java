package com.looxon;

import java.time.LocalDate;

public class Person {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private boolean active;

    // konstruktor, gettery i settery
    public Person() {}

    public Person(String firstName, String lastName, String email,
                  LocalDate birthDate, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.active = active;
    }

    // gettery i settery...
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}