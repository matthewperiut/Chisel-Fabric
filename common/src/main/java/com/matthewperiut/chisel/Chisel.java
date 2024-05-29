package com.matthewperiut.chisel;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.matthewperiut.chisel.block.BlockRegister;
import com.matthewperiut.chisel.gui.ChiselScreenHandler;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Chisel {
    public static final String MOD_ID = "chisel";
    public static final Logger LOGGER = LogManager.getFormatterLogger("Chisel");

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
    public static final Registrar<Block> BLOCKS = MANAGER.get().get(RegistryKeys.BLOCK);
    public static final Registrar<Item> ITEMS = MANAGER.get().get(RegistryKeys.ITEM);

    public static final Identifier CHISEL_SOUND_ID = new Identifier(MOD_ID, "chisel_sound");
    public static SoundEvent CHISEL_SOUND_EVENT = SoundEvent.of(CHISEL_SOUND_ID);

    public static final Registrar<ScreenHandlerType<?>> SCREEN_HANDLERS = MANAGER.get().get(RegistryKeys.SCREEN_HANDLER);
    public static final RegistrySupplier<ScreenHandlerType<ChiselScreenHandler>> CHISEL_SCREEN_HANDLER = SCREEN_HANDLERS.register(
            new Identifier(MOD_ID, "chisel_screen_handler"),
            () -> new ScreenHandlerType<>(ChiselScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
    );

    public static ArrayList<String> transparentBlocks = new ArrayList<>();
    public static ArrayList<String> translucentBlocks = new ArrayList<>();

    public static RegistrySupplier<Item> chiselSupplier;

    public static void init()
    {
        BlockRegister.Register();
    }
}
