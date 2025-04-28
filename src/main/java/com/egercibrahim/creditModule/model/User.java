package com.egercibrahim.creditModule.model;

import com.egercibrahim.creditModule.enums.Role;
import com.egercibrahim.creditModule.record.UserRecord;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "users_ukey_username", columnNames = {"username"})})
@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @SequenceGenerator(name = "users_seq_id", sequenceName = "users_seq_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "users_seq_id")
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserRecord toRecord() {
        return new UserRecord(
                this.username,
                this.role);
    }
}
