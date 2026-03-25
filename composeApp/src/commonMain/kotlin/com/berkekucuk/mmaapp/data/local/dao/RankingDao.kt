package com.berkekucuk.mmaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.berkekucuk.mmaapp.data.local.entity.WeightClassEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RankingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightClasses(weightClasses: List<WeightClassEntity>)

    @Query("SELECT * FROM weight_classes ORDER BY sort_order ASC")
    fun getAllWeightClasses(): Flow<List<WeightClassEntity>>

    @Query("SELECT * FROM weight_classes WHERE id = :weightClassId")
    fun getWeightClassById(weightClassId: String): Flow<WeightClassEntity?>

    @Query("SELECT COUNT(*) FROM weight_classes")
    suspend fun getCount(): Int
}
