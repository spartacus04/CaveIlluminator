package me.spartacus04.caveilluminator.block

import me.spartacus04.caveilluminator.CaveIlluminator
import me.spartacus04.colosseum.scheduler.scheduling.ScheduledTask
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.block.Block

/**
 * Manages the spawning of particles for cave illuminator blocks. Each block can have its own particle spawning task, which is started when the block is placed and stopped when the block is removed or no longer a cave illuminator.
 */
class ParticleManager(private val plugin: CaveIlluminator) {
    private val activeTasks = hashMapOf<Location, ScheduledTask>()

    /**
     * Starts a repeating task that spawns particles at the given block's location.
     * The task will continue to run until the block is no longer a cave illuminator or until stopParticles is called for the block's location.
     */
    fun startParticles(block: Block) {
        if(activeTasks.containsKey(block.location)) return

        activeTasks[block.location] = plugin.scheduler.runTaskTimer({
            if(!BlockManager.isCaveIlluminator(block)) {
                stopParticles(block.location)
                return@runTaskTimer
            }

            val particleLoc = block.location.clone().add(0.5, 0.5, 0.5)

            block.world.spawnParticle(
                Particle.FLAME,
                particleLoc,
                1,
                0.2, 0.2, 0.2,
                0.0
            )
        }, 0L, 5L)
    }

    /**
     * Stops the particle spawning task for the given location, if one is active. If no task is active for the location, this method does nothing.
     */
    fun stopParticles(location: Location) {
        val task = activeTasks.remove(location) ?: return
        task.cancel()
    }
}