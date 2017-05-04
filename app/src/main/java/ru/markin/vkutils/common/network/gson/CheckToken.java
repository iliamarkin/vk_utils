package ru.markin.vkutils.common.network.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

public class CheckToken {

    @SerializedName("response")
    @Expose
    @Getter
    private Response response;

    public static class Response {
        @SerializedName("success")
        @Expose
        @Getter
        private int success;

        @SerializedName("user_id")
        @Expose
        @Getter
        private long userId;

        @SerializedName("date")
        @Expose
        @Getter
        private long date;

        @SerializedName("expire")
        @Expose
        @Getter
        private long expire;
    }
}