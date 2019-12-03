package com.fit_with_friends.fitWithFriends.impl.models;

import com.fit_with_friends.common.contracts.tool.ISynchronizedModel;
import com.fit_with_friends.common.contracts.tool.ModelState;

import java.io.Serializable;
import java.util.Date;

public class UserDetailModel implements ISynchronizedModel, Serializable {

    private String id;
    private String role;
    private String reffered_by;
    private String name;
    private String email;
    private String dob;
    private String weight;
    private String height;
    private String gender;
    private String image;
    private String notification_status;
    private String motivation_notification_status;
    private String status;
    private String staturefferal_codes;
    private String device_type;
    private String device_token;
    private String social_type;
    private String social_id;
    private String refresh_token;
    private Date created_at;
    private Date updated_at;
    private String access_token;
    private String phone;
    private String password;
    private String newPassword;
    private String confirmPassword;
    private String userId;
    private String token;
    private String security_key;
    private Date date;
    private String weight_type;
    private String height_type;
    private String old_password;
    private String new_password;
    private String comfirm_password;
    private String privacy_policy;
    private TodayWeightModel today_weight;
    private int notification_count;

    public int getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(int notification_count) {
        this.notification_count = notification_count;
    }

    public String getHeight_type() {
        return height_type;
    }

    public void setHeight_type(String height_type) {
        this.height_type = height_type;
    }

    public TodayWeightModel getToday_weight() {
        return today_weight;
    }

    public void setToday_weight(TodayWeightModel today_weight) {
        this.today_weight = today_weight;
    }

    public String getMotivation_notification_status() {
        return motivation_notification_status;
    }

    public void setMotivation_notification_status(String motivation_notification_status) {
        this.motivation_notification_status = motivation_notification_status;
    }

    public String getPrivacy_policy() {
        return privacy_policy;
    }

    public void setPrivacy_policy(String privacy_policy) {
        this.privacy_policy = privacy_policy;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getComfirm_password() {
        return comfirm_password;
    }

    public void setComfirm_password(String comfirm_password) {
        this.comfirm_password = comfirm_password;
    }

    public String getWeight_type() {
        return weight_type;
    }

    public void setWeight_type(String weight_type) {
        this.weight_type = weight_type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSecurity_key() {
        return security_key;
    }

    public void setSecurity_key(String security_key) {
        this.security_key = security_key;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getReffered_by() {
        return reffered_by;
    }

    public void setReffered_by(String reffered_by) {
        this.reffered_by = reffered_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public void setNotification_status(String notification_status) {
        this.notification_status = notification_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaturefferal_codes() {
        return staturefferal_codes;
    }

    public void setStaturefferal_codes(String staturefferal_codes) {
        this.staturefferal_codes = staturefferal_codes;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getSocial_type() {
        return social_type;
    }

    public void setSocial_type(String social_type) {
        this.social_type = social_type;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
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

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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