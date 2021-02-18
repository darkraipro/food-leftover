package com.hungames.cookingsocial.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hungames.cookingsocial.util.NutritionType
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "dish", foreignKeys = [ForeignKey(entity = RegisteredUser::class, parentColumns = arrayOf("uid"), childColumns = arrayOf("uid"), onDelete = ForeignKey.CASCADE)])
data class Dishes (
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        @ColumnInfo(name = "uid", index = true) val uid: String,
        @ColumnInfo(name = "dish_name") val dishName: String,
        @ColumnInfo(name = "dish_description") val dishDescription: String,
        @ColumnInfo(name = "dish_price") val dishPrice: Float,
        @ColumnInfo(name = "nutrition_type") val nutritionType: NutritionType = NutritionType.ALL,
        @ColumnInfo(name = "quantity") val quantity: Int
        ):Parcelable


// TODO: Add timestamp plus uid who ordered food. Modify preference manager to keep uid while logged in
@Parcelize
data class Order (val quantity: Int, val dish: Dishes): Parcelable{
        val orderPrice = dish.dishPrice * quantity
}