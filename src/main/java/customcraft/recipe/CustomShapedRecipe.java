package customcraft.recipe;

import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author lt_name
 */
public class CustomShapedRecipe extends ShapedRecipe implements ICustomRecipe {

    @Setter
    @Getter
    private int maxCraftCount = -1;

    public CustomShapedRecipe(Item primaryResult, String[] shape, Map<Character, Item> ingredients, List<Item> extraResults) {
        super(primaryResult, shape, ingredients, extraResults);
    }

    public CustomShapedRecipe(String recipeId, int priority, Item primaryResult, String[] shape, Map<Character, Item> ingredients, List<Item> extraResults) {
        super(recipeId, priority, primaryResult, shape, ingredients, extraResults);
    }

    public CustomShapedRecipe(String recipeId, int priority, Item primaryResult, String[] shape, Map<Character, Item> ingredients, List<Item> extraResults, Integer networkId) {
        super(recipeId, priority, primaryResult, shape, ingredients, extraResults, networkId);
    }

}
