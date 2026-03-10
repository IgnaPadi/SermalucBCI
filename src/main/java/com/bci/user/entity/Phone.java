package com.bci.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    @Id
    @GeneratedValue // UUID con Hibernate
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    private UUID id;

    @Schema(example = "1234567")
    private long number;

    @Schema(example = "1")
    private int citycode;

    @Schema(example = "57")
    private String countrycode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private User user;
}
