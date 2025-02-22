package com.badbones69.crazyenchantments.api.multisupport.interfaces;

import com.badbones69.crazyenchantments.api.CrazyManager;
import java.util.Arrays;

/**
 * @Author Ome_R
 */
public enum ServerVersion {

    v1_8(18),
    v1_9(19),
    v1_10(110),
    v1_11(111),
    v1_12(112),
    v1_13(113),
    v1_14(114),
    v1_15(115),
    v1_16(116),
    v1_17(117),
    v1_18(118),
    v1_19(119),

    UNKNOWN(-1);

    private static final ServerVersion currentVersion;
    private static final String bukkitVersion;
    private static final boolean legacy;

    private static final CrazyManager ce = CrazyManager.getInstance();

    static {
        bukkitVersion = ce.getPlugin().getServer().getBukkitVersion().split("-")[0];
        String version = ce.getPlugin().getServer().getClass().getPackage().getName().split("\\.")[3];
        String[] sections = version.split("_");
        currentVersion = ServerVersion.getSafe(sections[0] + "_" + sections[1]);
        legacy = isLessThan(ServerVersion.v1_13);
    }

    private final int code;

    ServerVersion(int code) {
        this.code = code;
    }

    public static ServerVersion getSafe(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException error) {
            return UNKNOWN;
        }
    }

    public static boolean isAtLeast(ServerVersion serverVersion) {
        return isValidVersion(serverVersion) && currentVersion.code >= serverVersion.code;
    }

    public static boolean isLessThan(ServerVersion serverVersion) {
        return isValidVersion(serverVersion) && currentVersion.code < serverVersion.code;
    }

    public static boolean isEquals(ServerVersion serverVersion) {
        return isValidVersion(serverVersion) && currentVersion.code == serverVersion.code;
    }

    public static boolean isLegacy() {
        return legacy;
    }

    public static String getBukkitVersion() {
        return bukkitVersion;
    }

    public static ServerVersion[] getByOrder() {
        ServerVersion[] versions = Arrays.copyOfRange(values(), 0, currentVersion.ordinal() + 1);

        for (int i = 0; i < versions.length / 2; i++) {
            ServerVersion temp = versions[i];
            versions[i] = versions[versions.length - i - 1];
            versions[versions.length - i - 1] = temp;
        }

        return versions;
    }

    private static boolean isValidVersion(ServerVersion compareVersion) {
        return currentVersion != UNKNOWN && compareVersion != UNKNOWN;
    }
}