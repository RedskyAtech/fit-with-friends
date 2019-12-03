package com.fit_with_friends.fitWithFriends.impl.models;

import com.fit_with_friends.common.contracts.tool.ISynchronizedModel;
import com.fit_with_friends.common.contracts.tool.ModelState;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MotivationModel implements ISynchronizedModel, Serializable {

    private List<MotivationTypeModel> today_motivation;
    private List<MotivationTypeModel> yesterday_motivation;

    public List<MotivationTypeModel> getToday_motivation() {
        return today_motivation;
    }

    public void setToday_motivation(List<MotivationTypeModel> today_motivation) {
        this.today_motivation = today_motivation;
    }

    public List<MotivationTypeModel> getYesterday_motivation() {
        return yesterday_motivation;
    }

    public void setYesterday_motivation(List<MotivationTypeModel> yesterday_motivation) {
        this.yesterday_motivation = yesterday_motivation;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public Long getTableId() {
        return null;
    }

    @Override
    public void setTableId(Long tableId) {

    }

    @Override
    public Date getTimeStamp() {
        return null;
    }

    @Override
    public void setTimeStamp(Date timeStamp) {

    }

    @Override
    public ModelState getModelStatus() {
        return null;
    }

    @Override
    public void setStatus(Integer status) {

    }

    @Override
    public String getServerId() {
        return null;
    }

    @Override
    public void setServerId(String id) {

    }

    @Override
    public Boolean isCached() {
        return null;
    }

    @Override
    public void setCached() {

    }

    @Override
    public ModelState getStatusModel() {
        return null;
    }

    @Override
    public void setStatusModel(ModelState status) {

    }

    @Override
    public Boolean isFinal() {
        return null;
    }

    @Override
    public void setFinal() {

    }
}
