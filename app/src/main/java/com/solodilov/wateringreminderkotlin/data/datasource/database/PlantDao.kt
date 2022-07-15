package com.solodilov.wateringreminderkotlin.data.datasource.database

import androidx.room.*
import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.PlantDb
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plantDb: PlantDb): Long

    @Query("SELECT * FROM plant_table WHERE id =:id  ")
    suspend fun getPlantById(id: Long): PlantDb

    @Query("SELECT * FROM plant_table WHERE NOT id =:tempId ORDER BY name")
    fun getPlantList(tempId: Long): Flow<List<PlantDb>>

    @Query("DELETE FROM plant_table WHERE id =:id  ")
    suspend fun deletePlantById(id: Long)

    /*@Query("SELECT * FROM plant_table WHERE name LIKE :search")
    fun findPlantWithName(search: String): Flow<List<PlantDb>>*/

}