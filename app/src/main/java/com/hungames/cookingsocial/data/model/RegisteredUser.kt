package com.hungames.cookingsocial.data.model

import android.location.Address
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat
import java.util.*

@Parcelize
@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class RegisteredUser @JvmOverloads constructor(
    @ColumnInfo(name = "username")
    val username: String = "",

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "validated")
    val isValidated: Boolean = false,

    @PrimaryKey @ColumnInfo(name = "uid")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "dateCreated")
    val date: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "street")
    val street: String = "",

    @ColumnInfo(name = "postal")
    val postal: String = "",

    @ColumnInfo(name = "city")
    val city: String = "",

    @ColumnInfo(name = "country")
    val country: String = "",

    @ColumnInfo(name = "userDescription")
    val userDescription: String = ""
) : Parcelable {
    val createDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(date)
}


data class UserMinimal(
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "validated") val isValidated: Boolean,
    @ColumnInfo(name = "dateCreated") val date: Long,
    @ColumnInfo(name = "uid") val uid: String
)

@Parcelize
data class UserNeighbors(
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "validated") val isValidated: Boolean,
    @ColumnInfo(name = "street") val street: String,
    @ColumnInfo(name = "postal") val postal: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "userDescription") val userDesc: String
): Parcelable