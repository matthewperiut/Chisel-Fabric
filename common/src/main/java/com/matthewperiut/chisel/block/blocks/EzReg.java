package com.matthewperiut.chisel.block.blocks;

import com.matthewperiut.chisel.Chisel;
import com.matthewperiut.chisel.block.ChiselGroupLookup;
import com.matthewperiut.chisel.item.ChiselItem;
import com.matthewperiut.chisel.registry.ItemGroupRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import static com.matthewperiut.chisel.Chisel.BLOCKS;
import static com.matthewperiut.chisel.Chisel.ITEMS;

public class EzReg
{
    public static void Reg(String group, String name)
    {
        boolean redstone = false;
        boolean pillar = false;
        boolean purpur = false;
        boolean quartz = false;

        if (name.toLowerCase().contains("redstone"))
            redstone = true;

        if (name.toLowerCase().contains("pillar") || name.toLowerCase().contains("twist"))
            pillar = true;

        if (name.toLowerCase().contains("purpur"))
            purpur = true;

        if (name.toLowerCase().contains("quartz"))
            quartz = true;

        if (group.equals("glass")) {
            Chisel.transparentBlocks.add(name);
        }
        if (group.equals("ice")) {
            Chisel.translucentBlocks.add(name);
        }

        String[] individual = name.split("/", 2);
        Identifier baseBlockIdentifier = new Identifier("minecraft",individual[1]);
        ChiselGroupLookup.addItemToGroup(individual[1], baseBlockIdentifier);
        Block baseBlock = purpur ? Blocks.PURPUR_BLOCK : (quartz ? Blocks.QUARTZ_BLOCK : Registries.BLOCK.get(baseBlockIdentifier));
        RegistrySupplier<Block> blockSupplier;

        if (redstone && pillar)
            blockSupplier = BLOCKS.register(new Identifier("chisel", name), () -> new RedstonePillarBlock(AbstractBlock.Settings.copy(baseBlock)));
        else if (redstone)
            blockSupplier = BLOCKS.register(new Identifier("chisel", name), () -> new RedstoneBlock(AbstractBlock.Settings.copy(baseBlock)));
        else if (pillar)
            blockSupplier = BLOCKS.register(new Identifier("chisel", name), () -> new PillarBlock(AbstractBlock.Settings.copy(baseBlock)));
        else
            blockSupplier = BLOCKS.register(new Identifier("chisel", name), () -> new Block(AbstractBlock.Settings.copy(baseBlock)));

        RegistrySupplier<Item> itemSupplier = ITEMS.register(new Identifier("chisel", name), () -> new BlockItem(blockSupplier.get(), new Item.Settings().arch$tab(ItemGroupRegistry.CLAY_GROUP)));

        ChiselGroupLookup.addItemToGroup(group, new Identifier("chisel", name));
    }
}
