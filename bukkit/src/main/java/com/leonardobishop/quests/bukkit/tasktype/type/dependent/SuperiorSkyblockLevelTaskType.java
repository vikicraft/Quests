package com.leonardobishop.quests.bukkit.tasktype.type.dependent;

import com.bgsoftware.superiorskyblock.api.events.IslandWorthCalculatedEvent;
import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.tasktype.BukkitTaskType;
import com.leonardobishop.quests.bukkit.util.TaskUtils;
import com.leonardobishop.quests.common.config.ConfigProblem;
import com.leonardobishop.quests.common.player.QPlayer;
import com.leonardobishop.quests.common.player.questprogressfile.QuestProgress;
import com.leonardobishop.quests.common.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.common.quest.Quest;
import com.leonardobishop.quests.common.quest.Task;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class SuperiorSkyblockLevelTaskType extends BukkitTaskType {

    private final BukkitQuestsPlugin plugin;

    public SuperiorSkyblockLevelTaskType(BukkitQuestsPlugin plugin) {
        super("ssb_island_level", TaskUtils.TASK_ATTRIBUTION_STRING, "Reach a certain island level for SuperioSkyblock.");
        this.plugin = plugin;
    }

    @Override
    public @NotNull List<ConfigProblem> validateConfig(@NotNull String root, @NotNull HashMap<String, Object> config) {
        ArrayList<ConfigProblem> problems = new ArrayList<>();
        if (TaskUtils.configValidateExists(root + ".level", config.get("level"), problems, "level", super.getType()))
            TaskUtils.configValidateInt(root + ".level", config.get("level"), problems, false, false, "level");
        return problems;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorthEvent(IslandWorthCalculatedEvent event) {
        if (event.getPlayer() == null)
            return;

        QPlayer qPlayer = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        if (qPlayer == null)
            return;

        for (Quest quest : super.getRegisteredQuests()) {
            if (qPlayer.hasStartedQuest(quest)) {
                QuestProgress questProgress = qPlayer.getQuestProgressFile().getQuestProgress(quest);

                for (Task task : quest.getTasksOfType(super.getType())) {
                    TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());

                    if (taskProgress.isCompleted())
                        continue;

                    int islandLevelNeeded = (int) task.getConfigValue("level");

                    taskProgress.setProgress((int) event.getLevel().doubleValue());

                    if (((int) taskProgress.getProgress()) >= islandLevelNeeded)
                        taskProgress.setCompleted(true);
                }
            }
        }
    }
}