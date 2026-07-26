package me.spartacus04.caveilluminator.config.fields

import com.google.gson.annotations.SerializedName

enum class PlacingModeField {
    @SerializedName("torch")
    TORCH,
    @SerializedName("lantern")
    LANTERN,
    @SerializedName("lantern_hanging")
    LANTERN_HANGING,
    @SerializedName("embedded_blocks")
    EMBEDDED_BLOCKS,
    @SerializedName("blocks")
    BLOCKS,
    @SerializedName("light_block")
    LIGHT_BLOCK,
}