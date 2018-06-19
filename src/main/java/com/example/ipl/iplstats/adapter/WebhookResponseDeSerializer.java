package com.example.ipl.iplstats.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.cloud.dialogflow.v2.WebhookResponse;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;

public class WebhookResponseDeSerializer extends StdDeserializer<WebhookResponse> {

    public WebhookResponseDeSerializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public WebhookResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        WebhookResponse.Builder builder = WebhookResponse.newBuilder();

        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        JsonFormat.parser().merge(jsonNode.toString(),builder);
        return  builder.build();
    }
}
