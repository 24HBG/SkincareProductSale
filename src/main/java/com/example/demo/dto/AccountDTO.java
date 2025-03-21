package com.example.demo.dto;

import com.example.demo.entity.Account;
import com.example.demo.enums.RoleEnum;
import com.example.demo.enums.SkinTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class AccountDTO {
    private String email;
    private String fullName;
    private RoleEnum roleEnum;
    public SkinTypeEnum skintypeEnum;
    public String address;
    public String phoneNumber;

    public AccountDTO(Account account) {
        this.email = account.getEmail();
        this.fullName = account.getFullName();
        this.roleEnum = account.getRoleEnum();
        this.skintypeEnum = account.getSkintypeEnum();
        this.address = account.getAddress();
        this.phoneNumber = account.getPhonenumber();
    }
}
