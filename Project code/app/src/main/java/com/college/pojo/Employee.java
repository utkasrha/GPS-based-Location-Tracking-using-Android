package com.college.pojo;

public class Employee {
    private String e_id;
    private String e_name;
    private String e_email;
    private String e_phone;
    private String e_address;
    private String e_city;
    private String e_state;
    private String e_dob;
    private String e_age;
    private String e_post;
    private String e_gender;

    public Employee(String e_id, String e_name, String e_email, String e_phone, String e_address, String e_city, String e_state, String e_dob, String e_age, String e_post, String e_gender) {
        this.e_id = e_id;
        this.e_name = e_name;
        this.e_email = e_email;
        this.e_phone = e_phone;
        this.e_address = e_address;
        this.e_city = e_city;
        this.e_state = e_state;
        this.e_dob = e_dob;
        this.e_age = e_age;
        this.e_post = e_post;
        this.e_gender = e_gender;
    }

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }

    public String getE_email() {
        return e_email;
    }

    public void setE_email(String e_email) {
        this.e_email = e_email;
    }

    public String getE_phone() {
        return e_phone;
    }

    public void setE_phone(String e_phone) {
        this.e_phone = e_phone;
    }

    public String getE_address() {
        return e_address;
    }

    public void setE_address(String e_address) {
        this.e_address = e_address;
    }

    public String getE_city() {
        return e_city;
    }

    public void setE_city(String e_city) {
        this.e_city = e_city;
    }

    public String getE_state() {
        return e_state;
    }

    public void setE_state(String e_state) {
        this.e_state = e_state;
    }

    public String getE_dob() {
        return e_dob;
    }

    public void setE_dob(String e_dob) {
        this.e_dob = e_dob;
    }

    public String getE_age() {
        return e_age;
    }

    public void setE_age(String e_age) {
        this.e_age = e_age;
    }

    public String getE_post() {
        return e_post;
    }

    public void setE_post(String e_post) {
        this.e_post = e_post;
    }

    public String getE_gender() {
        return e_gender;
    }

    public void setE_gender(String e_gender) {
        this.e_gender = e_gender;
    }
}
