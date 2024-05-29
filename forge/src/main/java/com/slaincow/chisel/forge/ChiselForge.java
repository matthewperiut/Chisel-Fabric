package com.slaincow.chisel.forge;

import com.matthewperiut.chisel.Chisel;
import com.matthewperiut.chisel.gui.ChiselScreen;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.matthewperiut.chisel.Chisel.*;

@Mod(MOD_ID)
public class ChiselForge {

    public ChiselForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EventBuses.registerModEventBus(MOD_ID, modEventBus);
        init();

        // Register the client only setup method (client-side things)
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(
                // Assume RegistryObject<MenuType<MyMenu>> MY_MENU
                // Assume MyContainerScreen<MyMenu> which takes in three parameters
                () -> HandledScreens.register(CHISEL_SCREEN_HANDLER.get(), ChiselScreen::new)
        );
        for (String name : Chisel.translucentBlocks) {
            Block block = Registries.BLOCK.get(new Identifier(Chisel.MOD_ID, name));
            RenderLayers.setRenderLayer(block, RenderLayer.getTranslucent());
        }
        for (String name : Chisel.transparentBlocks) {
            Block block = Registries.BLOCK.get(new Identifier(Chisel.MOD_ID, name));
            RenderLayers.setRenderLayer(block, RenderLayer.getCutout());
        }

    }

    @SubscribeEvent
    public void onInterModProcessEvent(InterModProcessEvent event) {
        // Handle inter-mod communications here if necessary
    }
}