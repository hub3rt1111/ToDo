package com.example.todo.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todo.data.local.LocationEntity
import com.example.todo.data.local.TaskEntity
import com.example.todo.data.local.TaskStatus
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class TaskReminderManager(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val geofencingClient = LocationServices.getGeofencingClient(context)

    fun scheduleTaskReminders(task: TaskEntity, locations: List<LocationEntity> = emptyList()) {
        if (task.status == TaskStatus.DONE) {
            cancelReminders(task.id)
            return
        }
        
        scheduleTimeReminder(task)
        setupGeofences(task, locations)
    }

    private fun scheduleTimeReminder(task: TaskEntity) {
        val dueDate = task.dueDate ?: return
        val reminderTime = dueDate - (15 * 60 * 1000)

        if (reminderTime < System.currentTimeMillis()) return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("taskId", task.id)
            putExtra("title", "Upcoming Task: ${task.title}")
            putExtra("description", "This task is due in 15 minutes.")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminderTime,
                pendingIntent
            )
        } catch (e: SecurityException) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                reminderTime,
                pendingIntent
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupGeofences(task: TaskEntity, locations: List<LocationEntity>) {
        val geofenceList = mutableListOf<Geofence>()

        if (task.latitude != null && task.longitude != null) {
            geofenceList.add(
                Geofence.Builder()
                    .setRequestId("task_${task.id}_primary")
                    .setCircularRegion(task.latitude, task.longitude, 100f)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build()
            )
        }

        locations.forEachIndexed { index, location ->
            geofenceList.add(
                Geofence.Builder()
                    .setRequestId("task_${task.id}_loc_$index")
                    .setCircularRegion(location.latitude, location.longitude, 100f)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build()
            )
        }

        if (geofenceList.isEmpty()) return

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofenceList)
            .build()

        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt() + 1000000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        if (LocationUtils.hasLocationPermission(context)) {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
        }
    }

    fun cancelReminders(taskId: Long) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            alarmIntent,
            //czy taki PendingIntent już istnieje w pamięci Androida
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        alarmPendingIntent?.let { 
            alarmManager.cancel(it) 
            it.cancel()
        }

        val geoIntent = Intent(context, GeofenceBroadcastReceiver::class.java)
        val geoPendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt() + 1000000,
            geoIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE
        )
        geoPendingIntent?.let { 
            geofencingClient.removeGeofences(it)
            it.cancel()
        }
    }
}
