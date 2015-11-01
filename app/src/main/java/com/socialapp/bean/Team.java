package com.socialapp.bean;

import java.io.Serializable;

/**
 * Created by Sanjoy on 01-11-2015.
 */
public class Team implements Serializable
{
    private String name,id,ownerName,iconUrl,iconLocalPath;
    private int localId;

    public String getIconLocalPath() {
        return iconLocalPath;
    }

    public void setIconLocalPath(String iconLocalPath) {
        this.iconLocalPath = iconLocalPath;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
