package com.revenat.germes.application.model.entity.geography;

import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * Value type that stores address attributes
 * of the specific office or person
 *
 * @author Vitaliy Dragun
 */
@Embeddable
@Setter
public class Address {

    public static final String FIELD_ZIP_CODE = "zipCode";

    public static final String FIELD_STREET = "street";

    public static final String FIELD_HOUSE = "houseNo";

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

    @Column(name = "STREET", length = 32, nullable = false)
    public String getStreet() {
        return street;
    }

    @Column(name = "HOUSE_NO", length = 16, nullable = false)
    public String getHouseNo() {
        return houseNo;
    }

    @Column(name = "APARTMENT", length = 16)
    public String getApartment() {
        return apartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(zipCode, address.zipCode) &&
                Objects.equals(street, address.street) &&
                Objects.equals(houseNo, address.houseNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipCode, street, houseNo);
    }
}
