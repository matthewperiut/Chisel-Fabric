package com.matthewperiut.chisel.registry;

import com.matthewperiut.chisel.Chisel;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

import static net.minecraft.registry.RegistryKeys.ITEM_GROUP;

public class ItemGroupRegistry {
    public static final DeferredRegister<ItemGroup> ITEM_GROUPS = DeferredRegister.create(Chisel.MOD_ID, ITEM_GROUP);
    public static final RegistrySupplier<ItemGroup> CLAY_GROUP = ITEM_GROUPS.register("chisel", () -> CreativeTabRegistry.create(Text.translatable("itemGroup." + Chisel.MOD_ID + ".chisel"), () -> new ItemStack(Objects.requireNonNull(Chisel.ITEMS.get(Identifier.of(Chisel.MOD_ID, "chisel"))))));

    public static void Register() {
        ITEM_GROUPS.register();
    }
}
