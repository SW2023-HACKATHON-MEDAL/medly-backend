package medal.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pill {
    @Id
    @GeneratedValue
    @Column(name = "pill_id")
    private Long id;
    private String name; // 이름
    private String color; // 색깔
    private String shape; // 모양
    private String texture; // 제형
    private String storeImgName; //사진 (로컬에 저장된 이름)

}
