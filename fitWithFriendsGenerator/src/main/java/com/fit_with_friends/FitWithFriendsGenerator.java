package com.fit_with_friends;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class FitWithFriendsGenerator {
    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(2, "com.fit_with_friends.fitWithFriends.impl.entities");

        Entity user = schema.addEntity("User");
        user.addIdProperty();

        Entity challenge = schema.addEntity("Challenge");
        challenge.addIdProperty();

        Entity notification = schema.addEntity("Notification");
        notification.addIdProperty();

        Entity motivation = schema.addEntity("Motivation");
        motivation.addIdProperty();

        Entity connection = schema.addEntity("Connection");
        connection.addIdProperty();

        Entity weightLog = schema.addEntity("WeightLog");
        weightLog.addIdProperty();

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }
}
