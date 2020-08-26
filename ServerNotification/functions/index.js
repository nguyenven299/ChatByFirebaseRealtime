'use strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { database } = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/Notification/{receiver_user_id}/{notification_id}')
    .onWrite((data, context) => {
        const receiver_user_id = context.params.receiver_user_id;
        const notification_id = context.params.notification_id;

        // console.log('Notification to user: ', receiver_user_id);

        if (!data.after.val()) {
            // console.log('notification has been deleted: ', notification_id);
            return null;
        }

        const sender_user_id = admin.database().ref(`/Notification/${receiver_user_id}/${notification_id}`).once('value');
        return sender_user_id.then(fromUserResult => {
            const from_sender_user_id = fromUserResult.val().from;
            // console.log('you have new notification:', sender_user_id);
            const userQuery = admin.database().ref(`/User/${receiver_user_id}/username`).once('value');
            return userQuery.then(userQuery => {
                const senderUserName = userQuery.val();
                const DeviceToken = admin.database().ref(`/User/${receiver_user_id}/device_token`).once('value');

                return DeviceToken.then(result => {
                    const token_id = result.val();


                    const payload =
                    {
                        notification:
                        {
                            from_sender_user_id: from_sender_user_id,
                            title: "Chat Encode",
                            body: `You have a new chat. Please check! `,
                            sound: "mySound"
                        }
                    };

                    return admin.messaging().sendToDevice(token_id, payload)
                        .then(function (response) {
                            console.log("Successfully sent message:", response, token_id);
                        })
                        .catch(function (error) {
                            console.log("Error sending message:", error);
                        });
                });
            });

        });
    });

function decrypt3DES(input, key) {
    // let md5Key = forge.md.md5.create();
    // md5Key.update(key);
    // md5Key = md5Key.digest().toHex();
    // const decipher = forge.cipher.createDecipher('3DES-ECB', md5Key.substring(0, 24));
    // decipher.start();

    // const inputEx = forge.util.createBuffer(Buffer.from(input, "base64").toString("binary"));
    // decipher.update(inputEx);
    // decipher.finish();
    // const decrypted = decipher.output;
    // return Buffer.from(decrypted.getBytes(), "binary").toString("utf8")
    const md5Key = crypto.createHash('md5').update(key).digest("hex").substr(0, 24);
    const decipher = crypto.createDecipheriv('des-ede3', md5Key, '');

    let encrypted = decipher.update(input, 'base64', 'utf8');
    encrypted += decipher.final('utf8');
    return encrypted;
}



