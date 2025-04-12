package com.tcontur.login_tcontur.ui.data.manager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import com.ftpos.library.smartpos.errcode.ErrCode
import com.ftpos.library.smartpos.printer.AlignStyle
import com.ftpos.library.smartpos.printer.OnPrinterCallback
import com.ftpos.library.smartpos.printer.PrintStatus
import com.ftpos.library.smartpos.printer.Printer
import com.tcontur.login_tcontur.ui.data.models.TicketData


class PrinterManager(private val printer: Printer) {

    private val TAG = "PrinterManager"

    fun imprimir(
        text: String,
        onComplete: (() -> Unit)? = null,
        onError: ((Int, String?) -> Unit)? = null
    ) {
        var ret = printer.open()
        if (ret != ErrCode.ERR_SUCCESS) {
            Log.e(TAG, "open failed errCode = 0x${ret.toString(16)}")
            return
        }

        ret = printer.startCaching()
        if (ret != ErrCode.ERR_SUCCESS) {
            Log.e(TAG, "startCaching failed errCode = 0x${ret.toString(16)}")
            return
        }

        printer.setAlignStyle(1)
        ret = printer.setGray(3)
        if (ret != ErrCode.ERR_SUCCESS) {
            Log.e(TAG, "setGray failed errCode = 0x${ret.toString(16)}")
            return
        }

        printer.printStr(text)

        val printStatus = PrintStatus()
        ret = printer.getStatus(printStatus)
        if (ret != ErrCode.ERR_SUCCESS) {
            Log.e(TAG, "getStatus failed errCode = 0x${ret.toString(16)}")
            return
        }

        if (!printStatus.getmIsHavePaper()) {
            Log.e(TAG, "Printer out of paper")
            return
        }

        printer.setAlignStyle(AlignStyle.PRINT_STYLE_CENTER)
        printer.printStr("MICHAEL KORS\n")

        printer.setAlignStyle(AlignStyle.PRINT_STYLE_LEFT)
        printer.printStr("Please retain this receipt\n")
        printer.printStr("for your exchange.\n")
        printer.printStr("this gift was thoughtfully purchased\n")
        printer.printStr("for you at Michael Kors Chinook Centre.\n")

        val used = printer.usedPaperLenManage
        if (used < 0) {
            Log.e(TAG, "usedPaperLenManage failed errCode = 0x${used.toString(16)}")
            return
        }

        printer.printWithFeed(0, object : OnPrinterCallback {
            override fun onSuccess() {
                printer.setLCDFlicker(2)
                printer.close()
                Log.e(TAG, "Success printer")
                onComplete?.invoke()
            }

            override fun onError(p0: Int) {
                Log.e(TAG, "Error al imprimir: $p0")
                onError?.invoke(p0, null)
            }
        })
    }

    fun getPrinterStatus() {
        val status = PrintStatus()
        val result = printer.getStatus(status)
        printer.printStr("Estado de la impresora: $result")
    }

    fun imprimirTicket(ticketData: TicketData, qrBitmap: Bitmap) {
        printer.open()
        printer.startCaching()

        printer.setAlignStyle(0x01)
        printer.printStr(ticketData.empresa)
        printer.setAlignStyle(0x04)
        printer.printStr(ticketData.ticket)

        printer.setAlignStyle(0x01)
        printer.printStr("RUC: ${ticketData.ruc}")

        printer.setAlignStyle(0x01)
        printer.printStr(ticketData.concepto)
        printer.setAlignStyle(0x04)
        printer.printStr("S/ ${ticketData.monto}")
        printer.setFont(Bundle()) // restaurar fuente

        printer.setAlignStyle(0x01)
        printer.printStr("UNIDAD: ${ticketData.unidad}    ${ticketData.metodoPago}")

        printer.printStr("TCK: ${ticketData.ticket}    ${ticketData.hora} ${ticketData.fecha}")

        printer.setAlignStyle(0x02)
        printer.printStr("ID: ${ticketData.id}")

        // printer.printBmp(qrBitmap)

        printer.feed(30)
        printer.print(null)
        printer.close()
    }
}
