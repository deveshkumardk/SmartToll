package com.alorb.smarttoll.qrcode

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.alorb.smarttoll.ui.theme.SmartTollTheme
import com.dynamsoft.dbr.BarcodeReader
import com.dynamsoft.dbr.BarcodeReaderException
import com.dynamsoft.dbr.TextResult
import com.dynamsoft.dce.CameraEnhancer
import com.dynamsoft.dce.DCECameraView

class QrCodeActivity: ComponentActivity() {
    private lateinit var mCameraEnhancer: CameraEnhancer
    private lateinit var mBarcodeReader: BarcodeReader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTollTheme {
                val context = LocalContext.current

                var barcodeTextResult by remember {
                    mutableStateOf("")
                }

                var hasCameraPermission by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { granted ->
                        hasCameraPermission = granted
                        if (granted == true) {
                            startScanning() { result ->
                                barcodeTextResult = result.barcodeFormatString+": "+result.barcodeText
                            }
                            mCameraEnhancer.open()
                        }
                    }
                )

                LaunchedEffect(key1 = true){
                    initLicense()
                    initDBR()
                    launcher.launch(Manifest.permission.CAMERA)
                }

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    AndroidView(factory = {context ->
                        mCameraEnhancer = CameraEnhancer(context.findActivity())
                        val mCameraView: DCECameraView
                        mCameraView = DCECameraView(context)
                        mCameraView.overlayVisible = true
                        mCameraEnhancer.cameraView = mCameraView
                        mCameraView
                    })
                    BarcodeText(text = barcodeTextResult)
                }
            }
        }
    }

    @Composable
    fun BarcodeText(text:String) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp
        )
    }

    private fun initLicense(){
        BarcodeReader.initLicense(
            "DLS2eyJoYW5kc2hha2VDb2RlIjoiMjAwMDAxLTE2NDk4Mjk3OTI2MzUiLCJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSIsInNlc3Npb25QYXNzd29yZCI6IndTcGR6Vm05WDJrcEQ5YUoifQ=="
        ) { isSuccess, error ->
            if (!isSuccess) {
                error.printStackTrace()
            }else{
                Log.d("DBR","license initialized")
            }
        }
    }

    private fun initDBR(){
        try {
            // Create an instance of Dynamsoft Barcode Reader.
            mBarcodeReader = BarcodeReader()
            // Bind the Camera Enhancer instance to the Barcode Reader instance to get frames from camera.
            mBarcodeReader.setCameraEnhancer(mCameraEnhancer)
        }catch (e: BarcodeReaderException){
            e.printStackTrace()
        }
    }

    private fun startScanning(scanned:(TextResult) -> Unit){
        try{
            mBarcodeReader.setTextResultListener { id, imageData, textResults ->
                //Log.d("DBR", textResults.size.toString())
                if (textResults.size>0) {
                    scanned(textResults[0])
                }
            }
            mBarcodeReader.startScanning()
        } catch (e: BarcodeReaderException) {
            e.printStackTrace()
        }
    }
}

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}