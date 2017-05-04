package ru.markin.vkutils.common.network.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

public class AccessToken {

    @SerializedName("access_token")
    @Expose
    @Getter
    private String accessToken;

    @SerializedName("expires_in")
    @Expose
    @Getter
    private long expiresIn;
}
