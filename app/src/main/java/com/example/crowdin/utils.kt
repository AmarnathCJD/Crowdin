package com.example.crowdin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    )

    LaunchedEffect(key1 = permissionState) {
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }
        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()
        if (allPermissionsRevoked) {
            onPermissionsRevoked()
        } else {
            if (permissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
}

fun resolveCoordinates(latitude: Double, longitude: Double): String {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://nominatim.openstreetmap.org/reverse.php?lat=$latitude&lon=$longitude&zoom=18&format=jsonv2")
        .build()

    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()

    val json = responseBody?.let { JSONObject(it) }
    return json?.getString("display_name") ?: "Unknown"
}

fun cellTowerLookup(cellId: Int, lac: Int, mcc: Int, mnc: Int): String {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://us1.unwiredlabs.com/v2/process")
        .post(
            okhttp3.RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                """
                    {
                        "token": "pk.8b14e9da4c39d1a8d6a7288680050f5a",
                        "radio": "lte",
                        "mcc": $mcc,
                        "mnc": $mnc,
                        "cells": [{
                            "lac": $lac,
                            "cid": $cellId,
                            "psc": 0
                        }],
                        "address": 1
                    }
                """.trimIndent()
            )
        )
        .build()

    val response = client.newCall(request).execute()
    val responseBody = response.body?.string()

    val json = responseBody?.let { JSONObject(it) }
    if (json != null && json.has("lat") && json.has("lon")) {
        return "${json.get("lat")}, ${json.get("lon")}"
    }
    return "Unknown"
}

fun extractCellTowerLocationInfo(cellInfo: CellInfo): Map<String, Any?>? {
    return when (cellInfo) {
        is CellInfoGsm -> {
            val cellIdentity = cellInfo.cellIdentity
            mapOf(
                "cellId" to cellIdentity.cid,
                "lac" to cellIdentity.lac,
                "mcc" to cellIdentity.mcc,
                "mnc" to cellIdentity.mnc
            )
        }

        is CellInfoLte -> {
            val cellIdentity = cellInfo.cellIdentity
            val cid = cellIdentity.ci
            val correctedCid = if (cid and (1 shl 28) != 0) {
                cid - (1 shl 28)
            } else {
                cid
            }
            mapOf(
                "cellId" to correctedCid,
                "tac" to cellIdentity.tac,
                "mcc" to cellIdentity.mcc,
                "mnc" to cellIdentity.mnc,
            )
        }

        is CellInfoWcdma -> {
            val cellIdentity = cellInfo.cellIdentity
            mapOf(
                "cellId" to cellIdentity.cid,
                "lac" to cellIdentity.lac,
                "mcc" to cellIdentity.mcc,
                "mnc" to cellIdentity.mnc
            )
        }

        else -> null
    }
}

@Composable
fun TowerInfoScreen() {
    val context = LocalContext.current
    var towerInfo by remember { mutableStateOf<List<CellInfo>?>(null) }
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                towerInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    telephonyManager.allCellInfo
                } else {
                    listOf()
                }
            }
        }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            towerInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                telephonyManager.allCellInfo
            } else {
                listOf()
            }
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    if (towerInfo != null) {
        if (towerInfo!!.isEmpty()) {
            return
        }
        val towerDetails = extractCellTowerLocationInfo(towerInfo!![0])
        val cellCoordinates = remember { mutableStateOf("Unknown") }
        LaunchedEffect(towerDetails) {
            if (towerDetails != null)
                Thread {
                    try {
                        var lac = towerDetails["lac"]
                        if (lac == null) {
                            lac = towerDetails["tac"]
                        }
                        cellCoordinates.value = cellTowerLookup(
                            towerDetails["cellId"] as Int,
                            lac as Int,
                            towerDetails["mcc"] as Int,
                            towerDetails["mnc"] as Int
                        )
                    } catch (e: Exception) {
                        cellCoordinates.value = "Unknown"
                    }
                }.start()
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            ElevatedButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        "TowerID: ${towerDetails?.get("cellId")} LAC: ${towerDetails?.get("tac")} MCC: ${
                            towerDetails?.get(
                                "mcc"
                            )
                        } MNC: ${towerDetails?.get("mnc")}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                    if (cellCoordinates.value != "Unknown") {
                        Text(
                            "Tower Location: ${cellCoordinates.value}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

fun timeMsToTimeString(timeMs: Long): String {
    val seconds = timeMs / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    return "${hours % 24}:${minutes % 60}:${seconds % 60}"
}

fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun getCustomBitmapDescriptor(
    context: Context?,
    drawableResId: Int,
    color: Color,
    width: Int,
    height: Int,
): BitmapDescriptor? {
    var drawable = ContextCompat.getDrawable(context!!, drawableResId) ?: return null
    drawable = DrawableCompat.wrap(drawable)
    if (color != Color.Transparent) {
        DrawableCompat.setTint(drawable, color.toArgb())
    }
    // DrawableCompat.setTint(drawable, color.toArgb())
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    drawable.setBounds(0, 0, width, height)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371
    val dLat = deg2rad(lat2 - lat1)
    val dLon = deg2rad(lon2 - lon1)
    val a =
        sin(dLat / 2) * sin(dLat / 2) +
                cos(deg2rad(lat1)) * cos(deg2rad(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val res = R * c
    return Math.round(res * 100.0) / 100.0
}

fun deg2rad(deg: Double): Double {
    return deg * (Math.PI / 180)
}

var AlertIconMap = mapOf(
    "fire" to R.drawable.wildfire,
    "elephant" to R.drawable.elephant__3_,
    "tiger" to R.drawable.tiger,
    "dog" to R.drawable.bernese_mountain,
    "tornado" to R.drawable.tornado,
    "landslide" to R.drawable.landslide,
    "flood" to R.drawable.flooded_house,
)

fun getAlertIcon(iconName: String): Int {
    return AlertIconMap[iconName] ?: R.drawable.alert
}

fun addNavEntry(route: String) {
    NavHistory.value = (NavHistory.value + route).toMutableList()
}

fun popNavEntry(): String {
    val history = NavHistory.value
    if (history.size == 1) {
        return history[0]
    } else if (history.size == 0) {
        return "Home"
    }
    // else return last 2nd element
    return history[history.size - 2]
}

fun peekNavEntry(): String {
    return NavHistory.value.last()
}