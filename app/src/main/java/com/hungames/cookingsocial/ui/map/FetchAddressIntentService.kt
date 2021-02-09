package com.hungames.cookingsocial.ui.map

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import androidx.core.app.JobIntentService
import com.hungames.cookingsocial.util.Constants
import com.hungames.cookingsocial.util.TAG_MAP
import timber.log.Timber
import java.io.IOException
import java.util.*

class FetchAddressIntentService: JobIntentService() {

    private var receiver: ResultReceiver? = null
    override fun onHandleWork(intent: Intent) {
        receiver = intent.getParcelableExtra(Constants.RECEIVER)

        // Defensive programming. Making sure that receiver and location data was really sent
        if (receiver == null){
            Timber.tag(TAG_MAP).wtf("No receiver received. Can not send results")
            deliverResultToReceiver(Constants.FAILURE_RESULT, "No receiver.")
            return
        }
        val location = intent.getParcelableExtra<Location>(Constants.LOCATION_DATA)
        if (location == null){
            Timber.tag(TAG_MAP).wtf("No location data. Can not extract address")
            deliverResultToReceiver(Constants.FAILURE_RESULT, "No location data.")
            return
        }


        // use ResultReceiver to communicate result of geocoders initiliazation and address lookup
        val geocoder = Geocoder(this, Locale.getDefault())

        var address: List<Address> = emptyList()
        try {
            address = geocoder.getFromLocation(location.latitude, location.longitude, 2)


        }catch (io: IOException){
            Timber.tag(TAG_MAP).e("Sorry service is not available")
        }catch (i: IllegalArgumentException){
            Timber.tag(TAG_MAP).e("Invalid longitude or/and latitude used.")
        }

        // address found?
        if (address.isEmpty()){
            Timber.tag(TAG_MAP).e("No address found")
            deliverResultToReceiver(Constants.FAILURE_RESULT, "No address found")
        } else {
            val addr = address[0]
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)
            // getAddressLine
            val addrFrag = with(addr){
                (0..maxAddressLineIndex).map {
                    Timber.tag(TAG_MAP).i(getAddressLine(it))
                    getAddressLine(it) }
            }
            Timber.tag(TAG_MAP).i(addrFrag.joinToString(separator = "\n"))
            deliverResultToReceiver(Constants.SUCCESS_RESULT, addrFrag.joinToString(separator = "\n"))
        }

    }

    private fun deliverResultToReceiver(resultCode: Int, message: String) {
        val bundle = Bundle().apply { putString(Constants.RESULT_DATA_KEY, message) }
        receiver?.send(resultCode, bundle)
    }


    companion object{
        fun enqueueWork(context: Context, intent: Intent, jobId: Int){
            enqueueWork(context, FetchAddressIntentService::class.java, jobId, intent)
        }
    }
}