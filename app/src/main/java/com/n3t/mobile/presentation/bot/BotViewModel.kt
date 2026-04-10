package com.n3t.mobile.presentation.bot

import androidx.lifecycle.ViewModel
import com.n3t.mobile.data.model.bot.BotModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class InteractionState {
    NONE,
    LISTENING,
    PROCESSING,
    RESPONDING
}

class BotViewModel : ViewModel() {

    private val _botInteractionState = MutableStateFlow(InteractionState.NONE)
    val botInteractionState: StateFlow<InteractionState> = _botInteractionState.asStateFlow()
    fun setBotInteractionState(state: InteractionState) {
        _botInteractionState.value = state
    }

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()
    fun setRecognizedText(text: String) {
        _recognizedText.value = text
    }

    private val _botModel = MutableStateFlow<BotModel?>(null)
    val botModel: StateFlow<BotModel?> = _botModel.asStateFlow()

    fun handleSkill(newBotModel: BotModel) {
        _botModel.value = newBotModel
    }

    private val _suggestBot = MutableStateFlow<String?>(null)
    val suggest: StateFlow<String?> = _suggestBot.asStateFlow()

    fun saveBeforeSuggest(text: String?) {
        _suggestBot.value = text
    }

    private val _onStartListening = MutableStateFlow<Boolean?>(null)
    val onStartListening: StateFlow<Boolean?> = _onStartListening.asStateFlow()

    fun setOnStartListening(flag: Boolean?) {
        _onStartListening.value = flag
    }

    private val _onStopListening = MutableStateFlow<Boolean?>(null)
    val onStopListening: StateFlow<Boolean?> = _onStopListening.asStateFlow()

    fun setOnStopListening(flag: Boolean?) {
        _onStopListening.value = flag
    }

    private val _onHzChanged = MutableStateFlow<Float?>(null)
    val onHzChanged: StateFlow<Float?> = _onHzChanged.asStateFlow()

    fun setOnHzChanged(rmsdB: Float?) {
        _onHzChanged.value = rmsdB
    }

    fun handleLlmMessage(text: String) {
        try {
            val processText = text.trim().replace(Regex("\\s+"), " ")
            val gson = Gson()
            val dataModel = gson.fromJson(processText, BotModel::class.java)
            handleSkill(dataModel)
        } catch (e: Exception) {
            var dataModel = BotModel("Knowledge info", "stop", "stop", "")
            if (text != "Stop") {
                dataModel = BotModel("Knowledge info", text, "", "")
            }
            handleSkill(dataModel)
        }
    }

    fun reset() {
        setOnStartListening(null)
        setOnHzChanged(null)
        setOnStopListening(null)
    }
}
