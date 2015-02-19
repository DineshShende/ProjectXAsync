package com.projectx.async.util.serializer;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class JsonDateDeSerializer extends JsonDeserializer<Date>
{
    @Override
    public Date deserialize(JsonParser jsonparser,
            DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        String date = jsonparser.getText();
        try {
            return format.parse(date);
        } catch (ParseException | java.text.ParseException e) {
            throw new RuntimeException(e);
        }

    }

}