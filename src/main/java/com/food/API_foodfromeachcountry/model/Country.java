package com.food.API_foodfromeachcountry.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Country")
public class Country {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="name",length = 56,nullable = false)
    private String name;

    @Column(name="continent",length = 20,nullable = false)
    private String continent;
}
