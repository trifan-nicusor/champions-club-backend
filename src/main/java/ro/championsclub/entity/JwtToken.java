package ro.championsclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("is_revoked <> true OR is_expired <> true")
@Table(schema = "cc", name = "jwt_token")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 256)
    @Column(
            name = "token",
            unique = true,
            nullable = false,
            length = 256
    )
    private String token;

    @Column(name = "is_revoked", nullable = false)
    private boolean isRevoked;

    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;

    @NotNull
    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // relationships
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

}
