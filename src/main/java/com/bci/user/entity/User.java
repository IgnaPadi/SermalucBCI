package com.bci.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue // UUID con Hibernate
    @Column(name = "id", updatable = false, nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se oculta en Swagger
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Se ignorará si viene en el JSON de entrada
    private UUID id;

    @Schema(example = "Juan Rodriguez")
    private String name;

    @Column(nullable = false, unique = true)
    @Schema(example = "juan@rodriguez.org")
    private String email;

    @Column(name = "password", nullable = false)
    @Schema(example = "Hunter2*", accessMode = Schema.AccessMode.WRITE_ONLY )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Phone> phones;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM d, yyyy HH:mm:ss a", timezone = "America/Santiago", locale = "es-CL")
    private Date created = new Date();

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM d, yyyy HH:mm:ss a", timezone = "America/Santiago", locale = "es-CL")
    private Date modified = new Date();

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM d, yyyy HH:mm:ss a", timezone = "America/Santiago", locale = "es-CL")
    private Date lastLogin = new Date();

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String token;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive = true;

    public User(String name, String email, String password, List<Phone> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }
}
