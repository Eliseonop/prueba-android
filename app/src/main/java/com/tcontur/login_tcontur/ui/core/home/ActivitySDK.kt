//package com.tcontur.login_tcontur.ui.core.home
//import androidx.compose.material3.Text
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.ftpos.library.smartpos.buzzer.Buzzer
//import com.ftpos.library.smartpos.crypto.Crypto
//import com.ftpos.library.smartpos.device.Device
//import com.ftpos.library.smartpos.emv.Emv
//import com.ftpos.library.smartpos.icreader.IcReader
//import com.ftpos.library.smartpos.keymanager.KeyManager
//import com.ftpos.library.smartpos.led.Led
//import com.ftpos.library.smartpos.magreader.MagReader
//import com.ftpos.library.smartpos.memoryreader.MemoryReader
//import com.ftpos.library.smartpos.nfcreader.NfcReader
//import com.ftpos.library.smartpos.printer.Printer
//import com.ftpos.library.smartpos.psamreader.PsamReader
//import com.ftpos.library.smartpos.serialport.SerialPort
//import com.ftpos.library.smartpos.servicemanager.OnServiceConnectCallback
//import com.ftpos.library.smartpos.servicemanager.ServiceManager
//
//class MainActivity : ComponentActivity() {
//
//    // Referencias a las clases que provee la librería
//    private var keyManager: KeyManager? = null
//    private var led: Led? = null
//    private var buzzer: Buzzer? = null
//    private var psamReader: PsamReader? = null
//    private var nfcReader: NfcReader? = null
//    private var icReader: IcReader? = null
//    private var magReader: MagReader? = null
//    private var printer: Printer? = null
//    private var device: Device? = null
//    private var crypto: Crypto? = null
//    private var memoryReader: MemoryReader? = null
//    private var emv: Emv? = null
//    private var serialport: SerialPort? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // 1. Configuramos la UI con Jetpack Compose
//        setContent {
//           Text(text = "Hola, mundo!")
//        }
//
//        // 2. Pedir permisos (ejemplo simplificado)
//        checkStoragePermissions()
//
//        // 3. Vincular el servicio (igual que en tu Java, pero en Kotlin)
//        ServiceManager.bindPosServer(this, object : OnServiceConnectCallback {
//            override fun onSuccess() {
//                Log.d("ServiceManager", "bindPosServer: onSuccess")
//
//                // Inicializa cada clase
//                led = Led.getInstance(this@MainActivity)
//                buzzer = Buzzer.getInstance(this@MainActivity)
//                psamReader = PsamReader.getInstance(this@MainActivity)
//                nfcReader = NfcReader.getInstance(this@MainActivity)
//                icReader = IcReader.getInstance(this@MainActivity)
//                magReader = MagReader.getInstance(this@MainActivity)
//                printer = Printer.getInstance(this@MainActivity)
//                device = Device.getInstance(this@MainActivity)
//                crypto = Crypto.getInstance(this@MainActivity)
//                memoryReader = MemoryReader.getInstance(this@MainActivity)
//                keyManager = KeyManager.getInstance(this@MainActivity)
//                emv = Emv.getInstance(this@MainActivity)
//                serialport = SerialPort.getInstance(this@MainActivity)
//
//                // Ejemplo de lógica adicional:
//                // keyManager?.setKeyGroupName(packageName)
//            }
//
//            override fun onFail(var1: Int) {
//                Log.e("ServiceManager", "bindPosServer: onFail = $var1")
//            }
//        })
//    }
//
//    private fun checkStoragePermissions() {
//        val permission = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ),
//                100
//            )
//        }
//    }
//
//    // Manejo del botón BACK si quieres simular el “doble click para salir”:
//    private var exitTime = 0L
//
//    override fun onBackPressed() {
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            Toast.makeText(this, "Presiona otra vez para salir", Toast.LENGTH_SHORT).show()
//            exitTime = System.currentTimeMillis()
//        } else {
//            super.onBackPressed()
//        }
//    }
//}
