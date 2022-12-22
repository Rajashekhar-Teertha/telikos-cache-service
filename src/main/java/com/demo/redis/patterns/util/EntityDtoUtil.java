package com.demo.redis.patterns.util;

import com.demo.redis.patterns.entity.BookingModel;
import com.maersk.telikos.model.Booking;

public class EntityDtoUtil {

    public static Booking toDto(BookingModel product) {
        Booking dto = new Booking();
    dto.setBookingId(product.getBookingId());
    dto.setBookingStatus(product.getBookingStatus());
    dto.setCaptureData(product.getCaptureData());
    dto.setIsFulfilment(product.getIsFulfilment());
    dto.setRules(product.getRules());
    dto.setLogisticId(product.getLogisticId());
    dto.setValidationMessage(product.getValidationMessage());
    dto.setServicePlan(null);

        return dto;
    }

  /*  public static ProductDto toDtoProduct(ProductEntity product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        return dto;
    }*/

    public static BookingModel toEntity(Booking dto) {

        return BookingModel.builder()
                .bookingId(dto.getBookingId())
                .bookingStatus(dto.getBookingStatus())
                .captureData(dto.getCaptureData())
                .isFulfilment(dto.getIsFulfilment())
                .logisticId(dto.getLogisticId())
                .rules(dto.getRules())
                .validationMessage(dto.getValidationMessage())
                .build();
    }

}
