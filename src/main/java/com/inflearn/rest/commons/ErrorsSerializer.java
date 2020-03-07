package com.inflearn.rest.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        errors.getFieldErrors().forEach(fieldError -> {
                    try {
                        gen.writeStartObject();
                        gen.writeObjectField("field", fieldError.getField());
                        gen.writeObjectField("objectName", fieldError.getObjectName());
                        gen.writeObjectField("defaultMessage", fieldError.getDefaultMessage());
                        gen.writeObjectField("code", fieldError.getCode());
                        Object rejectedValue = fieldError.getRejectedValue();
                        if (rejectedValue != null) {
                            gen.writeObjectField("rejectedValue", rejectedValue.toString());
                        }
                        gen.writeEndObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        errors.getGlobalErrors().forEach(globalError -> {
                    try {
                        gen.writeStartObject();
                        gen.writeObjectField("objectName", globalError.getObjectName());
                        gen.writeObjectField("defaultMessage", globalError.getDefaultMessage());
                        gen.writeObjectField("code", globalError.getCode());
                        gen.writeEndObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        gen.writeEndArray();
    }
}
