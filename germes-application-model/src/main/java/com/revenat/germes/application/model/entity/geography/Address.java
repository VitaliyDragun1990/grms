package com.revenat.germes.application.model.entity.geography;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Value type that stores address attributes
 * of the specific office or person
 *
 * @author Vitaliy Dragun
 */
@Embeddable
public class Address {

    private String zipCode;

    private String street;

    private String houseNo;

    /**
     * (Optional) Apartment number if it's address
     * of the apartment
     */
    private String apartment;

    @Column(name = "ZIP_CODE", length = 10, nullable = false)
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    @Column(name = "STREET", length = 32, nullable = false)
    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    @Column(name = "HOUSE_NO", length = 16, nullable = false)
    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(final String houseNo) {
        this.houseNo = houseNo;
    }

    @Column(name = "APARTMENT", length = 16)
    public String getApartment() {
        return apartment;
    }

    public void setApartment(final String apartment) {
        this.apartment = apartment;
    }
}
