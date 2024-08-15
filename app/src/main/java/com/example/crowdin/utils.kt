package com.example.crowdin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


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
            Text("No cell tower information available")
            return
        }
        val towerDetails = extractCellTowerLocationInfo(towerInfo!![0])
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            ElevatedButton(onClick = { /*TODO*/ },
                shape = RoundedCornerShape(5.dp)
                ) {
                Text(
                    "TowerId: ${towerDetails?.get("cellId")} LAC: ${towerDetails?.get("tac")} MCC: ${
                        towerDetails?.get(
                            "mcc"
                        )
                    } MNC: ${towerDetails?.get("mnc")}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}

