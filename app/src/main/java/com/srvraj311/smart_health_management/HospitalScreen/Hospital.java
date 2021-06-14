package com.srvraj311.smart_health_management.HospitalScreen;

import java.util.HashMap;

public class Hospital {
    private String licence_id;
    private String name;
    private String description;
    private String contact;
    private String address;
    private String city_name;
    private String state_name;
    private String geolocation;
    private String type;
    private String grade;
    private String last_updated;
    private String no_of_bed;
    private String vacant_bed;
    private String icu;
    private String vacant_icu;
    private String ccu;
    private String vacant_ccu;
    private String ventilators;
    private String vacant_ventilators;

    public String getLicence_id() {
        return licence_id;
    }

    public void setLicence_id(String licence_id) {
        this.licence_id = licence_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getNo_of_bed() {
        return no_of_bed;
    }

    public void setNo_of_bed(String no_of_bed) {
        this.no_of_bed = no_of_bed;
    }

    public String getVacant_bed() {
        return vacant_bed;
    }

    public void setVacant_bed(String vacant_bed) {
        this.vacant_bed = vacant_bed;
    }

    public String getIcu() {
        return icu;
    }

    public void setIcu(String icu) {
        this.icu = icu;
    }

    public String getVacant_icu() {
        return vacant_icu;
    }

    public void setVacant_icu(String vacant_icu) {
        this.vacant_icu = vacant_icu;
    }

    public String getCcu() {
        return ccu;
    }

    public void setCcu(String ccu) {
        this.ccu = ccu;
    }

    public String getVacant_ccu() {
        return vacant_ccu;
    }

    public void setVacant_ccu(String vacant_ccu) {
        this.vacant_ccu = vacant_ccu;
    }

    public String getVentilators() {
        return ventilators;
    }

    public void setVentilators(String ventilators) {
        this.ventilators = ventilators;
    }

    public String getVacant_ventilators() {
        return vacant_ventilators;
    }

    public void setVacant_ventilators(String vacant_ventilators) {
        this.vacant_ventilators = vacant_ventilators;
    }

    public String getOxygen_cylinders() {
        return oxygen_cylinders;
    }

    public void setOxygen_cylinders(String oxygen_cylinders) {
        this.oxygen_cylinders = oxygen_cylinders;
    }

    public String getVacant_oxygen_cylinders() {
        return vacant_oxygen_cylinders;
    }

    public void setVacant_oxygen_cylinders(String vacant_oxygen_cylinders) {
        this.vacant_oxygen_cylinders = vacant_oxygen_cylinders;
    }

    public HashMap<String, String> getBlood_bank() {
        return blood_bank;
    }

    public void setBlood_bank(HashMap<String, String> blood_bank) {
        this.blood_bank = blood_bank;
    }

    public String getX_ray() {
        return x_ray;
    }

    public void setX_ray(String x_ray) {
        this.x_ray = x_ray;
    }

    public String getMri() {
        return mri;
    }

    public void setMri(String mri) {
        this.mri = mri;
    }

    public String getEcg() {
        return ecg;
    }

    public void setEcg(String ecg) {
        this.ecg = ecg;
    }

    public String getUltra_sound() {
        return ultra_sound;
    }

    public void setUltra_sound(String ultra_sound) {
        this.ultra_sound = ultra_sound;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }

    public String getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(String closing_time) {
        this.closing_time = closing_time;
    }

    public Boolean getIs_24_hr_service() {
        return is_24_hr_service;
    }

    public void setIs_24_hr_service(Boolean is_24_hr_service) {
        this.is_24_hr_service = is_24_hr_service;
    }

    public String getGeneral_fees() {
        return general_fees;
    }

    public void setGeneral_fees(String general_fees) {
        this.general_fees = general_fees;
    }

    public String getVacant_ambulance() {
        return vacant_ambulance;
    }

    public void setVacant_ambulance(String vacant_ambulance) {
        this.vacant_ambulance = vacant_ambulance;
    }

    private String oxygen_cylinders;
    private String vacant_oxygen_cylinders;
    private HashMap<String,String> blood_bank;
    private String x_ray;
    private String mri;
    private String ecg;
    private String ultra_sound;
    private String opening_time;
    private String closing_time;
    private Boolean is_24_hr_service;
    private String general_fees;
    private String vacant_ambulance;


    public Hospital(String licence_id, String name, String description, String contact, String address,
                    String city_name, String state_name, String geolocation, String type, String grade,
                    String last_updated, String no_of_bed, String vacant_bed, String icu, String vacant_icu,
                    String ccu, String vacant_ccu, String ventilators, String vacant_ventilators,
                    String oxygen_cylinders, String vacant_oxygen_cylinders, HashMap<String, String> blood_bank,
                    String x_ray, String mri, String ecg, String ultra_sound, String opening_time,
                    String closing_time, Boolean is_24_hr_service, String general_fees, String vacant_ambulance) {
        this.licence_id = licence_id;
        this.name = name;
        this.description = description;
        this.contact = contact;
        this.address = address;
        this.city_name = city_name;
        this.state_name = state_name;
        this.geolocation = geolocation;
        this.type = type;
        this.grade = grade;
        this.last_updated = last_updated;
        this.no_of_bed = no_of_bed;
        this.vacant_bed = vacant_bed;
        this.icu = icu;
        this.vacant_icu = vacant_icu;
        this.ccu = ccu;
        this.vacant_ccu = vacant_ccu;
        this.ventilators = ventilators;
        this.vacant_ventilators = vacant_ventilators;
        this.oxygen_cylinders = oxygen_cylinders;
        this.vacant_oxygen_cylinders = vacant_oxygen_cylinders;
        this.blood_bank = blood_bank;
        this.x_ray = x_ray;
        this.mri = mri;
        this.ecg = ecg;
        this.ultra_sound = ultra_sound;
        this.opening_time = opening_time;
        this.closing_time = closing_time;
        this.is_24_hr_service = is_24_hr_service;
        this.general_fees = general_fees;
        this.vacant_ambulance = vacant_ambulance;
    }
}
