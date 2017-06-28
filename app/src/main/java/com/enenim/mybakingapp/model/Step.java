package com.enenim.mybakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Step implements Parcelable{

    @Expose
    @SerializedName("id")
    private long id;

    @Expose
    @SerializedName("shortDescription")
    private String shortDescription;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("videoURL")
    private String videoURL;

    @Expose
    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    public Step(){

    }

    //For widget data
    public Step (JSONObject stepJson) {
        try {
            setId(stepJson.getInt("id"));
            setShortDescription(stepJson.optString("shortDescription"));
            setDescription(stepJson.optString("description"));
            setVideoURL(stepJson.optString("videoURL"));
            setThumbnailURL(stepJson.getString("thumbnailURL"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Parcel Constructor
    public Step(Parcel in) {
        setId(in.readLong());
        setShortDescription(in.readString());
        setDescription(in.readString());
        setVideoURL(in.readString());
        setThumbnailURL(in.readString());
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getShortDescription());
        dest.writeString(getDescription());
        dest.writeString(getVideoURL());
        dest.writeString(getThumbnailURL());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Step{");
        sb.append("id=").append(id);
        sb.append(", shortDescription='").append(shortDescription).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", videoURL='").append(videoURL).append('\'');
        sb.append(", thumbnailURL='").append(thumbnailURL).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
