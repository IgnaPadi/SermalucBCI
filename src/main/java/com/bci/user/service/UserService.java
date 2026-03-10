package com.bci.user.service;

import com.bci.user.common.EncodeDecode;
import com.bci.user.common.TokenGenerator;
import com.bci.user.entity.User;
import com.bci.user.repository.UserRepository;
import com.bci.user.response.ResponseApi;
import com.bci.user.response.ResponseApiMessage;
import com.bci.user.response.ResponseApiMessageEnum;
import com.bci.user.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    @Autowired UserRepository userRepository;
    private static final String BEARER = "Bearer ";

    /**
     * Se validan los valores de entrada y se crea registro de usuario
     * @param user
     * @return ResponseEntity<Object>
     */
    public ResponseEntity<Object> setUser(User user) {
        try {
            List<ResponseApiMessage> responseApiMessageList = new ArrayList<>();

            //validación de formato
            if(Optional.ofNullable(user.getEmail()).isEmpty() || Optional.ofNullable(user.getPassword()).isEmpty()){

                responseApiMessageList.add(new ResponseApiMessage(new Timestamp(System.currentTimeMillis()),
                        ResponseApiMessageEnum.ERROR_NOT_NULL.getCode(), ResponseApiMessageEnum.ERROR_NOT_NULL.getMessage()));

                return new ResponseEntity<>(new ResponseApi(responseApiMessageList), HttpStatus.BAD_REQUEST);
            }

            //validación de registro unico
            if(userRepository.findByEmail(user.getEmail()).isPresent()){

                responseApiMessageList.add(new ResponseApiMessage(new Timestamp(System.currentTimeMillis()),
                        ResponseApiMessageEnum.ERROR_USER_EXISTS.getCode(), ResponseApiMessageEnum.ERROR_USER_EXISTS.getMessage()));

                return new ResponseEntity<>(new ResponseApi(responseApiMessageList), HttpStatus.BAD_REQUEST);
            }

            //validación de negocio
            List<ResponseApiMessage> responseApiBusinessMessageList = UserValidation.businessvalidation(user);
            if(!responseApiBusinessMessageList.isEmpty()){
                return new ResponseEntity<>(new ResponseApi(responseApiBusinessMessageList), HttpStatus.BAD_REQUEST);
            }

            //Se crea y asigna token
            String token = TokenGenerator.getToken(user.getEmail());
            user.setToken(token);
            //Se usa encode Base64 para contraseña
            user.setPassword(EncodeDecode.passwordEncode(user.getPassword()));
            //se asigna usuario a entidad phone
            user.getPhones().forEach(i-> i.setUser(user));

            User userResponse = userRepository.save(user);
            userResponse.setPassword(EncodeDecode.passwordDecode(userResponse.getPassword())); //se decodifica password

            return ResponseEntity.ok(userResponse);

        }catch (Exception e){
            log.error("Error en setUser");
            e.printStackTrace();

            return new ResponseEntity<>(new ResponseApiMessage(new Timestamp(System.currentTimeMillis()),
                    HttpStatus.BAD_REQUEST.value(), ResponseApiMessageEnum.ERROR.getMessage()), HttpStatus.BAD_REQUEST);

        }

    }

    /**
     * Se inicia sesión con token
     * @param authorization
     * @return ResponseEntity<Object>
     */
    public ResponseEntity<Object> login(String authorization){
        try{
            String token = authorization.replace(BEARER, "");
            Optional<User> optionalUser = userRepository.findByToken(token);

            if(optionalUser.isPresent()){
                User userResponse = optionalUser.get();
                userResponse.setLastLogin(new Date()); // se agrega valor lastLogin
                userResponse.setToken(TokenGenerator.getToken(userResponse.getEmail())); //se actualiza token
                userRepository.save(userResponse);

                userResponse.setPassword(EncodeDecode.passwordDecode(userResponse.getPassword())); //se decodifica password

                return ResponseEntity.ok(userResponse);

            }else{
                List<ResponseApiMessage> responseApiMessageList = new ArrayList<>();

                responseApiMessageList.add(new ResponseApiMessage(new Timestamp(System.currentTimeMillis()),
                        ResponseApiMessageEnum.ERROR_USER_NOT_EXISTS.getCode(), ResponseApiMessageEnum.ERROR_USER_NOT_EXISTS.getMessage()));

                return new ResponseEntity<>(new ResponseApi(responseApiMessageList), HttpStatus.FORBIDDEN);
            }

        }catch (Exception e){
            log.error("error en login");
            e.printStackTrace();

            return new ResponseEntity<>(new ResponseApiMessage(new Timestamp(System.currentTimeMillis()),
                    HttpStatus.BAD_REQUEST.value(), ResponseApiMessageEnum.ERROR.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }
}
