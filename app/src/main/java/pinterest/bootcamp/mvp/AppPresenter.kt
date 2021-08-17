package pinterest.bootcamp.mvp

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class AppPresenter(
    pinRepository: PinRepository,
    initialState: AppState = AppState()
) : AppActions {

    private val disposables = CompositeDisposable()
    private var view: AppView? = null
    private var state = initialState

    init {
        pinRepository
            .getPins()
            .observeOn(AndroidSchedulers.mainThread())
            .map { newPins ->
                AppState(
                    pins = newPins,
                    selectedPinIndex = 0
                )
            }
            .subscribe { state ->
                this.state = state
                view?.render(state)
            }
            .also {
                disposables.add(it)
            }
    }

    fun bind(view: AppView) {
        this.view = view
    }

    fun unbind() {
        view = null
        disposables.dispose()
    }

    override fun onAction(action: AppAction) {
        when (action) {
            AppAction.Next -> {
                state = state.next()
                view?.render(state)

            }
            AppAction.ToggleSave -> {
                state = state.toggleSave(state.selectedPinIndex)
                view?.render(state)
            }
        }
    }

    private fun AppState.next() = copy(
        selectedPinIndex = (selectedPinIndex + 1) % pins.size
    )

    private fun AppState.toggleSave(index: Int) = copy(
        savedPins = when {
            this.savedPins.contains(pins[index]) -> {
                this.savedPins.toMutableList().apply {
                    remove(pins[index])
                    toList()
                }
            }
            else -> {
                this.savedPins.toMutableList().apply {
                    add(pins[index])
                    toList()
                }
            }
        }
    )
}