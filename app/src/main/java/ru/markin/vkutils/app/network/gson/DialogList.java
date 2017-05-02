package ru.markin.vkutils.app.network.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

public class DialogList {

    @SerializedName("response")
    @Expose
    @Getter
    private Data data = null;

    public static class Data {

        @SerializedName("count")
        @Expose
        @Getter
        private Integer count;

        @SerializedName("items")
        @Expose
        @Getter
        private List<Item> items = null;
    }

    public static class Item {

        @SerializedName("type")
        @Expose
        @Getter
        private String type;

        @SerializedName("id")
        @Expose
        @Getter
        private Integer id;

        @SerializedName("title")
        @Expose
        @Getter
        private String title;

        @SerializedName("date")
        @Expose
        @Getter
        private Long date;

        @SerializedName("photo")
        @Expose
        @Getter
        private String photo;
    }
}
