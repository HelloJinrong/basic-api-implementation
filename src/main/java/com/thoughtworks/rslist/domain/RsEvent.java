package com.thoughtworks.rslist.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RsEvent {
    @NotNull
    public String eventName;
    @NotNull
    public String keyWord;
    @NotNull
    @Valid
    public User user;

    public String getEventName() {
        return eventName;
    }

    public RsEvent() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public RsEvent(String eventName, String keyWord,User user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;

    }

}
