package com.leonardobishop.quests.bukkit;

import com.leonardobishop.quests.common.logger.QuestsLogger;

public class BukkitQuestsLogger implements QuestsLogger {

    private final BukkitQuestsPlugin plugin;
    private LoggingLevel serverLoggingLevel;

    public BukkitQuestsLogger(BukkitQuestsPlugin plugin) {
        this.plugin = plugin;
        serverLoggingLevel = LoggingLevel.INFO;
    }

    @Override
    public LoggingLevel getServerLoggingLevel() {
        return serverLoggingLevel;
    }

    @Override
    public void setServerLoggingLevel(LoggingLevel serverLoggingLevel) {
        this.serverLoggingLevel = serverLoggingLevel;
    }

    @Override
    public void log(String str, LoggingLevel level) {
        if (serverLoggingLevel.getNumericVerbosity() < level.getNumericVerbosity()) {
            return;
        }
        switch (level) {
            case DEBUG:
                plugin.getLogger().info("Debug: " + str);
                break;
            case INFO:
                plugin.getLogger().info(str);
                break;
            case ERROR:
                plugin.getLogger().severe(str);
                break;
            case WARNING:
                plugin.getLogger().warning(str);
                break;
        }
    }

    @Override
    public void debug(String str) {
        log(str, LoggingLevel.DEBUG);
    }

    @Override
    public void info(String str) {
        log(str, LoggingLevel.INFO);
    }

    @Override
    public void warning(String str) {
        log(str, LoggingLevel.WARNING);
    }

    @Override
    public void severe(String str) {
        log(str, LoggingLevel.ERROR);
    }

}
