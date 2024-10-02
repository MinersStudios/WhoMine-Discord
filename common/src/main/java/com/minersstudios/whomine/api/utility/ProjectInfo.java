package com.minersstudios.whomine.api.utility;

import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")
public final class ProjectInfo {
    //<editor-fold desc="Constants" defaultstate="collapsed">

    public static final String NAME =        "WhoMine";
    public static final String VERSION =     "1.0.0";
    public static final String DESCRIPTION = "A Minecraft plugin for WhoMine";
    public static final String AUTHOR =      "MinersStudios";
    public static final String WEBSITE =     "https://whomine.net";

    //</editor-fold>

    @Contract(" -> fail")
    private ProjectInfo() throws AssertionError {
        throw new AssertionError("Utility class");
    }
}
