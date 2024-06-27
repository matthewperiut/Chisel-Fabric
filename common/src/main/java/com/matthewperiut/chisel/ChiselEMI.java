package com.matthewperiut.chisel;

import com.matthewperiut.chisel.block.ChiselGroupLookup;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EmiEntrypoint
public class ChiselEMI implements EmiPlugin {
    private static final ChiselCategory CHISEL_CATEGORY = new ChiselCategory();

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CHISEL_CATEGORY);
        registry.addWorkstation(CHISEL_CATEGORY, EmiStack.of(Chisel.chiselSupplier.get()));

        Iterator<String> chiselGroupNames = ChiselGroupLookup.getGroupNameIterator();
        while (chiselGroupNames.hasNext()) {
            registry.addRecipe(new ChiselEmiRecipe(chiselGroupNames.next()));
        }
    }

    public static class ChiselCategory extends EmiRecipeCategory {
        public ChiselCategory() {
            super(new Identifier(Chisel.MOD_ID, "chisel_recipes_category"), EmiStack.of(Chisel.chiselSupplier.get()));
        }

        @Override
        public Text getName() {
            return Text.of(I18n.translate("rei.chisel.category"));
        }
    }

    public static class ChiselEmiRecipe implements EmiRecipe {
        private static final Identifier TEXTURE = new Identifier(Chisel.MOD_ID, "textures/rei_recipes.png");

        private final Identifier id;
        private final List<EmiIngredient> input;
        private final List<EmiStack> output;

        public ChiselEmiRecipe(String chiselGroup) {
            this.id = new Identifier(Chisel.MOD_ID, chiselGroup);
            this.input = new ArrayList<>();
            this.output = new ArrayList<>();
            for (Item item : ChiselGroupLookup.getBlocksInGroup(chiselGroup)) {
                this.input.add(EmiStack.of(item));
                this.output.add(EmiStack.of(item));
            }
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return CHISEL_CATEGORY;
        }

        @Override
        public @Nullable Identifier getId() {
            return id;
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return input;
        }

        @Override
        public List<EmiStack> getOutputs() {
            return output;
        }

        @Override
        public int getDisplayHeight() {
            return 200;
        }

        @Override
        public int getDisplayWidth() {
            return 150;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            int startX = 20;
            int startY = 4;

            int j = output.size();
            int rows = (int) Math.ceil((double) j / 6.0d);

            widgets.addTexture(TEXTURE, 21, 15, 108, 236 - (18 * (10 - rows) + 11), 0, 10);

            widgets.addSlot(input.get(0), startX + 46, startY + 17).drawBack(false);

            for (int x = 0; x < 6; x++) {
                for (int y = 0; y < rows; y++) {
                    if (6 * y + x >= j) {
                        break;
                    }

                    widgets.addSlot(output.get(6 * y + x), startX + 1 + 18 * x, startY + 56 + 18 * y).drawBack(false).recipeContext(this);
                }
            }
        }
    }
}
