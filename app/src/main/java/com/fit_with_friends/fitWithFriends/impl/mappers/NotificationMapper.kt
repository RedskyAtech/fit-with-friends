package com.fit_with_friends.fitWithFriends.impl.mappers

import com.fit_with_friends.common.contracts.tool.IModelMapper
import com.fit_with_friends.fitWithFriends.impl.entities.Notification
import com.fit_with_friends.fitWithFriends.impl.models.NotificationModel

class NotificationMapper : IModelMapper<Notification, NotificationModel> {

    override fun Map(entity: Notification): NotificationModel {
        return NotificationModel()
    }
}

