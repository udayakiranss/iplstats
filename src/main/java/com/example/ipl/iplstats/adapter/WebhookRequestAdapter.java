package com.example.ipl.iplstats.adapter;

import com.google.cloud.dialogflow.v2.WebhookRequest;
import com.google.cloud.dialogflow.v2.WebhookResponse;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;

public class WebhookRequestAdapter extends TypeAdapter<WebhookRequest> {
    @Override
    public void write(JsonWriter jsonWriter, WebhookRequest request) throws IOException {
        jsonWriter.jsonValue(JsonFormat.printer().print(request));

    }

    @Override
    public WebhookRequest read(JsonReader jsonReader) throws IOException {
        // Create a builder for the Person message
        WebhookRequest.Builder requestBuilder = WebhookRequest.newBuilder();
        // Use the JsonFormat class to parse the json string into the builder object
        // The Json string will be parsed fromm the JsonReader object
        JsonParser jsonParser = new JsonParser();
        JsonFormat.parser().merge(jsonParser.parse(jsonReader).toString(), requestBuilder);
        // Return the built Person message
        return requestBuilder.build();
    }
}
