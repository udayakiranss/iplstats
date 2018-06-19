package com.example.ipl.iplstats.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.cloud.dialogflow.v2.WebhookRequest;
import com.google.cloud.dialogflow.v2.WebhookResponse;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;

public class WebhookRequestSerializer extends StdSerializer<WebhookRequest> {

    public WebhookRequestSerializer(Class<WebhookRequest> t) {
        super(t);
    }

    @Override
    public void serialize(WebhookRequest webhookRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeObject(JsonFormat.printer().print(webhookRequest));

    }
}
