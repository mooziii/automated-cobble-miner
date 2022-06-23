package me.obsilabor.automatedcobbleminer;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.glfw.GLFW;

public class AutomatedCobbleMiner implements ClientModInitializer {
    private BlockPos blockPos = null;
    private Direction direction = null;

    @Override
    public void onInitializeClient() {
        Minecraft minecraft = Minecraft.getInstance();
        KeyMapping hotkey  = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.toggleBreaking",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "Automated Cobble Miner"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(hotkey.isDown()) {
                if(blockPos == null) {
                    if (minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
                        BlockHitResult blockHitResult = (BlockHitResult) minecraft.hitResult;
                        blockPos = blockHitResult.getBlockPos();
                        direction = blockHitResult.getDirection();
                        minecraft.getToasts().addToast(new SystemToast(
                                SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                                Component.nullToEmpty("Automated Cobble Miner"),
                                Component.translatable("toast.toggledOn")
                        ));
                    }
                } else {
                    blockPos = null;
                    minecraft.getToasts().addToast(new SystemToast(
                            SystemToast.SystemToastIds.PERIODIC_NOTIFICATION,
                            Component.nullToEmpty("Automated Cobble Miner"),
                            Component.translatable("toast.toggledOff")
                    ));
                }
            }
            if(blockPos == null) {
                return;
            }
            if(minecraft == null || minecraft.player == null || minecraft.gameMode == null || minecraft.level == null) {
                return;
            }
            if(minecraft.level.getBlockState(blockPos).isAir()) {
                return;
            }
            if(minecraft.gameMode.continueDestroyBlock(blockPos, direction)) {
                minecraft.particleEngine.crack(blockPos, direction);
                minecraft.player.swing(InteractionHand.MAIN_HAND);
            }
        });
    }
}
