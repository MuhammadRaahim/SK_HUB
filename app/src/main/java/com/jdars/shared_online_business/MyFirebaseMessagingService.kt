package com.jdars.shared_online_business

import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.horizam.skbhub.Utils.Constants.Companion.NOTIFICATION_DATABASE_ROOT
import com.jdars.shared_online_business.models.Data
import com.jdars.shared_online_business.models.Notification


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            scheduleJob(remoteMessage)
        }

        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

    }

    private fun scheduleJob(remoteMessage: RemoteMessage) {
        val gson = Gson()
        val jsonElement = gson.toJsonTree(remoteMessage.data)
        val data: Data = gson.fromJson(jsonElement, Data::class.java)
        saveNotification(data)
    }

    private fun saveNotification(data: Data){
        val notificationReference = Firebase.firestore.collection(NOTIFICATION_DATABASE_ROOT)
        val ref = notificationReference.document()
        val notification = Notification(ref.id, FirebaseAuth.getInstance().currentUser!!.uid,data.body,
        data.title,data.type)
        ref.set(notification).addOnCompleteListener {
            if (it.isSuccessful){

            }else{
                Toast.makeText(applicationContext,it.exception!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(intent: Intent, title: String?, body: String?) {
        val channelId = "notification_channel"
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT)
        }
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, builder.build())

    }

    override fun onNewToken(token: String) {
//        sendRegistrationToServer(token)
    }



    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

}
