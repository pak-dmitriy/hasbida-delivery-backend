package com.delivery.habsida.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "store")
public class Store extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "store_slug", nullable = false, unique = true)
    private String storeSlug;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_store_services", nullable = false)
    private TypeStoreServices typeStoreServices;

    @Column(name = "phone",  nullable = false)
    private String phone;

    @Column(name = "logo", nullable = false)
    private String logo;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "sns_link")
    private String snsLink;

    @Column(name = "last_order_number")
    private Long lastOrderNumber = 0L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreSlug() {
        return storeSlug;
    }

    public void setStoreSlug(String storeSlug) {
        this.storeSlug = storeSlug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeStoreServices getTypeStoreServices() {
        return typeStoreServices;
    }

    public void setTypeStoreServices(TypeStoreServices typeStoreServices) {
        this.typeStoreServices = typeStoreServices;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSnsLink() {
        return snsLink;
    }

    public void setSnsLink(String snsLink) {
        this.snsLink = snsLink;
    }

    public Long getLastOrderNumber() {
        return lastOrderNumber;
    }

    public void setLastOrderNumber(Long lastOrderNumber) {
        this.lastOrderNumber = lastOrderNumber;
    }
}
