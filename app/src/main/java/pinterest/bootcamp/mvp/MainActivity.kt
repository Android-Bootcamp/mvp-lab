package pinterest.bootcamp.mvp

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.RoundedCornersTransformation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class MainActivity : AppCompatActivity() {

    private lateinit var pinImage: ImageView
    private lateinit var switchButton: TextView
    private lateinit var saveButton: TextView
    private lateinit var savedBadge: View

    private val pinRepository = PinRepository()
    private val disposables = CompositeDisposable()
    private var pins = listOf<Pin>()
    private val savedPins = mutableListOf<Int>()
    private var selectedPinIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pinImage = findViewById(R.id.pin_image)
        switchButton = findViewById(R.id.pin_switch)
        saveButton = findViewById(R.id.pin_save)
        savedBadge = findViewById(R.id.pin_saved_badge)

        pinRepository
            .getPins()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { newPins: List<Pin> ->
                pins = newPins
                loadImage()
            }
            .also {
                disposables.add(it)
            }

        switchButton.setOnClickListener {
            selectedPinIndex = (selectedPinIndex + 1) % pins.size
            val selectedPin = pins[selectedPinIndex]
            if (savedPins.contains(selectedPin.id)) {
                savedBadge.visibility = VISIBLE
                saveButton.text = resources.getString(R.string.unsave)
                saveButton.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(baseContext, R.color.gestalt_green)
                )
            } else {
                savedBadge.visibility = GONE
                saveButton.text = resources.getString(R.string.save)
                saveButton.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(baseContext, R.color.gestalt_red)
                )
            }
            loadImage()
        }

        saveButton.setOnClickListener {
            val selectedPin = pins[selectedPinIndex]
            if (savedPins.contains(selectedPin.id)) {
                savedPins.remove(selectedPin.id)
                savedBadge.visibility = GONE
                saveButton.text = resources.getString(R.string.save)
                saveButton.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(baseContext, R.color.gestalt_red)
                )
            } else {
                savedPins.add(selectedPin.id)
                savedBadge.visibility = VISIBLE
                saveButton.text = resources.getString(R.string.unsave)
                saveButton.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(baseContext, R.color.gestalt_green)
                )
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.dispose()
    }

    private fun loadImage() {
        pinImage.load(pins[selectedPinIndex].imageUrl) {
            transformations(
                RoundedCornersTransformation(
                    radius = resources.getDimension(R.dimen.gestalt_image_corner_radius)
                )
            )
        }
    }
}