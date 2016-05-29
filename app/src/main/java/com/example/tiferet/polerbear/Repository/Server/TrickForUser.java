package com.example.tiferet.polerbear.Repository.Server;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "userId",
        "userName",
        "trickId",
        "trickName",
        "date",
        "comment",
        "trickPic",
        "isFinised",
        "doneBefore"
})
public class TrickForUser {

    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("trickId")
    private Integer trickId;
    @JsonProperty("trickName")
    private String trickName;
    @JsonProperty("date")
    private String date;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("trickPic")
    private String trickPic;
    @JsonProperty("isFinised")
    private Integer isFinised;
    @JsonProperty("doneBefore")
    private Integer doneBefore;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The userId
     */
    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The userId
     */
    @JsonProperty("userId")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The userName
     */
    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     * The userName
     */
    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     * The trickId
     */
    @JsonProperty("trickId")
    public Integer getTrickId() {
        return trickId;
    }

    /**
     *
     * @param trickId
     * The trickId
     */
    @JsonProperty("trickId")
    public void setTrickId(Integer trickId) {
        this.trickId = trickId;
    }

    /**
     *
     * @return
     * The trickName
     */
    @JsonProperty("trickName")
    public String getTrickName() {
        return trickName;
    }

    /**
     *
     * @param trickName
     * The trickName
     */
    @JsonProperty("trickName")
    public void setTrickName(String trickName) {
        this.trickName = trickName;
    }

    /**
     *
     * @return
     * The date
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The comment
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     * The comment
     */
    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     * @return
     * The trickPic
     */
    @JsonProperty("trickPic")
    public String getTrickPic() {
        return trickPic;
    }

    /**
     *
     * @param trickPic
     * The trickPic
     */
    @JsonProperty("trickPic")
    public void setTrickPic(String trickPic) {
        this.trickPic = trickPic;
    }

    /**
     *
     * @return
     * The isFinised
     */
    @JsonProperty("isFinised")
    public Integer getIsFinised() {
        return isFinised;
    }

    /**
     *
     * @param isFinised
     * The isFinised
     */
    @JsonProperty("isFinised")
    public void setIsFinised(Integer isFinised) {
        this.isFinised = isFinised;
    }

    /**
     *
     * @return
     * The doneBefore
     */
    @JsonProperty("doneBefore")
    public Integer getDoneBefore() {
        return doneBefore;
    }

    /**
     *
     * @param doneBefore
     * The doneBefore
     */
    @JsonProperty("doneBefore")
    public void setDoneBefore(Integer doneBefore) {
        this.doneBefore = doneBefore;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}