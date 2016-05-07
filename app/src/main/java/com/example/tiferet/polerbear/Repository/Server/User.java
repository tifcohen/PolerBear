
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
        "userPwd",
        "userEmail",
        "userBirthDate",
        "userLevel",
        "userSex"
})
public class User {

    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("userPwd")
    private String userPwd;
    @JsonProperty("userEmail")
    private String userEmail;
    @JsonProperty("userBirthDate")
    private String userBirthDate;
    @JsonProperty("userLevel")
    private Integer userLevel;
    @JsonProperty("userSex")
    private String userSex;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The userId
     */
    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     *     The userId
     */
    @JsonProperty("userId")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     *     The userName
     */
    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     *     The userName
     */
    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     *     The userPwd
     */
    @JsonProperty("userPwd")
    public String getUserPwd() {
        return userPwd;
    }

    /**
     *
     * @param userPwd
     *     The userPwd
     */
    @JsonProperty("userPwd")
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    /**
     *
     * @return
     *     The userEmail
     */
    @JsonProperty("userEmail")
    public String getUserEmail() {
        return userEmail;
    }

    /**
     *
     * @param userEmail
     *     The userEmail
     */
    @JsonProperty("userEmail")
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     *
     * @return
     *     The userBirthDate
     */
    @JsonProperty("userBirthDate")
    public String getUserBirthDate() {
        return userBirthDate;
    }

    /**
     *
     * @param userBirthDate
     *     The userBirthDate
     */
    @JsonProperty("userBirthDate")
    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    /**
     *
     * @return
     *     The userLevel
     */
    @JsonProperty("userLevel")
    public Integer getUserLevel() {
        return userLevel;
    }

    /**
     *
     * @param userLevel
     *     The userLevel
     */
    @JsonProperty("userLevel")
    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    /**
     *
     * @return
     *     The userSex
     */
    @JsonProperty("userSex")
    public String getUserSex() {
        return userSex;
    }

    /**
     *
     * @param userSex
     *     The userSex
     */
    @JsonProperty("userSex")
    public void setUserSex(String userSex) {
        this.userSex = userSex;
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
