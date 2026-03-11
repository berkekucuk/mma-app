package com.berkekucuk.mmaapp.data.repository

import com.berkekucuk.mmaapp.domain.model.AuthState
import com.berkekucuk.mmaapp.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    override val authState: Flow<AuthState> = supabaseClient.auth.sessionStatus.map { status ->
        when (status) {
            is SessionStatus.Initializing -> AuthState.Loading
            is SessionStatus.Authenticated -> AuthState.Authenticated(
                userId = status.session.user?.id ?: "",
                email = status.session.user?.email
            )
            is SessionStatus.NotAuthenticated -> AuthState.Unauthenticated
            else -> AuthState.Unauthenticated
        }
    }

    override suspend fun signOut() {
        supabaseClient.auth.signOut()
    }
}
