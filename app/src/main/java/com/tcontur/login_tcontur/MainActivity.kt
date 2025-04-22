package com.tcontur.login_tcontur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tcontur.login_tcontur.ui.core.NavigationWrapper
//import com.tcontur.login_tcontur.ui.data.bd.AppDatabase
//import com.tcontur.login_tcontur.ui.data.bd.repository.LoginRepository
import com.tcontur.login_tcontur.ui.data.manager.DeviceManager
import com.tcontur.login_tcontur.ui.theme.TconturTheme
import java.util.TimeZone

class MainActivity : ComponentActivity() {
//    val database by lazy { AppDatabase.getDatabase(this) }
//    val loginRepository by lazy { LoginRepository(database.loginDao()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"))
        DeviceManager.init(this)

//        startKoin {
//            androidLogger()
//            androidContext(this@MainActivity)
//            modules(
//                appModule, dataModule, appViewModule
//
//            )
//        }

//        device = DeviceRegistry.device
//        mContext = this
//        getDeviceModel()
//        Log.d("getDeviceModel", "mDeviceModel:$mDeviceModel")
//        ServiceManager.bindPosServer(this, object : OnServiceConnectCallback {
//            override fun onSuccess() {
//                device = Device.getInstance(mContext)
//                crypto = Crypto.getInstance(mContext)
//                val sdkVersion = device?.sdkVersionName ?: "SDK no disponible"
//                Log.d("binding", "onSuccess")
//                Log.d("binding", "SDK Version: ${crypto.toString()}")
//            }
//
//            override fun onFail(var1: Int) {
//                Log.e("binding", "onFail")
//            }
//        })

//        val permission = ActivityCompat.checkSelfPermission(
//            this, Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION
//        )
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION),
//                100
//            )
//        } else {
//        bindPosService()
//        }

//        println("SDK Version: $sdkVersion")
        setContent {
//            var skuVersion by remember { mutableStateOf("SDK ") }
////            Log.d("SDK Version", "onSuccess ${DeviceRegistry.device.toString()}")
//            if (DeviceRegistry.isReady()) {
//                skuVersion = DeviceRegistry.device.sdkVersionName ?: "SDK no "
//            }
//            skuVersion = DeviceRegistry.device.sdkVersionName ?: "SDK no "
//            Log.d("SDK Version", "onSuccess $skuVersion")
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(text = "SDK Version: $skuVersion")
//            }
            TconturTheme {
                NavigationWrapper(
                    context = this
                )
            }
        }
    }

//    val DEVICE_MODE_UNKOWN: Int = -1
//    val DEVICE_MODE_F100: Int = 1
//    val DEVICE_MODE_F200: Int = 0
//    val DEVICE_MODE_F600_300: Int = 2
//    var mDeviceModel: Int = DEVICE_MODE_UNKOWN
//    fun getDeviceModel(): Int {
//        if (mDeviceModel == DEVICE_MODE_UNKOWN) {
////            String deviceModel = android.os.Build.MODEL;
////            String deviceModel = getSystemProperty("ro.product.model", "null");
//            val deviceModel: String? = getSystemModel()
//            if (deviceModel == "F100" || Build.MODEL == "full_k61v1_32_bsp_1g") {
//                mDeviceModel = DEVICE_MODE_F100
//            } else if (deviceModel == "F300" || deviceModel == "F600") {
//                mDeviceModel = DEVICE_MODE_F600_300
//            } else {
//                mDeviceModel = DEVICE_MODE_F200
//            }
//        }
//        return mDeviceModel
//    }
//
//    fun getSystemModel(): String? {
//        try {
//            val clazz = Class.forName("android.os.SystemProperties")
//            val getter = clazz.getDeclaredMethod("get", String::class.java)
//            var value = getter.invoke(null, "ro.ft.product.model") as String
//            if (!TextUtils.isEmpty(value)) {
//                return value
//            }
//            value = getter.invoke(null, "ro.product.model") as String
//            if (!TextUtils.isEmpty(value)) {
//                return value
//            }
//        } catch (e: Exception) {
//            Log.e("SDK", "getSystemProperty: Unable to read system properties")
//        }
//
//        return Build.MODEL
//    }
}

