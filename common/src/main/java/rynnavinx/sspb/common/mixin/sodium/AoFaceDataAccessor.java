package rynnavinx.sspb.common.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(targets = "net.caffeinemc.mods.sodium.client.model.light.smooth.AoFaceData", remap = false)
public interface AoFaceDataAccessor {

	@Invoker(value = "getBlendedShade")
	float sspb$invokeGetBlendedShade(float[] w);

	@Invoker(value = "getBlendedSkyLight")
	float sspb$invokeGetBlendedSkyLight(float[] w);

	@Invoker(value = "getBlendedBlockLight")
	float sspb$invokeGetBlendedBlockLight(float[] w);

	@Invoker(value = "hasUnpackedLightData")
	boolean sspb$invokeHasUnpackedLightData();

	@Invoker(value = "unpackLightData")
	void sspb$invokeUnpackLightData();
}
