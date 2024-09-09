package ro.championsclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import ro.championsclub.constant.EquipmentCategoryEnum;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE cc.equipment SET is_active = false WHERE id = ?")
@Table(schema = "cc", name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Size(max = 32)
    @Column(
            name = "name",
            unique = true,
            nullable = false,
            length = 32
    )
    private String name;

    @NotNull
    @Column(
            name = "category",
            nullable = false,
            length = 32
    )
    @Enumerated(EnumType.STRING)
    private EquipmentCategoryEnum category;

    @NotNull
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // relationships
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "image_id"
    )
    private Image image;

}
