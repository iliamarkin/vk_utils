package ru.markin.vkutils.common.network.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

public class History {

    @SerializedName("response")
    @Expose
    @Getter
    private Response response;

    public static class Response {

        @SerializedName("count")
        @Expose
        @Getter
        private int count;

        @SerializedName("items")
        @Expose
        @Getter
        private List<Item> items = null;
    }

    public static class Item {

        @SerializedName("date")
        @Expose
        @Getter
        private long date;

        @SerializedName("out")
        @Expose
        @Getter
        private int out;
    }
}