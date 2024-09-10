package ro.championsclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "cc", name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Size(max = 36)
    @Column(
            name = "name",
            unique = true,
            nullable = false,
            length = 32
    )
    private String name;

    @Column(
            name = "data",
            columnDefinition = "bytea",
            nullable = false
    )
    private byte[] data;

    @NotNull
    @Min(0)
    @Max(315)
    @Column(name = "width", nullable = false)
    private Integer width;

    @NotNull
    @Min(0)
    @Max(315)
    @Column(name = "height", nullable = false)
    private Integer height;

    @NotNull
    @Min(0)
    @Max(1048576)
    @Column(name = "size", nullable = false)
    private Long size;

    // relationships
    @OneToOne(mappedBy = "image")
    private Equipment equipment;

    @OneToOne(mappedBy = "image")
    private Subscription subscription;

}
