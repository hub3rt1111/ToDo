package com.example.todo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todo.TodoApplication
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as TodoApplication
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) return

        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences ?: return

            CoroutineScope(Dispatchers.IO).launch {
                if (app.settingsRepository.locationNotificationsEnabled.first()) {
                    for (geofence in triggeringGeofences) {
                        // Wyciągamy ID zadania z formatu "task_ID_cośtam"
                        val taskId = geofence.requestId.substringAfter("task_").substringBefore("_")
                            .toLongOrNull() ?: continue

                        NotificationHelper.showNotification(
                            context,
                            "Zadanie w pobliżu!",
                            "Właśnie wszedłeś w obszar przypisany do Twojego zadania.",
                            taskId
                        )
                    }
                }
            }
        }
    }
}
