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
        try {
            // Sunucu üzerinden güvenli çıkış yapmayı dener (internet gerektirir)
            supabaseClient.auth.signOut()
        } catch (e: Exception) {
            // Eğer internet yoksa network çağrısı Exception fırlatır ve uygulamayı çökertir.
            // Bu hatayı yakalayıp, sadece cihazdaki (lokal) oturum verilerini siliyoruz.
            // Böylece kullanıcı internetsiz kalsa bile takılmadan çıkış yapabilir.
            supabaseClient.auth.clearSession()
        }
    }
}
