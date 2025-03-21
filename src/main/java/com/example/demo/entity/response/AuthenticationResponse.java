package com.example.demo.entity.response;

import com.example.demo.entity.Account;
import com.example.demo.enums.RoleEnum;
import com.example.demo.enums.SkinTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
//    private Long id;
//    private String email;
//    private String fullName;
//    private String phonenumber;
//    private String address;
//    private SkinTypeEnum skintypeEnum;
//    private long point;
    private Account user;
//    private RoleEnum roleEnum;
    private String accessToken;

}
