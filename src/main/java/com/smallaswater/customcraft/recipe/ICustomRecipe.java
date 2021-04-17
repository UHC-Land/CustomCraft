package com.smallaswater.customcraft.recipe;

import cn.nukkit.inventory.CraftingRecipe;

/**
 * @author lt_name
 */
public interface ICustomRecipe extends CraftingRecipe {

    void setMaxCraftCount(int count);

    int getMaxCraftCount();

}
