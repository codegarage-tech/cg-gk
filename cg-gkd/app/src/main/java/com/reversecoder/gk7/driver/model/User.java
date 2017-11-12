package com.reversecoder.gk7.driver.model;

/**
 * Created by rashed on 3/2/16.
 */
public class User {

    private String id = "";
    private String custom_id = "";
    private String customer_type = "";
    private String company_id = "";
    private String name = "";
    private String email = "";
    private String password = "";
    private String salt = "";
    private String validation_code = "";
    private String verification_status = "";
    private String photo = "";
    private String street = "";
    private String area_id = "";
    private String postal_code = "";
    private String mobile = "";
    private String phone = "";
    private String vehicle_regi_number = "";
    private String taxi_regi_number = "";
    private String taxi_model = "";
    private String taxi_brand = "";
    private String taxi_color = "";
    private String taxi_seat = "";
    private String base_fare = "";
    private String permile_fare = "";
    private String available_distance = "";
    private String availability = "";
    private String suspended_to = "";
    private String lat = "";
    private String lon = "";
    private String status = "";
    private String last_logged_in = "";
    private String created = "";
    private String modified = "";
    private String created_by = "";
    private String modified_by = "";
    private String userAreaName = "";
    private String userPhoto = "";
    private UserCompany userCompany = new UserCompany();
    private AverageMileAndBaseFare avgMileAndBaseFare = new AverageMileAndBaseFare();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAvailable_distance() {
        return available_distance;
    }

    public void setAvailable_distance(String available_distance) {
        this.available_distance = available_distance;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getSuspended_to() {
        return suspended_to;
    }

    public void setSuspended_to(String suspended_to) {
        this.suspended_to = suspended_to;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_logged_in() {
        return last_logged_in;
    }

    public void setLast_logged_in(String last_logged_in) {
        this.last_logged_in = last_logged_in;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getUserAreaName() {
        return userAreaName;
    }

    public void setUserAreaName(String userAreaName) {
        this.userAreaName = userAreaName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getCustom_id() {
        return custom_id;
    }

    public void setCustom_id(String custom_id) {
        this.custom_id = custom_id;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getValidation_code() {
        return validation_code;
    }

    public void setValidation_code(String validation_code) {
        this.validation_code = validation_code;
    }

    public String getVerification_status() {
        return verification_status;
    }

    public void setVerification_status(String verification_status) {
        this.verification_status = verification_status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getTaxi_model() {
        return taxi_model;
    }

    public void setTaxi_model(String taxi_model) {
        this.taxi_model = taxi_model;
    }

    public String getTaxi_brand() {
        return taxi_brand;
    }

    public void setTaxi_brand(String taxi_brand) {
        this.taxi_brand = taxi_brand;
    }

    public String getTaxi_color() {
        return taxi_color;
    }

    public void setTaxi_color(String taxi_color) {
        this.taxi_color = taxi_color;
    }

    public UserCompany getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(UserCompany userCompany) {
        this.userCompany = userCompany;
    }

    public AverageMileAndBaseFare getAvgMileAndBaseFare() {
        return avgMileAndBaseFare;
    }

    public void setAvgMileAndBaseFare(AverageMileAndBaseFare avgMileAndBaseFare) {
        this.avgMileAndBaseFare = avgMileAndBaseFare;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", custom_id='" + custom_id + '\'' +
                ", customer_type='" + customer_type + '\'' +
                ", company_id='" + company_id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", validation_code='" + validation_code + '\'' +
                ", verification_status='" + verification_status + '\'' +
                ", photo='" + photo + '\'' +
                ", street='" + street + '\'' +
                ", area_id='" + area_id + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", mobile='" + mobile + '\'' +
                ", phone='" + phone + '\'' +
                ", vehicle_regi_number='" + vehicle_regi_number + '\'' +
                ", taxi_regi_number='" + taxi_regi_number + '\'' +
                ", taxi_model='" + taxi_model + '\'' +
                ", taxi_brand='" + taxi_brand + '\'' +
                ", taxi_color='" + taxi_color + '\'' +
                ", taxi_seat='" + taxi_seat + '\'' +
                ", base_fare='" + base_fare + '\'' +
                ", permile_fare='" + permile_fare + '\'' +
                ", available_distance='" + available_distance + '\'' +
                ", availability='" + availability + '\'' +
                ", suspended_to='" + suspended_to + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", status='" + status + '\'' +
                ", last_logged_in='" + last_logged_in + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", created_by='" + created_by + '\'' +
                ", modified_by='" + modified_by + '\'' +
                ", userAreaName='" + userAreaName + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", userCompany=" + userCompany +
                ", avgMileAndBaseFare=" + avgMileAndBaseFare +
                '}';
    }
}
