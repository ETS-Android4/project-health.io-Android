package com.srvraj311.smart_health_management.Models;

import java.util.Set;

public class User {
    private String first_name;
    private String last_name;
    private String mobile_num;
    private String email;
    private String password;
    private String age;
    private String dog_tag;
    private Set<String> known_device;

    // We can use @SerializedName("key_name") for any field to change it in JSON

    public User() {
    }

    public User(String first_name, String last_name,
                String email, String password,
                String age,
                Set<String> known_device) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.mobile_num = "";
        this.age = age;
        this.dog_tag = "";
        this.known_device = known_device;
    }

    @Override
    public String toString() {
        return "User{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", mobile_num='" + mobile_num + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age='" + age + '\'' +
                ", dog_tag='" + dog_tag + '\'' +
                ", known_device=" + known_device +
                '}';
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDog_tag() {
        return dog_tag;
    }

    public void setDog_tag(String dog_tag) {
        this.dog_tag = dog_tag;
    }

    public Set<String> getKnown_device() {
        return known_device;
    }

    public void setKnown_device(Set<String> known_device) {
        this.known_device = known_device;
    }
}
