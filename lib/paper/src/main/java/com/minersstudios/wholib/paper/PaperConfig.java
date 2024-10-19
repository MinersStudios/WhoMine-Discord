package com.minersstudios.wholib.paper;

import com.minersstudios.wholib.module.components.Configuration;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public interface PaperConfig extends Configuration<WhoMine> {

    boolean reload();

    @NotNull YamlConfiguration getYaml();

    @NotNull Logger getLogger();

    @Nullable String getDateFormat();

    boolean isChristmas();

    boolean isHalloween();

    @UnknownNullability String getLanguageDefaultCode();

    @UnknownNullability DateTimeFormatter getDateFormatter();

    @UnknownNullability Locale getDefaultLocale();

    @UnknownNullability List<Locale> getLocales();

    long getDosimeterCheckRate();

    @UnknownNullability String getWoodSoundPlace();

    @UnknownNullability String getWoodSoundBreak();

    @UnknownNullability String getWoodSoundStep();

    @UnknownNullability String getWoodSoundHit();

    boolean isDeveloperMode();

    void setDeveloperMode(final boolean developerMode);

    long getAnomalyCheckRate();

    void setAnomalyCheckRate(final long rate);

    long getAnomalyParticlesCheckRate();

    void setAnomalyParticlesCheckRate(final long rate);

    long getDiscordServerId();

    void setDiscordServerId(final long id);

    long getMemberRoleId();

    void setMemberRoleId(final long id);

    long getDiscordGlobalChannelId();

    void setDiscordGlobalChannelId(final long id);

    long getDiscordLocalChannelId();

    void setDiscordLocalChannelId(final long id);

    double getLocalChatRadius();

    void setLocalChatRadius(final double radius);

    @Nullable String getMineSkinApiKey();

    void setMineSkinApiKey(final @Nullable String apiKey);

    @NotNull Location getSpawnLocation();

    void setSpawnLocation(final @NotNull Location location);
}
