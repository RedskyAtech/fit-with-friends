package com.fit_with_friends.fitWithFriends.impl.models;

import com.fit_with_friends.common.contracts.tool.ISynchronizedModel;
import com.fit_with_friends.common.contracts.tool.ModelState;

import java.io.Serializable;
import java.util.Date;

public class TodayWeightModel implements ISynchronizedModel, Serializable {

    private String id;
    private String weight_id;
    private String weight;
    private int weight_type;
    private String image;
    private String created_at;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    public String getWeight_id() {
        return weight_id;
    }

    public void setWeight_id(String weight_id) {
        this.weight_id = weight_id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getWeight_type() {
        return weight_type;
    }

    public void setWeight_type(int weight_type) {
        this.weight_type = weight_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
