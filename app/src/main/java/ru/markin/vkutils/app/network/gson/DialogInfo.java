package ru.markin.vkutils.app.network.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

public class DialogInfo {

    @SerializedName("response")
    @Expose
    @Getter
    private List<List<Integer>> outputs = null;
}