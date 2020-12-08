package com.timothybreitenfeldt.blog.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Table
@Data
public class User {

    public static enum Role {
        ROLE_ADMINISTRATOR, ROLE_AUTHOR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 35, unique = true)
    @Size(min = 5, max = 35)
    private String username;

    @Column(nullable = false, length = 60)
    @Size(min = 59, max = 60)
    private String password;

    @Column(nullable = false, length = 256, unique = true)
    @Size(min = 3, max = 256)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdOn;

}
