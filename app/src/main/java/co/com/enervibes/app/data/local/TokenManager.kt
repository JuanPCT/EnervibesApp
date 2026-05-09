package co.com.enervibes.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "enervibes_session")

class TokenManager(private val context: Context) {

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("access_token")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_USER_ROLE = stringPreferencesKey("user_role")
        private val KEY_COMPANY_ID = stringPreferencesKey("company_id")
        private val KEY_BRANCH_ID = stringPreferencesKey("branch_id")
        private val KEY_BRANCH_NAME = stringPreferencesKey("branch_name")
    }

    val token: Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN] }
    val userName: Flow<String?> = context.dataStore.data.map { it[KEY_USER_NAME] }
    val userEmail: Flow<String?> = context.dataStore.data.map { it[KEY_USER_EMAIL] }
    val userRole: Flow<String?> = context.dataStore.data.map { it[KEY_USER_ROLE] }

    suspend fun saveSession(
        token: String,
        userId: String,
        userName: String,
        userEmail: String,
        userRole: String,
        companyId: String,
        branchId: String,
        branchName: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
            prefs[KEY_USER_ID] = userId
            prefs[KEY_USER_NAME] = userName
            prefs[KEY_USER_EMAIL] = userEmail
            prefs[KEY_USER_ROLE] = userRole
            prefs[KEY_COMPANY_ID] = companyId
            prefs[KEY_BRANCH_ID] = branchId
            prefs[KEY_BRANCH_NAME] = branchName
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun getToken(): String? = context.dataStore.data.first()[KEY_TOKEN]
    suspend fun getCompanyId(): String? = context.dataStore.data.first()[KEY_COMPANY_ID]
    suspend fun getBranchId(): String? = context.dataStore.data.first()[KEY_BRANCH_ID]
}
