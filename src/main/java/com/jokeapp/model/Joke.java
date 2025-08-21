package com.jokeapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Joke {
    private String id;
    private String joke;
    private String status;
    private LocalDateTime timestamp;
    
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @JsonProperty("joke")
    public String getJoke() {
        return joke;
    }
    
    public void setJoke(String joke) {
        this.joke = joke;
    }
    
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "Joke{" +
                "id='" + id + '\'' +
                ", joke='" + joke + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
