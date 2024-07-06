package top.funsite.spring.action.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.funsite.spring.action.Pet;

@RestController
@RequestMapping("/pet")
@Tag(name = "Pet", description = "宠物相关接口")
public class PetController {

    @Operation(summary = "通过 ID 查找宠物",
            // tags = {"Pet"},
            description = "当 0 < ID <= 10 时返回一只宠物。ID > 10 或非整数将模拟 API 错误情况",
            responses = {
                    @ApiResponse(description = "The pet", content = @Content(schema = @Schema(implementation = Pet.class))),
                    @ApiResponse(responseCode = "400", description = "提供的 ID 无效"),
                    @ApiResponse(responseCode = "404", description = "未找到宠物")
            })
    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPetById(
            @Parameter(
                    description = "需要领取的宠物 ID",
                    schema = @Schema(
                            type = "long",
                            description = "需要获取的宠物 ID",
                            allowableValues = {"1", "2", "3"}
                    ),
                    required = true)
            @PathVariable("petId") Long petId) {
        Pet pet = new Pet();
        return ResponseEntity.ok(pet);
    }
}
