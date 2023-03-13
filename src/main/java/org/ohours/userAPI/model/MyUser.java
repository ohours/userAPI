package org.ohours.userAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "MYUSER")
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;
    @Column(name = "USERNAME", unique = true)
    @NotNull
    @Length(min = 3, max = 64)
    @Pattern(regexp = "^[a-zA-Z]([._-](?![._-])|[\\w]){1,62}[\\w]$", message = "must start with a letter, .-_ permitted but not in first or last position and not consecutively, no special characters")
    private String username;
    @Column(name = "BIRTHDATE")
    @NotNull
    private LocalDate birthdate;
    @Column(name = "COUNTRY")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Country countryOfResidence;
    @Column(name = "PHONE")
    @Pattern(regexp = "^(0|\\+33|0033)[1-9][0-9]{8}$", message = "french number only, format (0|+33|0033)123456789")
    private String phoneNumber;
    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public MyUser(String username, LocalDate birthdate, Country countryOfResidence, String phoneNumber, Gender gender) {
        this.username = username;
        this.birthdate = birthdate;
        this.countryOfResidence = countryOfResidence;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public MyUser(Long id, String username, LocalDate birthdate, Country countryOfResidence, String phoneNumber, Gender gender) {
        this.id = id;
        this.username = username;
        this.birthdate = birthdate;
        this.countryOfResidence = countryOfResidence;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public MyUser() {
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", birthdate=" + birthdate + ", countryOfResidence=" + countryOfResidence + ", phoneNumber='" + phoneNumber + '\'' + ", gender=" + gender + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyUser myUser = (MyUser) o;
        return id.equals(myUser.id) && username.equals(myUser.username) && birthdate.equals(myUser.birthdate) && countryOfResidence == myUser.countryOfResidence && Objects.equals(phoneNumber, myUser.phoneNumber) && gender == myUser.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, birthdate, countryOfResidence, phoneNumber, gender);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Country getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(Country countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
