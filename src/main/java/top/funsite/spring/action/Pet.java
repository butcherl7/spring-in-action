package top.funsite.spring.action;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(name = "Pet", description = "My Pet")
public class Pet {

    @Schema(title = "唯一的 ID", description = "ID")
    private Long id;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "生日", pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
