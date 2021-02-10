package com.hungames.cookingsocial.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hungames.cookingsocial.util.NutritionType
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(foreignKeys = [ForeignKey(entity = RegisteredUser::class, parentColumns = arrayOf("uid"), childColumns = arrayOf("uid"), onDelete = ForeignKey.CASCADE)])
data class Dishes (
        @PrimaryKey(autoGenerate = true) var id: Int,
        @ColumnInfo(name = "uid", index = true) val uid: String,
        @ColumnInfo(name = "dish_name") val dishName: String,
        @ColumnInfo(name = "dish_description") val dishDescription: String,
        @ColumnInfo(name = "dish_price") val dishPrice: Int,
        @ColumnInfo(name = "nutrition_type") val nutritionType: NutritionType,
        @ColumnInfo(name = "quantity") val quantity: Int
        ):Parcelable