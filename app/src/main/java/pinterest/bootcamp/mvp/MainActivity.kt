package pinterest.bootcamp.mvp

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.load
import coil.transform.RoundedCornersTransformation

class MainActivity : AppCompatActivity(), AppView {

    private val presenter = AppPresenter(
        pinRepository = PinRepository()
    )

    private lateinit var pinImage: ImageView
    private lateinit var nextButton: TextView
    private lateinit var saveButton: TextView
    private lateinit var savedBadge: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.bind(this)

        pinImage = findViewById(R.id.pin_image)
        nextButton = findViewById(R.id.pin_next)
        saveButton = findViewById(R.id.pin_save)
        savedBadge = findViewById(R.id.pin_saved_badge)

        nextButton.setOnClickListener {
            presenter.onAction(AppAction.Next)
        }

        saveButton.setOnClickListener {
            presenter.onAction(AppAction.ToggleSave)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.unbind()
    }

    override fun render(state: AppState) {
        if (state.pins.isEmpty()) {
            toggleUiVisibility(false)
        } else {
            toggleUiVisibility(true)
            renderImage(state)
            renderButtons(state)
            savedBadge.isVisible = state.selectedPinSaved
        }
    }

    private fun renderImage(state: AppState) {
        pinImage.load(state.pins[state.selectedPinIndex].imageUrl) {
            transformations(
                RoundedCornersTransformation(
                    radius = resources.getDimension(R.dimen.gestalt_image_corner_radius)
                )
            )
            crossfade(true)
        }
    }

    private fun renderButtons(state: AppState) {
        with(saveButton) {
            text = if (state.selectedPinSaved) {
                resources.getString(R.string.unsave)
            } else {
                resources.getString(R.string.save)
            }
            backgroundTintList = if (state.selectedPinSaved) {
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        baseContext,
                        R.color.gestalt_green
                    )
                )
            } else {
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        baseContext,
                        R.color.gestalt_red
                    )
                )
            }
        }
    }

    private fun toggleUiVisibility(visible: Boolean) {
        nextButton.isVisible = visible
        saveButton.isVisible = visible
        pinImage.isVisible = visible
        savedBadge.isVisible = visible
    }
}