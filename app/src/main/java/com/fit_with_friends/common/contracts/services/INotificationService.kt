package com.fit_with_friends.common.contracts.services

import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.ICRUDService
import com.fit_with_friends.common.contracts.tool.Page
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.fitWithFriends.impl.models.NotificationModel

interface INotificationService : ICRUDService<NotificationModel> {

    fun getNotificationListFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<Page<NotificationModel>>
    )

    fun notificationLike(
        url: String,
        action: String?,
        notificationModel: NotificationModel,
        result: AsyncResult<NotificationModel>
    )

    fun notificationComment(
        url: String,
        action: String?,
        notificationModel: NotificationModel,
        result: AsyncResult<NotificationModel>
    )

    fun getNotificationCommentListFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<Page<NotificationModel>>
    )

    fun clearNotificationCount(
        url: String,
        result: AsyncResult<NotificationModel>
    )

    fun clearSingleNotificationsCount(
        url: String,
        action: String?,
        notificationModel: NotificationModel,
        result: AsyncResult<NotificationModel>
    )
}