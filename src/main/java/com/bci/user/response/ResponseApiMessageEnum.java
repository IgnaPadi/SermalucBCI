package com.bci.user.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Clase Enum donde se asignan los mensajes de error con sus codigos
 */
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseApiMessageEnum {

    ERROR_USER_NOT_EXISTS("Usuario no existe", 1),
    ERROR("Error interno, comuniquese con equipo de soporte", 2),
    ERROR_NOT_NULL("Email y/o password son valores obligatorios", 3),
    ERROR_EMAIL("Email %1$s no cumple con el formato aaaaaaa@undominio.algo", 4),
    ERROR_PASSWORD("Password debe contener al menos una letra minúscula, una letra mayúscula, un número, un caracter especial [@#$%^&+=!*] y sin espacios. El largo permitido es entre 8 y 20", 5),
    ERROR_USER_EXISTS("Usuario ya existe, correo ya está registrado", 6);

    private String message;
    private int code;

    public String getMessage() {
        return message;
    }
    public int getCode() {
        return code;
    }
}