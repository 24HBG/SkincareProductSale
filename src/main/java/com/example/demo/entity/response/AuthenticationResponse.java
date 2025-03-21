package com.example.demo.entity.response;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;
import com.example.demo.enums.RoleEnum;
import com.example.demo.enums.SkinTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationResponse {

    private AccountDTO user;
    private String token;

    public AuthenticationResponse(Account account, String token) {
        this.user = new AccountDTO(account); // Chuyển từ Account sang AccountDTO
        this.token = token;
    }
}
