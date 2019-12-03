package com.fit_with_friends.common.contracts.tool;


import java.io.Serializable;
import java.util.Date;

public interface IModel extends Serializable {

    String getId();

    void setId(String id);

    Long getTableId();

    void setTableId(Long tableId);

    Date getTimeStamp();

    void setTimeStamp(Date timeStamp);

    ModelState getModelStatus();

    void setStatus(Integer status);

    String getServerId();

    void setServerId(String id);

    Boolean isCached();

    void setCached();

    ModelState getStatusModel();

    void setStatusModel(ModelState status);

    Boolean isFinal();

    void setFinal();
}
