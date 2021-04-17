package com.smallaswater.customcraft.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.inventory.transaction.CraftingTransaction;
import cn.nukkit.item.Item;

/**
 * @author lt_name
 */
public class CraftCustomItemEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private Item[] input;
    private final Recipe recipe;
    private final Player player;
    private CraftingTransaction transaction;

    public CraftCustomItemEvent(CraftingTransaction transaction) {
        this.transaction = transaction;
        this.player = transaction.getSource();
        this.input = transaction.getInputList().toArray(new Item[0]);
        this.recipe = transaction.getRecipe();
    }

    public CraftCustomItemEvent(Player player, Item[] input, Recipe recipe) {
        this.player = player;
        this.input = input;
        this.recipe = recipe;
    }

    public CraftingTransaction getTransaction() {
        return this.transaction;
    }

    public Item[] getInput() {
        return this.input;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public Player getPlayer() {
        return this.player;
    }

}
