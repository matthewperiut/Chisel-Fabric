package com.matthewperiut.chisel.fabric;

import com.matthewperiut.chisel.gui.ChiselScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import static com.matthewperiut.chisel.Chisel.CHISEL_SCREEN_HANDLER;

public class ChiselClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ClientRegister.Register();
        HandledScreens.register(CHISEL_SCREEN_HANDLER.get(), ChiselScreen::new);
    }
}
