package me.spartacus04.caveilluminator.block

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

/**
 * Custom type adapter for cave illuminator save data
 */
class IlluminatorTypeAdapter : TypeAdapter<Map<Location, Map<Location, Material>>>() {
    override fun write(out: JsonWriter, value: Map<Location, Map<Location, Material>>?) {
        if (value == null) {
            out.nullValue()
            return
        }

        out.beginObject()
        for ((key, innerMap) in value) {
            out.name("${key.world?.name}:${key.blockX}:${key.blockY}:${key.blockZ}")

            out.beginObject()
            for ((innerKey, material) in innerMap) {
                out.name("${innerKey.world?.name}:${innerKey.blockX}:${innerKey.blockY}:${innerKey.blockZ}")
                out.value(material.name)
            }
            out.endObject()
        }
        out.endObject()
    }

    override fun read(`in`: JsonReader): Map<Location, Map<Location, Material>>? {
        if (`in`.peek() == JsonToken.NULL) {
            `in`.nextNull()
            return null
        }

        val result = mutableMapOf<Location, MutableMap<Location, Material>>()

        `in`.beginObject()
        while (`in`.hasNext()) {
            val key = parseLocationKey(`in`.nextName())

            val innerMap = mutableMapOf<Location, Material>()

            `in`.beginObject()
            while (`in`.hasNext()) {
                val innerKey = parseLocationKey(`in`.nextName())
                val material = Material.valueOf(`in`.nextString())

                innerMap[innerKey] = material
            }
            `in`.endObject()

            result[key] = innerMap
        }
        `in`.endObject()

        return result
    }

    /**
     * Parses a "world:x:y:z" key back into a Location.
     * Uses block coordinates (Int), matching how they were written.
     *
     * @param key The string key in the format "world:x:y:z".
     * @return The
     */
    private fun parseLocationKey(key: String): Location {
        val parts = key.split(":")
        require(parts.size == 4) { "Malformed location key: $key" }

        val worldName = parts[0]
        val x = parts[1].toInt()
        val y = parts[2].toInt()
        val z = parts[3].toInt()

        val world = Bukkit.getWorld(worldName)

        // Using block coords + 0.5 offset is optional; here we keep it as
        // exact block coordinates since that's what was serialized.
        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }
}