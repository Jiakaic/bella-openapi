package com.ke.bella.openapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonSchema implements Serializable {
    private static final long serialVersionUID = 1L;
    private Set<TypeSchema> params;
    @SuppressWarnings("unchecked")
    public static JsonSchema toSchema(Class<?> type) {
        try {
            Map<String, String> desc;
            if(IDescription.class.isAssignableFrom(type)) {
                desc = ((Class<? extends IDescription>)type).newInstance().description();
            } else {
                desc = new HashMap<>();
            }
            Set<TypeSchema> schemas = Arrays.stream(type.getDeclaredFields())
                    .map(field -> TypeSchema.toSchema(field, desc))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            return new JsonSchema(schemas);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
