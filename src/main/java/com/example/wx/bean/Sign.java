package com.example.wx.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="SIGN")
public class Sign {
    @Id
    private String id;

    private String userId;

    private String userDataId;

    private Date signDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public String getUserDataId() {
        return userDataId;
    }

    public void setUserDataId(String userDataId) {
        this.userDataId = userDataId;
    }
}
