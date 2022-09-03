package kr.ac.kpu.ce2019152012.vegan_life.Classes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder

class BroadcateReceiver : BroadcastReceiver() {
    val randnum = mutableSetOf<Int>()
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(Intent.ACTION_DATE_CHANGED == p1!!.action){
            while(randnum.size<4){ randnum.add((1..41).random())}
        }
    }

    override fun peekService(myContext: Context?, service: Intent?): IBinder {
        return super.peekService(myContext, service)
    }
}