package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.data.local.dao.FightCardDao
import com.berkekucuk.mmaapp.data.local.entity.FightEntity
import com.berkekucuk.mmaapp.data.local.entity.FighterEntity
import com.berkekucuk.mmaapp.data.local.entity.ParticipantEntity
import com.berkekucuk.mmaapp.data.mapper.toEntity
import com.berkekucuk.mmaapp.data.remote.api.FightCardRemoteDataSource
import com.berkekucuk.mmaapp.domain.repository.FightCardRepository
import com.berkekucuk.mmaapp.data.mapper.toDomain
import com.berkekucuk.mmaapp.domain.model.FightCardDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException


class FightCardRepositoryImpl(
    private val remoteDataSource: FightCardRemoteDataSource,
    private val dao: FightCardDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): FightCardRepository {

    override fun getFightsByEvent(eventId: String): Flow<List<FightCardDomain>> {
        return dao.getFightCardsByEvent(eventId)
            .map { list ->
                list.map { it.toDomain() }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun syncFightsByEvent(eventId: String, status: String): Result<Unit> {
        return withContext(ioDispatcher){

            try {
                val localFightCount = dao.getFightCountForEvent(eventId)
                if(localFightCount == 0){
                    fetchAndSaveData(eventId)
                    Result.success(Unit)
                }
                else if (status != "Completed") {
                    fetchAndSaveData(eventId)
                    Result.success(Unit)
                }
                else {
                    Result.success(Unit)
                }
            }catch (e: Exception){
                if (e is CancellationException) throw e
                Result.failure(e)
            }
        }
    }

    private suspend fun fetchAndSaveData(eventId: String){
        val remoteDtos = remoteDataSource.getFightCardsByEvent(eventId)

        val fightEntities = mutableListOf<FightEntity>()
        val participantEntities = mutableListOf<ParticipantEntity>()
        val fighterEntities = mutableListOf<FighterEntity>()

        remoteDtos.forEach { fightDto ->
            fightEntities.add(fightDto.toEntity())

            fightDto.participants.forEach { partDto ->
                participantEntities.add(partDto.toEntity())

                partDto.fighter?.let { fighterDto ->
                    fighterEntities.add(fighterDto.toEntity())
                }
            }
        }

        dao.insertFullFightCardData(
            fights = fightEntities,
            fighters = fighterEntities,
            participants = participantEntities
        )
    }
}