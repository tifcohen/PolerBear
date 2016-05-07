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
        "trickId",
        "trickName",
        "trickLevel"
})
public class Trick {

    @JsonProperty("trickId")
    private Integer trickId;
    @JsonProperty("trickName")
    private String trickName;
    @JsonProperty("trickLevel")
    private Integer trickLevel;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The trickLevel
     */
    @JsonProperty("trickLevel")
    public Integer getTrickLevel() {
        return trickLevel;
    }

    /**
     *
     * @param trickLevel
     * The trickLevel
     */
    @JsonProperty("trickLevel")
    public void setTrickLevel(Integer trickLevel) {
        this.trickLevel = trickLevel;
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