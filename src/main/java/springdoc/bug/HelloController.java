package springdoc.bug;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class HelloController {
    /**
     * Creates some item.
     *
     * @param id          item ID
     * @param code        item code
     * @param description item description
     * @param file        item model file
     * @return Data of the created item.
     */
    @PostMapping(path = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ItemData createSomeItem(
            @PathVariable @NotNull Long id,
            @RequestPart(required = false) Integer code,
            @Parameter(description = "item description (@parameter)")
            @RequestPart(required = false) String description,
            @RequestPart(required = false) MultipartFile file) {
        return new ItemData(id, code, description, file);
    }

    /**
     * Item data
     */
    @lombok.Value
    public static class ItemData {
        /**
         * Item ID.
         */
        Long id;
        /**
         * Item code.
         */
        Integer code;
        /**
         * Item description.
         */
        String description;
        /**
         * Item model file.
         */
        MultipartFile file;
    }
}
