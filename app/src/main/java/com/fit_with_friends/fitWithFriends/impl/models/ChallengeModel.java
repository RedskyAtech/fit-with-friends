package com.fit_with_friends.fitWithFriends.impl.models;

import com.fit_with_friends.common.contracts.tool.ISynchronizedModel;
import com.fit_with_friends.common.contracts.tool.ModelState;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ChallengeModel implements ISynchronizedModel, Serializable {

    private String id;
    private String user_id;
    private String challange_name;
    private int base_weight;
    private String challange_image;
    private String description;
    private String start_date;
    private String end_date;
    private Date created_at;
    private Date updated_at;
    private String your_ranking;
    private int rank;
    private String body_weight;
    private int participant_count;
    private String name;
    private String dob;
    private String weight;
    private File image;
    private String security_key;
    private String auth_key;
    private String add_friend;
    private String challenge_id;
    private String weight_type;
    private UserModel user;
    private List<ParticipantModel> participant;
    private int request_status;
    private int participant_id;
    private String collective_body_weight;

    public String getCollective_body_weight() {
        return collective_body_weight;
    }

    public void setCollective_body_weight(String collective_body_weight) {
        this.collective_body_weight = collective_body_weight;
    }

    public int getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(int participant_id) {
        this.participant_id = participant_id;
    }

    public int getRequest_status() {
        return request_status;
    }

    public void setRequest_status(int request_status) {
        this.request_status = request_status;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getYour_ranking() {
        return your_ranking;
    }

    public void setYour_ranking(String your_ranking) {
        this.your_ranking = your_ranking;
    }

    public String getBody_weight() {
        return body_weight;
    }

    public void setBody_weight(String body_weight) {
        this.body_weight = body_weight;
    }

    public int getParticipant_count() {
        return participant_count;
    }

    public void setParticipant_count(int participant_count) {
        this.participant_count = participant_count;
    }

    public List<ParticipantModel> getParticipant() {
        return participant;
    }

    public void setParticipant(List<ParticipantModel> participant) {
        this.participant = participant;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getWeight_type() {
        return weight_type;
    }

    public void setWeight_type(String weight_type) {
        this.weight_type = weight_type;
    }


    public String getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(String challenge_id) {
        this.challenge_id = challenge_id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getChallange_name() {
        return challange_name;
    }

    public void setChallange_name(String challange_name) {
        this.challange_name = challange_name;
    }

    public int getBase_weight() {
        return base_weight;
    }

    public void setBase_weight(int base_weight) {
        this.base_weight = base_weight;
    }

    public String getChallange_image() {
        return challange_image;
    }

    public void setChallange_image(String challange_image) {
        this.challange_image = challange_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getSecurity_key() {
        return security_key;
    }

    public void setSecurity_key(String security_key) {
        this.security_key = security_key;
    }

    public String getAuth_key() {
        return auth_key;
    }

    public void setAuth_key(String auth_key) {
        this.auth_key = auth_key;
    }

    public String getAdd_friend() {
        return add_friend;
    }

    public void setAdd_friend(String add_friend) {
        this.add_friend = add_friend;
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
