package pinterest.bootcamp.mvp

interface AppView {
    fun render(state: AppState)
}

interface AppActions {
    fun onAction(action: AppAction)
}

data class AppState(
    val pins: List<Pin> = emptyList(),
    val savedPins: List<Pin> = emptyList(),
    val selectedPinIndex: Int = -1
)  {
    val selectedPinSaved = if (selectedPinIndex < 0) {
        false
    } else {
        savedPins.contains(pins[selectedPinIndex])
    }
}

enum class AppAction {
    Next,
    ToggleSave
}