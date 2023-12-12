package ru.moore.task_management_systems.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ACCOUNTS")
@Where(clause = "deleted = false")
public class Account extends BaseEntity {

    @Column(name = "email", updatable = false)
    @Email()
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;

}