package com.matthewperiut.chisel.item;
import com.matthewperiut.chisel.Chisel;
import com.matthewperiut.chisel.block.ChiselGroupLookup;
import com.matthewperiut.chisel.gui.ChiselScreenHandler;
import com.matthewperiut.chisel.inventory.InventoryNbtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.TagGroup;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

import static com.matthewperiut.chisel.Chisel.ITEM_CHISEL;

public class ChiselItem extends BundleItem implements NamedScreenHandlerFactory
{
    public ChiselItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack)
    {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {

    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        return false;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference)
    {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if (!world.isClient)
        {
            if (user.getItemsHand().iterator().next().isOf(ITEM_CHISEL))
                user.openHandledScreen(this);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
    {
        NbtCompound nbt = player.getItemsHand().iterator().next().getOrCreateNbt();
        Inventory chiselInv = InventoryNbtUtil.createInventory(nbt);
        return new ChiselScreenHandler(syncId, inv, chiselInv, nbt);
        //return new ChiselDescription(syncId, inv, ScreenHandlerContext.create(player.world, player.getBlockPos()));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        return ActionResult.PASS;
    }

    public static void chiselSound(World world, Block block, BlockPos pos)
    {
        if (!world.isClient)
        {
            world.playSound(
                    null, // Player - if non-null, will play sound for every nearby player *except* the specified player
                    pos, // The position of where the sound will come from
                    block.getSoundGroup(block.getDefaultState()).getPlaceSound(), // The sound that will play
                    SoundCategory.BLOCKS, // This determines which of the volume sliders affect this sound
                    1f, //Volume multiplier, 1 is normal, 0.5 is half volume, etc
                    1f // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
            );
        }
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        ItemStack inHand = miner.getItemsHand().iterator().next();
        NbtCompound nbtCompound = inHand.getOrCreateNbt();

        if (!world.isClient)
        {
            if (!nbtCompound.contains("Items")) {
                return false;
            }
            if (world.getTime() - nbtCompound.getLong("time") < 5)
            {
                return false;
            }

            NbtList nbtList = nbtCompound.getList("Items", 10);

            NbtCompound nbtCompound2 = nbtList.getCompound(0);
            ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);

            String inInventoryModID;
            Identifier blockId = Registry.BLOCK.getId(state.getBlock());
            Identifier inInventory = Registry.ITEM.getId(itemStack.getItem());

            if (inInventory == Registry.ITEM.getId(Items.AIR))
            {
                TagGroup<Item> itemTags = ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.ITEM.getKey());
                List<Item> items = ChiselGroupLookup.getBlocksInGroup(state.getBlock().asItem(), itemTags);
                if(items.size() > 0)
                {
                    inInventory = Registry.ITEM.getId(items.get(world.random.nextInt(items.size())));
                    world.setBlockState(pos, Registry.BLOCK.get(inInventory).getDefaultState());
                    nbtCompound.putLong("time",world.getTime());
                    chiselSound(world,state.getBlock(),pos);
                }
            }

            String[] compare = new String[2];

            String[] temp;
            temp = inInventory.getPath().split("/");
            compare[0] = temp[temp.length-1];
            temp = blockId.getPath().split("/");
            compare[1] = temp[temp.length-1];

            if(compare[0].contains(compare[1]) || compare[1].contains(compare[0]))
            {
                world.setBlockState(pos, Registry.BLOCK.get(inInventory).getDefaultState());
                nbtCompound.putLong("time",world.getTime());
                chiselSound(world,state.getBlock(),pos);
            }
        }
        return false;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        Item item = state.getBlock().asItem();
        if (!(item instanceof BlockItem)) {
            Chisel.LOGGER.info("How is " + item.getName().getString() +" not an blockItem?");
            return 1.0f;
        }
        TagGroup<Item> itemTags = ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.ITEM_KEY);
        List<Item> chiselBlocks = ChiselGroupLookup.getBlocksInGroup(item, itemTags);
        if (chiselBlocks.isEmpty()) {
            return 1.0f;
        }
        return 500.0f;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("gui.chisel.chisel");
    }

}
