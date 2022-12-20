package com.demo.redis.patterns.entity;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@ToString
@Table("Product")
@Builder
public class ProductEntity  implements  Serializable {


    @Column
    @Id
    private Integer id;
    @Column
    private String description;
    @Column
    private Double price;





}
