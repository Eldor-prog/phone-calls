package uz.eldor.phonecallsapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class MyPhoneCallListener : PhoneStateListener() {
    val TAG = "MainActivity"

    private var returningFromOffHook = false
    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        var message = "Phone status"

        when(state){
            TelephonyManager.CALL_STATE_RINGING -> {
                message = "$message ringing $phoneNumber"
//                Toast.makeText(, message, Toast.LENGTH_SHORT).show()
                Log.i(TAG, message)
            }
            TelephonyManager.CALL_STATE_OFFHOOK->{
                message = "$message offHook"
                Log.i(TAG, message)
                returningFromOffHook = true

            }
            TelephonyManager.CALL_STATE_IDLE -> {
                message = "$message idle"
                Log.i(TAG, message)

                /*if (returningFromOffHook) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        Log.i(TAG, "onCallStateChanged: Restarting app")

                        val intent = Intent()
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP


                    }
                }*/

            }
        }
    }
}