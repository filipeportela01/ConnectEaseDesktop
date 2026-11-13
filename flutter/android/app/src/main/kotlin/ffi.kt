// ffi.kt

package ffi

import android.content.Context
import java.nio.ByteBuffer

import com.ion.connecteasedesktop.RdClipboardManager

object FFI {
    init {
        // libconnectease.so (nome novo, sem a marca do RustDesk); cai no librustdesk.so
        // enquanto o build ainda copiar a lib com o nome antigo para jniLibs.
        try {
            System.loadLibrary("connectease")
        } catch (e: UnsatisfiedLinkError) {
            System.loadLibrary("rustdesk")
        }
    }

    external fun init(ctx: Context)
    external fun onAppStart(ctx: Context)
    external fun setClipboardManager(clipboardManager: RdClipboardManager)
    external fun startServer(app_dir: String, home_dir: String, custom_client_config: String)
    external fun startService()
    external fun onVideoFrameUpdate(buf: ByteBuffer)
    external fun onAudioFrameUpdate(buf: ByteBuffer)
    external fun translateLocale(localeName: String, input: String): String
    external fun refreshScreen()
    external fun closeAllSessions()
    external fun setFrameRawEnable(name: String, value: Boolean)
    external fun setCodecInfo(info: String)
    external fun getLocalOption(key: String): String
    external fun getBuildinOption(key: String): String
    external fun onClipboardUpdate(clips: ByteBuffer)
    external fun isServiceClipboardEnabled(): Boolean
}
