/*
 * This file contains modified parts of AoCalculator.java from
 * "Fabric Renderer - Indigo" from "Fabric API".
 *
 * Therefore, it incorporates work under the following license:
 *
	 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *     http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
 */

package rynnavinx.sspb.common.mixin.sodium;

import net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess;
import net.caffeinemc.mods.sodium.client.model.light.smooth.SmoothLightPipeline;
import net.caffeinemc.mods.sodium.client.model.light.data.QuadLightData;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.EncodingFormat;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.QuadViewImpl;

import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rynnavinx.sspb.common.client.SSPBClientMod;
import rynnavinx.sspb.common.client.render.frapi.aocalc.VanillaAoHelper;
import rynnavinx.sspb.common.mixin.minecraft.AmbientOcclusionFaceAccessor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.BitSet;


@Mixin(value = SmoothLightPipeline.class, remap = false)
public abstract class MixinSmoothLightPipeline {

	@Final @Shadow
	private LightDataAccess lightCache;

	@Shadow
	private static int getLightMapCoord(float sl, float bl) {return 0;}

	@Unique
	private static final MethodHandle sspb$getCachedFaceDataHandle;


	static {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			Class<?> aoFaceDataClass = Class.forName("net.caffeinemc.mods.sodium.client.model.light.smooth.AoFaceData");

			sspb$getCachedFaceDataHandle = lookup.findVirtual(SmoothLightPipeline.class, "getCachedFaceData", MethodType.methodType(aoFaceDataClass, BlockPos.class, Direction.class, boolean.class, boolean.class));
		} catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}


	@Unique @SuppressWarnings({"JavaLangInvokeHandleSignature", "DataFlowIssue"})
	private AoFaceDataAccessor sspb$getCachedFaceData(BlockPos pos, Direction dir, boolean offset, boolean shade){
		try {
			return (AoFaceDataAccessor) sspb$getCachedFaceDataHandle.invoke((SmoothLightPipeline) (Object) this, pos, dir, offset, shade);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Unique
	private void sspb$applyInsetPartialFaceVertex(BlockPos pos, Direction dir, float n1d, float n2d, float[] w, int i, QuadLightData out, boolean shade, boolean isParallel){
		AoFaceDataAccessor n1 = sspb$getCachedFaceData(pos, dir, false, shade);

		if(!n1.sspb$invokeHasUnpackedLightData()){
			n1.sspb$invokeUnpackLightData();
		}

		AoFaceDataAccessor n2 = sspb$getCachedFaceData(pos, dir, true, shade);

		if(!n2.sspb$invokeHasUnpackedLightData()){
			n2.sspb$invokeUnpackLightData();
		}

		float ao1 = n1.sspb$invokeGetBlendedShade(w);
		float sl1 = n1.sspb$invokeGetBlendedSkyLight(w);
		float bl1 = n1.sspb$invokeGetBlendedBlockLight(w);

		float ao2 = n2.sspb$invokeGetBlendedShade(w);
		float sl2 = n2.sspb$invokeGetBlendedSkyLight(w);
		float bl2 = n2.sspb$invokeGetBlendedBlockLight(w);

		float ao;
		float sl;
		float bl;

		BlockState blockState = lightCache.getLevel().getBlockState(pos);
		boolean onlyAffectPathBlocks = SSPBClientMod.options().onlyAffectPathBlocks;

		if((!onlyAffectPathBlocks && blockState.propagatesSkylightDown(lightCache.getLevel(), pos)) ||
				(isParallel && onlyAffectPathBlocks && blockState.getBlock() instanceof DirtPathBlock)){

			// Mix between sodium inset lighting (default applyInsetPartialFaceVertex) and vanilla-like inset lighting (applyAlignedPartialFaceVertex).
			float shadowyness = SSPBClientMod.options().getShadowyness(); // vanilla-like inset lighting percentage
			float shadowynessCompliment = SSPBClientMod.options().getShadowynessCompliment(); // sodium inset lighting percentage

			ao = (((ao1 * n1d) + (ao2 * n2d)) * shadowynessCompliment) + (ao1 * shadowyness);
			sl = (((sl1 * n1d) + (sl2 * n2d)) * shadowynessCompliment) + (sl1 * shadowyness);
			bl = (((bl1 * n1d) + (bl2 * n2d)) * shadowynessCompliment) + (bl1 * shadowyness);
		}
		else{
			// Do not apply this change to fluids or full blocks (to fix custom 3D models having dark insides)
			ao = (ao1 * n1d) + (ao2 * n2d);
			sl = (sl1 * n1d) + (sl2 * n2d);
			bl = (bl1 * n1d) + (bl2 * n2d);
		}

		out.br[i] = ao;
		out.lm[i] = getLightMapCoord(sl, bl);
	}


	@Redirect(method = "applyParallelFace", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/model/light/smooth/SmoothLightPipeline;applyInsetPartialFaceVertex(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;FF[FILnet/caffeinemc/mods/sodium/client/model/light/data/QuadLightData;Z)V"))
	private void redirectParallelApplyInset(SmoothLightPipeline instance, BlockPos pos, Direction dir, float n1d, float n2d, float[] w, int i, QuadLightData out, boolean shade){
		sspb$applyInsetPartialFaceVertex(pos, dir, n1d, n2d, w, i, out, shade, true);
	}

	@Redirect(method = "applyNonParallelFace", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/model/light/smooth/SmoothLightPipeline;applyInsetPartialFaceVertex(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;FF[FILnet/caffeinemc/mods/sodium/client/model/light/data/QuadLightData;Z)V"))
	private void redirectNonParallelApplyInset(SmoothLightPipeline instance, BlockPos pos, Direction dir, float n1d, float n2d, float[] w, int i, QuadLightData out, boolean shade){
		sspb$applyInsetPartialFaceVertex(pos, dir, n1d, n2d, w, i, out, shade, false);
	}

	@ModifyVariable(method = "gatherInsetFace", at = @At("STORE"), ordinal = 0)
	private float modifyGatherInsetFaceW1(float w1, ModelQuadView quad, BlockPos blockPos, int vertexIndex, Direction lightFace, boolean shade){
		if(SSPBClientMod.options().onlyAffectPathBlocks && !(lightCache.getLevel().getBlockState(blockPos).getBlock() instanceof DirtPathBlock)){
			return w1;
		}
		return (w1 * SSPBClientMod.options().getShadowynessCompliment()) + (SSPBClientMod.options().getShadowyness());
	}

	@Inject(method = "calculate", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/model/light/smooth/SmoothLightPipeline;applyParallelFace(Lnet/caffeinemc/mods/sodium/client/model/light/smooth/AoNeighborInfo;Lnet/caffeinemc/mods/sodium/client/model/quad/ModelQuadView;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/caffeinemc/mods/sodium/client/model/light/data/QuadLightData;Z)V", shift = At.Shift.BEFORE), cancellable = true)
	private void injectVanillaAoCalcForPathBlocks(ModelQuadView quad, BlockPos pos, QuadLightData out, Direction cullFace, Direction lightFace, boolean shade, boolean isFluid, CallbackInfo ci){
		if(SSPBClientMod.options().vanillaPathBlockLighting && lightCache.getLevel().getBlockState(pos).getBlock() instanceof DirtPathBlock){
			sspb$calcVanilla((QuadViewImpl) quad, out.br, out.lm, pos, lightFace, shade);
			ci.cancel();
		}
	}


	/*
	 * From here to the end of the file is code adapted from AoCalculator.java from
	 * "Fabric Renderer - Indigo" from "Fabric API".
	 */

	@Unique
	private final ModelBlockRenderer.AmbientOcclusionFace sspb$vanillaCalc = new ModelBlockRenderer.AmbientOcclusionFace();

	// These are what vanilla AO calc wants, per its usage in vanilla code
	// Because this instance is effectively thread-local, we preserve instances
	// to avoid making a new allocation each call.
	@Unique
	private final float[] sspb$vanillaAoData = new float[Direction.values().length * 2];
	@Unique
	private final BitSet sspb$vanillaAoControlBits = new BitSet(3);
	@Unique
	private final int[] sspb$vertexData = new int[EncodingFormat.QUAD_STRIDE];


	@Unique
	private void sspb$calcVanilla(QuadViewImpl quad, float[] aoDest, int[] lightDest, BlockPos pos, Direction lightFace, boolean shade) {
		sspb$vanillaAoControlBits.clear();
		quad.toVanilla(sspb$vertexData, 0);

		BlockAndTintGetter level = lightCache.getLevel();

		VanillaAoHelper.updateShape(level, level.getBlockState(pos), pos, sspb$vertexData, lightFace, sspb$vanillaAoData, sspb$vanillaAoControlBits);
		sspb$vanillaCalc.calculate(level, level.getBlockState(pos), pos, lightFace, sspb$vanillaAoData, sspb$vanillaAoControlBits, shade);

		System.arraycopy(((AmbientOcclusionFaceAccessor) sspb$vanillaCalc).sspb$getBrightness(), 0, aoDest, 0, 4);
		System.arraycopy(((AmbientOcclusionFaceAccessor) sspb$vanillaCalc).sspb$getLightmap(), 0, lightDest, 0, 4);
	}
}
