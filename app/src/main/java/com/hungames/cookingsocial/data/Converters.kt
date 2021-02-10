package com.hungames.cookingsocial.data

import android.location.Address
import androidx.room.TypeConverter
import com.hungames.cookingsocial.util.NutritionType
import java.util.*

class Converters {

    @TypeConverter
    fun fromAddress(addr: Address?): String{
        if (addr == null){
            return ""
        }
        val lst =  with(addr){
            (0..maxAddressLineIndex).map { getAddressLine(it) }
        }
        return lst.joinToString(separator = ", ")
    }

    @TypeConverter
    fun fromStringAddress(addr: String): Address{
        if (addr.isEmpty()){
            return Address(Locale.getDefault())
        }
        val ad = Address(Locale.getDefault())
        val addressParts = addr.split(",")
        if (addressParts.size < 3){
            return Address(Locale.getDefault())
        }

        // Country
        ad.setAddressLine(2, addressParts[2])
        // Postal
        ad.setAddressLine(1, addressParts[1])
        // Street
        ad.setAddressLine(0, addressParts[0])
        return ad
    }

    @TypeConverter
    fun toNutritionType(value: Int) = enumValues<NutritionType>()[value]

    @TypeConverter
    fun fromNutritionType(value: NutritionType) = value.ordinal
}