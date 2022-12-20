package com.demo.redis.patterns.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
@Table("serviceplan")
@Builder
public class ServicePlan {

    @Id
    private String bookingId;

    private String logisticId;

    public ServicePlan(String bookingId, String logisticId) {
        this.bookingId = bookingId;
        this.logisticId = logisticId;
    }

    public ServicePlan(){}
}
