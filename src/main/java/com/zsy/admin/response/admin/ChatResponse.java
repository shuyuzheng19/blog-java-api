package com.zsy.admin.response.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {

    private ResponseMessage message;

    public static class ResponseMessage {
        private Content content;

        public static class Content {
            private List<String> parts;
        }
    }
}
