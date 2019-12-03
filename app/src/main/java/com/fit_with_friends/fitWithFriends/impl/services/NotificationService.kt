package com.fit_with_friends.fitWithFriends.impl.services

import com.fit_with_friends.common.contracts.services.INotificationService
import com.fit_with_friends.common.contracts.tool.*
import com.fit_with_friends.common.ui.BaseService
import com.fit_with_friends.fitWithFriends.impl.models.NotificationModel

class NotificationService(
    private val mLocal: IRepository<NotificationModel>,
    private val mRemoteApi: IAsyncRemoteApi<NotificationModel>
) : BaseService<NotificationModel>(mLocal), INotificationService {

    override fun getNotificationListFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<Page<NotificationModel>>
    ) {
        mRemoteApi.page(url, input, result)
    }

    override fun notificationLike(
        url: String,
        action: String?,
        notificationModel: NotificationModel,
        result: AsyncResult<NotificationModel>
    ) {
        mRemoteApi.initialStep(url, action, notificationModel, result)
    }

    override fun notificationComment(
        url: String,
        action: String?,
        notificationModel: NotificationModel,
        result: AsyncResult<NotificationModel>
    ) {
        mRemoteApi.initialStep(url, action, notificationModel, result)
    }

    override fun getNotificationCommentListFromServer(
        url: String,
        token: String?,
        input: PageInput,
        result: AsyncResult<Page<NotificationModel>>
    ) {
        mRemoteApi.page(url, input, result)
    }

    override fun clearNotificationCount(url: String, result: AsyncResult<NotificationModel>) {
        mRemoteApi.get(url, null, PageInput(), result)
    }

    override fun clearSingleNotificationsCount(
        url: String,
        action: String?,
        notificationModel: NotificationModel,
        result: AsyncResult<NotificationModel>
    ) {
        mRemoteApi.initialStep(url, action, notificationModel, result)
    }
}