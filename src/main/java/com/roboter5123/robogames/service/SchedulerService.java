package com.roboter5123.robogames.service;

import com.roboter5123.robogames.RoboGames;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class SchedulerService {
    private final RoboGames roboGames;

    public SchedulerService(RoboGames roboGames) {
        this.roboGames = roboGames;
    }

    public @NotNull BukkitTask scheduleDelayedTask(BukkitRunnable task, Long ticksUntil) {
        return task.runTaskLater(this.roboGames, ticksUntil);
    }

    public @NotNull BukkitTask scheduleRepeatingTask(BukkitRunnable task, Long startDelay, Long ticksBetween) {
        return task.runTaskTimer(this.roboGames, startDelay, ticksBetween);
    }
}
