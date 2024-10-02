package com.minersstudios.whomine.custom.anomaly.task;

import com.minersstudios.whomine.PaperConfig;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.anomaly.Anomaly;
import com.minersstudios.whomine.custom.anomaly.AnomalyAction;
import com.minersstudios.whomine.custom.anomaly.AnomalyBoundingBox;
import com.minersstudios.whomine.custom.anomaly.action.SpawnParticlesAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

/**
 * Particle anomaly task. This task is used to check if the player is in the
 * anomaly zone. When player is in the anomaly zone, the action will be
 * performed and particles will be spawned. Otherwise, the action will be
 * removed.
 * <br>
 * The task is registered in {@link PaperConfig#reload()} with
 * {@link PaperConfig#getAnomalyParticlesCheckRate()}.
 *
 * @see SpawnParticlesAction
 * @see AnomalyBoundingBox
 */
public final class AnomalyParticleTask implements Runnable {
    private final WhoMine plugin;
    private final Map<Player, Map<AnomalyAction, Long>> anomalyActionMap;
    private final Collection<Anomaly> anomalies;

    public AnomalyParticleTask(final @NotNull WhoMine plugin) {
        this.plugin = plugin;
        this.anomalyActionMap = plugin.getCache().getPlayerAnomalyActionMap();
        this.anomalies = plugin.getCache().getAnomalies().values();
    }

    @Override
    public void run() {
        if (
                this.anomalies.isEmpty()
                || this.anomalyActionMap.isEmpty()
        ) {
            return;
        }

        this.plugin.runTaskAsync(() -> {
            for (final var entry : this.anomalyActionMap.entrySet()) {
                final Player player = entry.getKey();
                final var actionMap = entry.getValue();

                for (final var action : actionMap.keySet()) {
                    if (!(action instanceof SpawnParticlesAction)) {
                        continue;
                    }

                    for (final var anomaly : this.anomalies) {
                        final double radiusInside = anomaly.getBoundingBox().getRadiusInside(player);

                        if (radiusInside == -1.0d) {
                            continue;
                        }

                        if (anomaly.getAnomalyActionMap().get(radiusInside).contains(action)) {
                            action.doAction(player, null);
                        } else {
                            action.removeAction(player);
                        }
                    }
                }
            }
        });
    }
}
