package com.tcontur.login_tcontur.ui.core.protobin

import android.content.Context
import android.util.Log
import com.ftpos.library.smartpos.buzzer.Buzzer
import com.ftpos.library.smartpos.crypto.Crypto
import com.ftpos.library.smartpos.device.Device
import com.ftpos.library.smartpos.emv.Emv
import com.ftpos.library.smartpos.icreader.IcReader
import com.ftpos.library.smartpos.keymanager.KeyManager
import com.ftpos.library.smartpos.led.Led
import com.ftpos.library.smartpos.magreader.MagReader
import com.ftpos.library.smartpos.memoryreader.MemoryReader
import com.ftpos.library.smartpos.nfcreader.NfcReader
import com.ftpos.library.smartpos.printer.Printer
import com.ftpos.library.smartpos.psamreader.PsamReader
import com.ftpos.library.smartpos.serialport.SerialPort
import com.ftpos.library.smartpos.servicemanager.OnServiceConnectCallback
import com.ftpos.library.smartpos.servicemanager.ServiceManager
// File: DeviceRegistry.kt

object DeviceRegistry {

    lateinit var keyManager: KeyManager
    lateinit var led: Led
    lateinit var buzzer: Buzzer
    lateinit var psamReader: PsamReader
    lateinit var nfcReader: NfcReader
    lateinit var icReader: IcReader
    lateinit var magReader: MagReader
    lateinit var printer: Printer
    lateinit var device: Device
    lateinit var crypto: Crypto
    lateinit var memoryReader: MemoryReader
    lateinit var emv: Emv
    lateinit var serialport: SerialPort

    fun init(context: Context) {
        ServiceManager.bindPosServer(context, object : OnServiceConnectCallback {
            override fun onSuccess() {
                led = Led.getInstance(context)
                buzzer = Buzzer.getInstance(context)
                psamReader = PsamReader.getInstance(context)
                nfcReader = NfcReader.getInstance(context)
                icReader = IcReader.getInstance(context)
                magReader = MagReader.getInstance(context)
                printer = Printer.getInstance(context)
                device = Device.getInstance(context)
                crypto = Crypto.getInstance(context)
                memoryReader = MemoryReader.getInstance(context)
                keyManager = KeyManager.getInstance(context)
                emv = Emv.getInstance(context)
                serialport = SerialPort.getInstance(context)
            }

            override fun onFail(var1: Int) {
                Log.e("DeviceRegistry", "Falló la inicialización: $var1")
            }
        })
    }
    fun isReady(): Boolean {
        return this::device.isInitialized && this::printer.isInitialized
    }
}

