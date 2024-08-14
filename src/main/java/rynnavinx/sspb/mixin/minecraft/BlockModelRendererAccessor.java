package rynnavinx.sspb.mixin.minecraft;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.BitSet;


@Mixin(BlockModelRenderer.class)
public interface BlockModelRendererAccessor {

    @Invoker("getQuadDimensions")
    void sspb$invokeGetQuadDimensions(BlockRenderView world, BlockState state, BlockPos pos, int[] vertexData, Direction face, float[] box, BitSet flags);
}
