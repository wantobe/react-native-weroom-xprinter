package com.weroomxprinter

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import net.posprinter.IConnectListener
import net.posprinter.IDeviceConnection
import net.posprinter.POSConnect
import net.posprinter.TSPLPrinter
import net.posprinter.posprinterface.IStatusCallback

@ReactModule(name = WeroomXprinterModule.NAME)
class WeroomXprinterModule(reactContext: ReactApplicationContext) :
        NativeWeroomXprinterSpec(reactContext) {

  private var printer: TSPLPrinter? = null
  private var curConnect: IDeviceConnection? = null

  override fun getName(): String {
    return NAME
  }

  override fun init() {
    POSConnect.init(reactApplicationContext)
  }

  override fun close(): Boolean {
    try {
      curConnect?.closeSync()
    } catch (e: Exception) {
      return false
    }
    return true
  }

  override fun connect(address: String, type: Double, callback: Callback): Boolean {
    try {
      curConnect?.close()
      curConnect = POSConnect.createDevice(type.toInt())
      val isconnected: Boolean =
              curConnect!!.connectSync(
                      address,
                      IConnectListener { code, connInfo, msg ->
                        when (code) {
                          POSConnect.CONNECT_SUCCESS -> callback(true)
                          POSConnect.CONNECT_FAIL -> callback(false)
                          POSConnect.CONNECT_INTERRUPT -> callback(false)
                          POSConnect.USB_ATTACHED -> callback(true)
                          POSConnect.USB_DETACHED -> callback(false)
                        }
                      }
              )
      printer = TSPLPrinter(curConnect)
      return isconnected
    } catch (e: Exception) {
      return false
    }
  }

  override fun printerStatus(timeout: Double, promise: Promise) {
    printer?.printerStatus(timeout.toInt(), IStatusCallback { promise.resolve(it) })
  }

  override fun TSPLprint(array: ReadableArray, count: Double, promise: Promise) {
    printer?.let {
      synchronized(it) {
        val thread =
                Thread(
                        Runnable {
                          for (i in 0 until array.size()) {
                            if (array.getType(i) == ReadableType.Map) {
                              val map = array.getMap(i)
                              val iterator = map!!.keySetIterator()
                              while (iterator.hasNextKey()) {
                                val key = iterator.nextKey()
                                setPtrintRules(key, map)
                              }
                            }
                          }
                          it.print(count.toInt())
                        }
                )
        thread.start()
        thread.join()

        it.printerStatus(1000) { i -> promise.resolve(i) }
      }
    }
            ?: run { promise.reject("-1", "请先初始打印机") }
  }

  fun setPtrintRules(key: String, map: ReadableMap) {

    val value =
            when (map.getType(key)) {
              ReadableType.Null -> null
              ReadableType.Boolean -> map.getBoolean(key)
              ReadableType.Number -> map.getDouble(key)
              ReadableType.String -> map.getString(key)
              ReadableType.Map -> map.getMap(key)
              ReadableType.Array -> map.getArray(key)
            }

    when (key) {
      "sizeInch" ->
              if (value is ReadableMap)
                      printer?.sizeInch(value.getDouble("width"), value.getDouble("height"))
      "sizeMm" ->
              if (value is ReadableMap)
                      printer?.sizeMm(value.getDouble("width"), value.getDouble("height"))
      "gapInch" ->
              if (value is ReadableMap) printer?.gapInch(value.getDouble("m"), value.getDouble("n"))
      "gapMm" ->
              if (value is ReadableMap) printer?.gapMm(value.getDouble("m"), value.getDouble("n"))
      "speed" -> if (value is Number) printer?.speed(value.toDouble())
      "density" -> if (value is Number) printer?.density(value.toInt())
      "direction" -> if (value is Number) printer?.direction(value.toInt())
      "cls" -> if (value is Boolean && value) printer?.cls()
      "box" ->
              if (value is ReadableMap)
                      printer?.box(
                              value.getInt("x"),
                              value.getInt("y"),
                              value.getInt("w"),
                              value.getInt("h"),
                              value.getInt("thickness")
                      )
      "bar" ->
              if (value is ReadableMap)
                      printer?.bar(
                              value.getInt("x"),
                              value.getInt("y"),
                              value.getInt("w"),
                              value.getInt("h")
                      )
      "barcode" ->
              if (value is ReadableMap)
                      printer?.barcode(
                              value.getInt("x"),
                              value.getInt("y"),
                              value.getString("codeType"),
                              value.getInt("height"),
                              value.getInt("readable"),
                              value.getInt("rotation"),
                              value.getInt("narrow"),
                              value.getInt("wide"),
                              value.getString("content")
                      )
      "text" ->
              if (value is ReadableMap)
                      printer?.text(
                              value.getInt("x"),
                              value.getInt("y"),
                              value.getString("font"),
                              value.getInt("rotation"),
                              value.getInt("xRatio"),
                              value.getInt("yRatio"),
                              value.getString("content")
                      )
      "offsetInch" -> if (value is Number) printer?.offsetInch(value.toDouble())
      "offsetMm" -> if (value is Number) printer?.offsetMm(value.toDouble())
      "reference" ->
              if (value is ReadableMap) printer?.reference(value.getInt("x"), value.getInt("y"))
      "qrcode" ->
              if (value is ReadableMap)
                      printer?.qrcode(
                              value.getInt("x"),
                              value.getInt("y"),
                              value.getString("ecLevel"),
                              value.getInt("cellWidth"),
                              value.getString("mode"),
                              value.getInt("rotation"),
                              value.getString("data")
                      )
      "feed" -> if (value is Number) printer?.feed(value.toInt())
      "backFeed" -> if (value is Number) printer?.backFeed(value.toInt())
      "limitFeedMm" -> if (value is Number) printer?.limitFeedMm(value.toInt())
      "limitFeedInch" -> if (value is Number) printer?.limitFeedInch(value.toInt())
      "home" -> if (value is Boolean && value) printer?.home()
      "codePage" -> if (value is String) printer?.codePage(value)
      "sound" ->
              if (value is ReadableMap)
                      printer?.sound(value.getInt("level"), value.getInt("interval"))
      "bitmap" ->
              if (value is ReadableMap) {
                // todo getBmp
                printer?.bitmap(
                        value.getInt("x"),
                        value.getInt("y"),
                        value.getInt("mode"),
                        value.getInt("width"),
                        null
                )
              }
      "erase" ->
              if (value is ReadableMap)
                      printer?.erase(
                              value.getInt("x"),
                              value.getInt("y"),
                              value.getInt("width"),
                              value.getInt("height")
                      )
      "reverse" ->
              if (value is ReadableMap)
                      printer?.reverse(
                              value.getInt("x"),
                              value.getInt("y"),
                              value.getInt("width"),
                              value.getInt("height")
                      )
      "cut" -> if (value is Boolean && value) printer?.cut()
      "setPeel" -> if (value is Boolean) printer?.setPeel(value)
      "setTear" -> if (value is Boolean) printer?.setTear(value)
      "blineInch" ->
              if (value is ReadableMap)
                      printer?.blineInch(value.getDouble("m"), value.getDouble("n"))
      "blineMm" ->
              if (value is ReadableMap) printer?.blineMm(value.getDouble("m"), value.getDouble("n"))
    }
  }

  companion object {
    const val NAME = "WeroomXprinter"
  }
}
