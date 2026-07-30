package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ChatMessage
import com.example.model.MessageSender
import com.example.network.WebhookClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            sender = MessageSender.AI,
            text = "Hello! I am Assets AI. What would you like to work on today, or what is your plan?"
        )
    ),
    val isThinking: Boolean = false
)

class ChatViewModel(
    private val webhookClient: WebhookClient = WebhookClient()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(inputText: String) {
        val trimmedText = inputText.trim()
        if (trimmedText.isEmpty() || _uiState.value.isThinking) return

        val userMsg = ChatMessage(
            sender = MessageSender.USER,
            text = trimmedText
        )

        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMsg,
                isThinking = true
            )
        }

        viewModelScope.launch {
            val result = webhookClient.sendMessage(trimmedText)

            result.fold(
                onSuccess = { aiResponseText ->
                    val aiMsg = ChatMessage(
                        sender = MessageSender.AI,
                        text = aiResponseText
                    )
                    _uiState.update { currentState ->
                        currentState.copy(
                            messages = currentState.messages + aiMsg,
                            isThinking = false
                        )
                    }
                },
                onFailure = { error ->
                    val errorMsgText = "Assets AI Connection Alert: ${error.localizedMessage ?: "Unable to reach the webhook service."}. Please check connection or endpoint status and try again."
                    val aiMsg = ChatMessage(
                        sender = MessageSender.AI,
                        text = errorMsgText
                    )
                    _uiState.update { currentState ->
                        currentState.copy(
                            messages = currentState.messages + aiMsg,
                            isThinking = false
                        )
                    }
                }
            )
        }
    }
}
