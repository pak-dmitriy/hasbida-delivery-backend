package com.delivery.habsida.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImages extends BaseEntity {

    @Column(name = "image_photo", nullable = false)
    private String imagePhoto;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public String getImagePhoto() {
        return imagePhoto;
    }

    public void setImagePhoto(String imagePhoto) {
        this.imagePhoto = imagePhoto;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
