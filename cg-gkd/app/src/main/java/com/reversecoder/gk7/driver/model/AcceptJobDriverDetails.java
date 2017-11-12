package com.reversecoder.gk7.driver.model;

/**
 * Created by rashed on 3/2/16.
 */
public class AcceptJobDriverDetails {

    private String name = "";
    private String email = "";
    private String phone = "";
    private String photo = "";
    private String vehicle_regi_number = "";
    private String taxi_regi_number = "";
    private String taxi_make = "";
    private String taxi_model = "";
    private String taxi_color = "";
    private String taxi_seat = "";
    private String base_fare = "";
    private String permile_fare = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVehicle_regi_number() {
        return vehicle_regi_number;
    }

    public void setVehicle_regi_number(String vehicle_regi_number) {
        this.vehicle_regi_number = vehicle_regi_number;
    }

    public String getTaxi_regi_number() {
        return taxi_regi_number;
    }

    public void setTaxi_regi_number(String taxi_regi_number) {
        this.taxi_regi_number = taxi_regi_number;
    }

    public String getTaxi_make() {
        return taxi_make;
    }

    public void setTaxi_make(String taxi_make) {
        this.taxi_make = taxi_make;
    }

    public String getTaxi_model() {
        return taxi_model;
    }

    public void setTaxi_model(String taxi_model) {
        this.taxi_model = taxi_model;
    }

    public String getTaxi_color() {
        return taxi_color;
    }

    public void setTaxi_color(String taxi_color) {
        this.taxi_color = taxi_color;
    }

    public String getTaxi_seat() {
        return taxi_seat;
    }

    public void setTaxi_seat(String taxi_seat) {
        this.taxi_seat = taxi_seat;
    }

    public String getBase_fare() {
        return base_fare;
    }

    public void setBase_fare(String base_fare) {
        this.base_fare = base_fare;
    }

    public String getPermile_fare() {
        return permile_fare;
    }

    public void setPermile_fare(String permile_fare) {
        this.permile_fare = permile_fare;
    }

    @Override
    public String toString() {
        return "AcceptJobDriverDetails{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", vehicle_regi_number='" + vehicle_regi_number + '\'' +
                ", taxi_regi_number='" + taxi_regi_number + '\'' +
                ", taxi_make='" + taxi_make + '\'' +
                ", taxi_model='" + taxi_model + '\'' +
                ", taxi_color='" + taxi_color + '\'' +
                ", taxi_seat='" + taxi_seat + '\'' +
                ", base_fare='" + base_fare + '\'' +
                ", permile_fare='" + permile_fare + '\'' +
                '}';
    }
}