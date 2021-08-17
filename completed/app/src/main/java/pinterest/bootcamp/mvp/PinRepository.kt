package pinterest.bootcamp.mvp

import io.reactivex.Single

data class Pin(
    val id: Int,
    val imageUrl: String
)

private val pins = listOf(
    Pin(
        id = 1,
        imageUrl = "https://i.pinimg.com/564x/b9/46/32/b946326ee75e13a1200648f86d225053.jpg"
    ),
    Pin(
        id = 2,
        imageUrl = "https://i.pinimg.com/564x/f0/ce/e6/f0cee6248f8a248b7bb31993c69eaf87.jpg"
    ),
    Pin(
        id = 3,
        imageUrl = "https://i.pinimg.com/564x/9c/da/f6/9cdaf60645b56b02b25684f6e53db09b.jpg"
    ),
    Pin(
        id = 4,
        imageUrl = "https://i.pinimg.com/564x/02/05/9b/02059bcc74c2752defb0b2c57c3c1b66.jpg"
    ),
)

class PinRepository {
    fun getPins(): Single<List<Pin>> {
        return Single.just(pins)
    }
}