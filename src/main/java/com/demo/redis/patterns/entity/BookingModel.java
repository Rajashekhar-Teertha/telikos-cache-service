package com.demo.redis.patterns.entity;


import com.maersk.telikos.model.ServicePlan;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@Table("booking")
@Builder
public class BookingModel implements Persistable, Serializable {

    @Id
    private String bookingId;

    private ServicePlan servicePlan;

    private String logisticId;

    private String captureData;

    private String bookingStatus;

    private List<String> rules;

    private String validationMessage;

    private Boolean isFulfilment;

    @Transient
    private boolean newProduct;


    public BookingModel() {
    }

    public BookingModel(String bookingId, ServicePlan servicePlan, String logisticId, String captureData, String bookingStatus, List<String> rules, String validationMessage, Boolean isFulfilment, boolean newProduct) {
        this.bookingId = bookingId;
        this.servicePlan = servicePlan;
        this.logisticId = logisticId;
        this.captureData = captureData;
        this.bookingStatus = bookingStatus;
        this.rules = rules;
        this.validationMessage = validationMessage;
        this.isFulfilment = isFulfilment;
        this.newProduct = newProduct;
    }

    /**
     * @return bookingId
     */
    @Override
    public Object getId() {
        return bookingId;
    }

    @Override
    @Transient
    public boolean isNew() {
        return this.newProduct || bookingId == null;
    }

    public BookingModel setAsNew(){
        this.newProduct = true;
        return this;
    }

}
