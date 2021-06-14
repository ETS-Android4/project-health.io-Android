/*
 * Copyright (c) 2021 Sourabh , The following code is a part of Project health.io ,
 * All rights reserved to Sourabh (Srvraj311)
 *
 */

package com.srvraj311.smart_health_management.Models;

public class EmergencyCases {


    private String licence_id;
    private String name_of_patient;
    private String type_of_emergency;
    private String address;
    private String intensity_of_emergency;
    private String requirements;
    private String time;
    private String description;

    public EmergencyCases(String licence_id, String name_of_patient, String type_of_emergency, String address, String intensity_of_emergency, String requirements, String time, String description) {
        this.licence_id = licence_id;
        this.name_of_patient = name_of_patient;
        this.type_of_emergency = type_of_emergency;
        this.address = address;
        this.intensity_of_emergency = intensity_of_emergency;
        this.requirements = requirements;
        this.time = time;
        this.description = description;

    }

    public String getLicence_id() {
        return licence_id;
    }

    public void setLicence_id(String licence_id) {
        this.licence_id = licence_id;
    }

    public String getName_of_patient() {
        return name_of_patient;
    }

    public void setName_of_patient(String name_of_patient) {
        this.name_of_patient = name_of_patient;
    }

    public String getType_of_emergency() {
        return type_of_emergency;
    }

    public void setType_of_emergency(String type_of_emergency) {
        this.type_of_emergency = type_of_emergency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntensity_of_emergency() {
        return intensity_of_emergency;
    }

    public void setIntensity_of_emergency(String intensity_of_emergency) {
        this.intensity_of_emergency = intensity_of_emergency;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
