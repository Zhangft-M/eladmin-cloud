package org.micah.security.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;
import org.micah.core.constant.Constants;
import org.micah.security.exception.CustomizeOAuth2Exception;

import java.io.IOException;

/**
 * @program: eladmin-cloud
 * @description: 自定义异常序列化
 * @author: Micah
 * @create: 2020-08-04 21:10
 **/
public class OAuth2ExceptionSerializer extends StdSerializer<CustomizeOAuth2Exception> {
    protected OAuth2ExceptionSerializer() {
        super(CustomizeOAuth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(CustomizeOAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("code", Constants.FAIL);
        jsonGenerator.writeStringField("msg", e.getMessage());
        jsonGenerator.writeStringField("data", e.getErrorCode());
        jsonGenerator.writeEndObject();
    }
}
