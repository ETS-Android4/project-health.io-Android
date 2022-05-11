package com.srvraj311.smart_health_management.Booking;

import androidx.annotation.NonNull;

public class BookingModel {
    private String id;
    private String licence_id;
    private String date;
    private String time;
    private String patient_name;
    private String patient_age;
    private String patient_phone;
    private String booking_number;
    private String booking_timestamp;
    private String booking_status;
    private String email;



    private String hospital_name;


    private String booking_type;

    public BookingModel(String licence_id, String date, String time, String email, String patient_name,
                   String patient_age, String patient_phone, String booking_number,
                   String booking_timestamp,String booking_type, String hospital_name) {
        this.licence_id = licence_id;
        this.date = date;
        this.time = time;
        this.email = email;
        this.patient_name = patient_name;
        this.patient_age = patient_age;
        this.patient_phone = patient_phone;
        this.booking_number = booking_number;
        this.booking_timestamp = booking_timestamp;
        this.booking_type = booking_type;
        this.hospital_name = hospital_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicence_id() {
        return licence_id;
    }

    public void setLicence_id(String licence_id) {
        this.licence_id = licence_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(String patient_age) {
        this.patient_age = patient_age;
    }

    public String getPatient_phone() {
        return patient_phone;
    }

    public void setPatient_phone(String patient_phone) {
        this.patient_phone = patient_phone;
    }

    public String getBooking_number() {
        return booking_number;
    }

    public void setBooking_number(String booking_number) {
        this.booking_number = booking_number;
    }

    public String getBooking_timestamp() {
        return booking_timestamp;
    }

    public void setBooking_timestamp(String booking_timestamp) {
        this.booking_timestamp = booking_timestamp;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    @NonNull
    @Override
    public String toString() {
        return "BookingModel{" +
                "id='" + id + '\'' +
                ", licence_id='" + licence_id + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", patient_name='" + patient_name + '\'' +
                ", patient_age='" + patient_age + '\'' +
                ", patient_phone='" + patient_phone + '\'' +
                ", booking_number='" + booking_number + '\'' +
                ", booking_timestamp='" + booking_timestamp + '\'' +
                ", booking_status='" + booking_status + '\'' +
                ", email='" + email + '\'' +
                ", hospital_name='" + hospital_name + '\'' +
                ", booking_type='" + booking_type + '\'' +
                '}';
    }
}
