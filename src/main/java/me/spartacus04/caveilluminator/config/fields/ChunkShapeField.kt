import com.google.gson.annotations.SerializedName

enum class ChunkShapeField {
    @SerializedName("square")
    SQUARE,
    @SerializedName("circle")
    CIRCLE,
    @SerializedName("diamond")
    DIAMOND
}