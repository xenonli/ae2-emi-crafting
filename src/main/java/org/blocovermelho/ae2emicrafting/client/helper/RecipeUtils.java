package org.blocovermelho.ae2emicrafting.client.helper;

import appeng.integration.modules.jeirei.EncodingHelper;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.collection.DefaultedList;
import org.blocovermelho.ae2emicrafting.client.Ae2EmiMod;
import org.blocovermelho.ae2emicrafting.client.Ae2EmiPlugin;

import java.util.HashMap;
import java.util.Map;

import static org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_HEIGHT;
import static org.blocovermelho.ae2emicrafting.client.handler.generic.Ae2BaseRecipeHandler.CRAFTING_GRID_WIDTH;

public class RecipeUtils {
    public static boolean isCraftingRecipe(Recipe<?> recipe, EmiRecipe emiRecipe) {
        return EncodingHelper.isSupportedCraftingRecipe(recipe)
                || emiRecipe.getCategory().equals(VanillaEmiRecipeCategories.CRAFTING);
    }

    public static boolean  fitsIn3x3Grid(Recipe<?> recipe) {
        if (recipe != null) {
            return recipe.fits(CRAFTING_GRID_WIDTH, CRAFTING_GRID_HEIGHT);
        } else {
            return true;
        }
    }
    public static boolean canFit(ShapedRecipe recipe, int width, int height) {
        DefaultedList<Ingredient> input = recipe.getIngredients();
        if (input.size() > 9) {
            return false;
        }
        for (int i = 0; i < input.size(); i++) {
            int x = i % 3;
            int y = i / 3;
            if (!input.get(i).isEmpty() && (x >= width || y >= height)) {
                return false;
            }
        }
        return true;
    }
    public static Map<Integer, Ingredient> getGuiSlotToIngredientMap(Recipe<?> recipe) {
        DefaultedList<Ingredient> ingredients = recipe.getIngredients();
        int width;
        int sOff = 0;
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            width = shapedRecipe.getWidth();
            if(canFit(shapedRecipe, 1, 3)) {
                sOff += 1;
            }
            if(canFit(shapedRecipe, 3, 1)) {
                sOff += width;
            }
        } else {
            width = 3;
        }

        HashMap<Integer, Ingredient> result = new HashMap(ingredients.size());

        for(int i = 0; i < ingredients.size(); ++i) {
            int guiSlot = i / width * 3 + i % width + sOff;
            Ingredient ingredient = (Ingredient)ingredients.get(i);
            if (!ingredient.isEmpty()) {
                result.put(guiSlot, ingredient);
            }
        }

        return result;
    }
}
