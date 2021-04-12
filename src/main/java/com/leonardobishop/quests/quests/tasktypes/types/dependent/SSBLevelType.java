package com.leonardobishop.quests.quests.tasktypes.types.dependent;

import com.bgsoftware.superiorskyblock.api.events.IslandWorthCalculatedEvent;
import com.leonardobishop.quests.api.QuestsAPI;
import com.leonardobishop.quests.player.QPlayer;
import com.leonardobishop.quests.player.questprogressfile.QuestProgress;
import com.leonardobishop.quests.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.quests.Quest;
import com.leonardobishop.quests.quests.Task;
import com.leonardobishop.quests.quests.tasktypes.ConfigValue;
import com.leonardobishop.quests.quests.tasktypes.TaskType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.ArrayList;
import java.util.List;

public final class SSBLevelType extends TaskType {
    private List<ConfigValue> creatorConfigValues = new ArrayList<>();

    public SSBLevelType() {
        super("ssb_island_level", "Falistos", "Reach a certain island level for SuperioSkyblock");
        this.creatorConfigValues.add(new ConfigValue("level", true, "Minimum island level needed."));
    }

    @Override
    public List<ConfigValue> getCreatorConfigValues() {
        return creatorConfigValues;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorthEvent(IslandWorthCalculatedEvent event) {
        QPlayer qPlayer = QuestsAPI.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
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
