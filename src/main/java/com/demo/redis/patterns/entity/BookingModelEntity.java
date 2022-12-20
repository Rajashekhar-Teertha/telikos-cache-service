package com.demo.redis.patterns.entity;


import com.maersk.telikos.model.ServicePlan;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@Table("booking")
@Builder
public class BookingModelEntity{

    @Id
    private String bookingId;

    private ServicePlan servicePlan;

    private String logisticId;

    private String captureData;

    private String bookingStatus;

    private List<String> rules;

    private String validationMessage;

    private Boolean isFulfilment;

}
