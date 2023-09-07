package com.pu.programus.sign.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {

    @ApiParam(value = "ID", required = true)
    @NotBlank
    private String id;

    @ApiParam(value = "Password", required = true)
    @NotBlank
    private String password;
}