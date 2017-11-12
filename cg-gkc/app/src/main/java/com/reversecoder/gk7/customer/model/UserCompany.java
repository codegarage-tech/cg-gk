package com.reversecoder.gk7.customer.model;

/**
 * Created by rashed on 3/2/16.
 */
public class UserCompany {

    private String name = "";
    private String logo = "";
    private String tradeLicenceNo = "";
    private String establishedDate = "";
    private String companyAreaName = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTradeLicenceNo() {
        return tradeLicenceNo;
    }

    public void setTradeLicenceNo(String tradeLicenceNo) {
        this.tradeLicenceNo = tradeLicenceNo;
    }

    public String getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(String establishedDate) {
        this.establishedDate = establishedDate;
    }

    public String getCompanyAreaName() {
        return companyAreaName;
    }

    public void setCompanyAreaName(String companyAreaName) {
        this.companyAreaName = companyAreaName;
    }
}
