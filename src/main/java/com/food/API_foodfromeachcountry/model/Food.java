package com.food.API_foodfromeachcountry.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Food")
public class Food {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="name",length = 50,nullable = false)
    private String name;

    @Column(name="price",nullable = false)
    private String price;

    @ManyToOne
    @JoinColumn(name = "country_id",nullable = false,foreignKey=@ForeignKey(name = "FK_COUNTRY_ID"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Country country;
}
