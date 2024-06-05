package com.roboter5123.robogames.repository;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public interface SchedulerRepository {

    BukkitTask scheduleDelayedTask(BukkitRunnable task, Long ticksUntil);

    BukkitTask scheduleRepeatingTask(BukkitRunnable task, Long startDelay, Long ticksBetween);
}
