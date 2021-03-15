package uz.eldor.phonecallsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private val mListener = MyPhoneCallListener()
    private var TAG = "MainActivity"
    private lateinit var mTelephonyManager: TelephonyManager
    private var MY_PERMISSIONS_REQUEST_CALL_PHONE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTelephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        if (isTelephonyEnabled()) {
            checkForPhonePermission()
            Log.d(TAG, "onCreate: Telephony Enabled")

            mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE)

        } else {
            Toast.makeText(this, "Telephony nit enabled", Toast.LENGTH_SHORT).show()
            disableCallButton()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isTelephonyEnabled()) {
            mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE)
        }
    }

    private fun disableCallButton() {
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CALL_PHONE -> {
                if (permissions[0] == Manifest.permission.CALL_PHONE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: MY_PERMISSIONS_REQUEST_CALL_PHONE")
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: failure permissions")
                    Toast.makeText(this, "Failure permissions", Toast.LENGTH_SHORT).show()
                    disableCallButton()
                }
            }
        }

    }


    private fun checkForPhonePermission() = if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CALL_PHONE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        Log.d(TAG, "checkForPhonePermission: Permissions not granted")
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CALL_PHONE),
            MY_PERMISSIONS_REQUEST_CALL_PHONE
        )
    } else {
        //Permission already granted
    }

    private fun isTelephonyEnabled(): Boolean {
        if (mTelephonyManager != null) {
            if (mTelephonyManager.simState == TelephonyManager.SIM_STATE_READY) {
                return true
            }
        }

        return false
    }


    fun callNumber(view: View) {
        val text = findViewById<EditText>(R.id.number_to_call)

        if (text == null) {
            Toast.makeText(this, "Raqam kiritmadingiz!", Toast.LENGTH_SHORT).show()
            return
        }

        val phoneNumber = String.format("tel: %s", text.text.toString())

        val callIntent = Intent(Intent.ACTION_CALL)

        callIntent.data = Uri.parse(phoneNumber)

        if (callIntent.resolveActivity(packageManager) != null) {
            checkForPhonePermission()
            startActivity(callIntent)
        } else {
            Log.e(TAG, "dialNumber: Can`t resolve App for ACTION DEAL Intent.")
        }

    }
}