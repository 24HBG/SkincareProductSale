package com.example.demo.entity.request;

import com.example.demo.enums.SkinTypeEnum;
import lombok.Data;

@Data
public class AccountRequest {
    public String email;
    public String password;
    public String fullName;
    public String phonenumber;
    public String address;
    public SkinTypeEnum skintypeEnum;

}
