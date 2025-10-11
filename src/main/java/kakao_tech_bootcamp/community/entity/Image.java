package kakao_tech_bootcamp.community.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String filename;

    @Column(name="object_key", length = 1024, nullable = false)
    private String objectKey;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Enumerated(EnumType.ORDINAL)
    private ImageStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public Image(String filename, String objectKey, ImageStatus status) {
        this.filename = filename;
        this.objectKey = objectKey;
        this.status = status;
    }

    public void changeStatus(ImageStatus imageStatus) {
        this.status = imageStatus;
    }
}
