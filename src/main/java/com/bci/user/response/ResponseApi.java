package com.bci.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseApi {

    @JsonProperty("error")
    List<ResponseApiMessage> responseApiMessageList = new ArrayList<>();

}
