**Describe the bug**
Javadoc description of the `@RequestPart` param of `multipart/form-data` request is treated as the description of the whole RequestBody, not as the description of the concrete part of the request.

**To Reproduce**
Steps to reproduce the behavior:
- spring-boot version: 2.7.5
- springdoc-openapi modules: 1.6.12
- therapi-runtime-javadoc version: 0.13.0

**HelloController.java**
```
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
            @Parameter(description = "item description (parameter")
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
```

**Actual result**
(requestBody part only)
```
  "requestBody": {
    "description": "item code",
    "content": {
      "multipart/form-data": {
        "schema": {
          "type": "object",
          "properties": {
            "code": {
              "type": "integer",
              "format": "int32"
            },
            "description": {
              "type": "string",
              "description": "item description (parameter"
            },
            "file": {
              "type": "string",
              "format": "binary"
            }
          }
        }
      }
    }
  }
```

**Expected result**
(requestBody part only)
```
  "requestBody": {
    "content": {
      "multipart/form-data": {
        "schema": {
          "type": "object",
          "properties": {
            "code": {
              "type": "integer",
              "format": "int32"
              "description": "item code"
            },
            "description": {
              "type": "string",
              "description": "item description (@parameter)"
            },
            "file": {
              "type": "string",
              "format": "binary"
              "description": "item model file"
            }
          }
        }
      }
    }
  }
```

**Additional context**
Despite this being related to the javadoc feature, it seems that the bug is somewhere in the `springdoc-openapi-common`, this is why I'm reporting it here.