package rynnavinx.sspb.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(targets = "net.caffeinemc.mods.sodium.client.model.light.smooth.AoFaceData")
public interface AoFaceDataAccessor {

	@Invoker(value = "getBlendedShade", remap = false)
	float sspb$invokeGetBlendedShade(float[] w);

	@Invoker(value = "getBlendedSkyLight", remap = false)
	float sspb$invokeGetBlendedSkyLight(float[] w);

	@Invoker(value = "getBlendedBlockLight", remap = false)
	float sspb$invokeGetBlendedBlockLight(float[] w);

	@Invoker(value = "hasUnpackedLightData", remap = false)
	boolean sspb$invokeHasUnpackedLightData();

	@Invoker(value = "unpackLightData", remap = false)
	void sspb$invokeUnpackLightData();
}
