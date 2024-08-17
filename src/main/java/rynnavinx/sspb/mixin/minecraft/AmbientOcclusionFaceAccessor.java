package rynnavinx.sspb.mixin.minecraft;

import net.minecraft.client.renderer.block.ModelBlockRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(ModelBlockRenderer.AmbientOcclusionFace.class)
public interface AmbientOcclusionFaceAccessor {

    @Accessor("brightness")
    float[] sspb$getBrightness();

    @Accessor("lightmap")
    int[] sspb$getLightmap();
}
