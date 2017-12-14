package com.quicktutorialz.learnmicroservices.DemoTDD.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name="EMAIL")
    @NotBlank
    @Getter
    @Setter
    private String email;

    @Column(name="NAME")
    @NotBlank
    @Getter @Setter
    private String name;

    @Column(name="PASSWORD")
    @NotBlank
    @Getter @Setter
    private String password;

}