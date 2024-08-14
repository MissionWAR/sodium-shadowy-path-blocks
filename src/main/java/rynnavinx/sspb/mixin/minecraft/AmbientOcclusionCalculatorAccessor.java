package rynnavinx.sspb.mixin.minecraft;

import net.minecraft.client.render.block.BlockModelRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(BlockModelRenderer.AmbientOcclusionCalculator.class)
public interface AmbientOcclusionCalculatorAccessor {

    @Accessor("brightness")
    float[] sspb$getBrightness();

    @Accessor("light")
    int[] sspb$getLight();
}
