package com.smallaswater.customcraft;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.CraftItemEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.utils.TextFormat;
import com.smallaswater.customcraft.event.CraftCustomItemEvent;
import com.smallaswater.customcraft.recipe.ICustomRecipe;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lt_name
 */
public class EventListener implements Listener {

    private final CustomCraft customCraft;

    public EventListener(CustomCraft customCraft) {
        this.customCraft = customCraft;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.customCraft.getPlayerCraftCountMap().remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event) {
        Player player = event.getPlayer();
        Recipe recipe = event.getRecipe();
        if (recipe instanceof ICustomRecipe) {
            CraftCustomItemEvent ev = new CraftCustomItemEvent(event.getTransaction());
            Server.getInstance().getPluginManager().callEvent(ev);
            if (!ev.isCancelled()) {
                ICustomRecipe customRecipe = (ICustomRecipe) recipe;
                if (customRecipe.getMaxCraftCount() <= 0) {
                    return;
                }
                ConcurrentHashMap<Recipe, Integer> map = this.customCraft.getPlayerCraftCountMap().getOrDefault(player, new ConcurrentHashMap<>());
                int newCount = map.getOrDefault(recipe, 0) + 1;
                if (newCount > customRecipe.getMaxCraftCount()) {
                    player.sendMessage(TextFormat.RED + "超过最大合成次数限制，您无法再次合成此物品");
                    event.setCancelled(true);
                } else {
                    map.put(recipe, newCount);
                    this.customCraft.getPlayerCraftCountMap().put(player, map);
                }
            }
        }
    }

}
