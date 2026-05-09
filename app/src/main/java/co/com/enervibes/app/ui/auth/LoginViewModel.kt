package co.com.enervibes.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.com.enervibes.app.data.local.TokenManager
import co.com.enervibes.app.data.model.UserModel
import co.com.enervibes.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

class LoginViewModel(tokenManager: TokenManager) : ViewModel() {
    private val repository = AuthRepository(tokenManager)
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) { _uiState.update { it.copy(email = value, error = null) } }
    fun onPasswordChange(value: String) { _uiState.update { it.copy(password = value, error = null) } }
    fun onRememberMeChange(value: Boolean) { _uiState.update { it.copy(rememberMe = value) } }

    fun login() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Completa todos los campos") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = repository.login(state.email.trim(), state.password, state.rememberMe)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error de conexión") }
                }
            )
        }
    }
}
