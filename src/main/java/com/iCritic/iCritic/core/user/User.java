package com.iCritic.iCritic.core.user;

import com.iCritic.iCritic.core.country.Country;
import com.iCritic.iCritic.core.enums.Role;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "users")
@DynamicUpdate
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String description;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private Role role = Role.DEFAULT;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    private transient Long countryId;

    private String passwordResetHash;

    private Date passwordResetDate;

    private String emailResetHash;

    private Date emailResetDate;

    private String newEmailReset;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
