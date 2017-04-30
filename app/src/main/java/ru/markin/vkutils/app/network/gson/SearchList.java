package ru.markin.vkutils.app.network.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

@SuppressWarnings("WeakerAccess")
public class SearchList {

    @SerializedName("response")
    @Expose
    @Getter
    private List<Item> items = null;

    public class Item {

        @SerializedName("id")
        @Expose
        @Getter
        private Integer id;

        @SerializedName("title")
        @Expose
        @Getter
        private String title;

        @SerializedName("photo")
        @Expose
        @Getter
        private String photo;
    }
}