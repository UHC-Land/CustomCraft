package customcraft.recipe;

import cn.nukkit.inventory.ShapelessRecipe;
import cn.nukkit.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @author lt_name
 */
public class CustomShapelessRecipe extends ShapelessRecipe implements ICustomRecipe {

    @Setter
    @Getter
    private int maxCraftCount = -1;

    public CustomShapelessRecipe(Item result, Collection<Item> ingredients) {
        super(result, ingredients);
    }

    public CustomShapelessRecipe(String recipeId, int priority, Item result, Collection<Item> ingredients) {
        super(recipeId, priority, result, ingredients);
    }

    public CustomShapelessRecipe(String recipeId, int priority, Item result, Collection<Item> ingredients, Integer networkId) {
        super(recipeId, priority, result, ingredients, networkId);
    }

}
