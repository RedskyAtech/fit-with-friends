package com.fit_with_friends.fitWithFriends.impl.models;

import com.fit_with_friends.common.contracts.tool.ISynchronizedModel;
import com.fit_with_friends.common.contracts.tool.ModelState;

import java.io.Serializable;
import java.util.Date;

public class NotificationModel implements ISynchronizedModel, Serializable {

    private String id;
    private String user_id;
    private int notification_id;
    private String comment;
    private String created_at;
    private Date updated_at;
    private UserModel user;
    private NotificationModel notification;
    private String friend_id;
    private String challenge_id;
    private String message;
    private String notification_type;
    private int total_likes;
    private int total_comments;
    private ChallengeModel challenge;
    private int request_id;
    private int participant_id;
    private UserDetailModel user_detail;
    private String like_status;
    private String notification_counts;
    private String comment_id;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public String getNotification_counts() {
        return notification_counts;
    }

    public void setNotification_counts(String notification_counts) {
        this.notification_counts = notification_counts;
    }

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public NotificationModel getNotification() {
        return notification;
    }

    public void setNotification(NotificationModel notification) {
        this.notification = notification;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(String challenge_id) {
        this.challenge_id = challenge_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public int getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(int total_likes) {
        this.total_likes = total_likes;
    }

    public int getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(int total_comments) {
        this.total_comments = total_comments;
    }

    public ChallengeModel getChallenge() {
        return challenge;
    }

    public void setChallenge(ChallengeModel challenge) {
        this.challenge = challenge;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(int participant_id) {
        this.participant_id = participant_id;
    }

    public UserDetailModel getUser_detail() {
        return user_detail;
    }

    public void setUser_detail(UserDetailModel user_detail) {
        this.user_detail = user_detail;
    }
}