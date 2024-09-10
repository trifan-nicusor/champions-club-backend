package ro.championsclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.championsclub.constant.EmailRegex;
import ro.championsclub.constant.UserRoleEnum;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "cc", name = "app_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Email(
            regexp = EmailRegex.EXPRESSION,
            message = "invalid email format"
    )
    @NotBlank
    @Size(max = 64)
    @Column(
            name = "email",
            unique = true,
            nullable = false,
            length = 64
    )
    private String email;

    @NotBlank
    @Size(max = 64)
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @NotBlank
    @Size(max = 32)
    @Column(name = "first_name", nullable = false, length = 32)
    private String firstName;

    @NotBlank
    @Size(max = 32)
    @Column(name = "last_name", nullable = false, length = 32)
    private String lastName;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    private UserRoleEnum role = UserRoleEnum.USER;

    @NotNull
    @Builder.Default
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @NotNull
    @Builder.Default
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = false;

    @NotNull
    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    // relationships
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<JwtToken> jwtTokens;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<UuidToken> uuidTokens;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private Wishlist wishlist;

    // UserDetails interface method override
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
