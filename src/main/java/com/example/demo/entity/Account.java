package com.example.demo.entity;

import com.example.demo.enums.RoleEnum;
import com.example.demo.enums.SkinTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generate tự động id
    public long id;
    @NotBlank
    public String fullName;
    @NotBlank
    @Column(unique = true)
    public String email;
    @NotBlank
    public String password;
    @NotBlank
    public String phonenumber;
    @Enumerated(value = EnumType.STRING)
    public SkinTypeEnum skintypeEnum;
    @NotBlank
    public String address;
    public long point;
    @Enumerated(value = EnumType.STRING)
    public RoleEnum roleEnum;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
            @JsonIgnore
    List<Order> orders = new ArrayList<>();
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
            @JsonIgnore
    List<Blog> blogs = new ArrayList<>();
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
            @JsonIgnore
    List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "createdBy",cascade = CascadeType.ALL)
    List<DashBoard> dashBoards = new ArrayList<>();

    @Override
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    @JsonSubTypes({
            @Type(value = SimpleGrantedAuthority.class, name = "SimpleGrantedAuthority")
    })
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.roleEnum.toString()));
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return  this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
