/*
 * This file is a modified version of VanillaAoHelper.java from
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

package rynnavinx.sspb.common.client.render.frapi.aocalc;

import java.util.BitSet;

import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import rynnavinx.sspb.common.mixin.minecraft.ModelBlockRendererAccessor;


public class VanillaAoHelper {
    // Renderer method we call isn't declared as static, but uses no
    // instance data and is called from multiple threads in vanilla also.
    private static ModelBlockRendererAccessor blockRenderer;

    public static void initialize(ModelBlockRenderer instance) {
        blockRenderer = (ModelBlockRendererAccessor) instance;
    }

    public static void updateShape(BlockAndTintGetter blockRenderView, BlockState blockState, BlockPos pos, int[] vertexData, Direction face, float[] aoData, BitSet controlBits) {
        blockRenderer.sspb$invokeCalculateShape(blockRenderView, blockState, pos, vertexData, face, aoData, controlBits);
    }
}