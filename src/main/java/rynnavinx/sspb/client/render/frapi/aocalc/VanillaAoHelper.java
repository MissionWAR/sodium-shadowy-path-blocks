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

package rynnavinx.sspb.client.render.frapi.aocalc;

import java.util.BitSet;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import rynnavinx.sspb.mixin.minecraft.BlockModelRendererAccessor;


public class VanillaAoHelper {
    // Renderer method we call isn't declared as static, but uses no
    // instance data and is called from multiple threads in vanilla also.
    private static BlockModelRendererAccessor blockRenderer;

    public static void initialize(BlockModelRenderer instance) {
        blockRenderer = (BlockModelRendererAccessor) instance;
    }

    public static void updateShape(BlockRenderView blockRenderView, BlockState blockState, BlockPos pos, int[] vertexData, Direction face, float[] aoData, BitSet controlBits) {
        blockRenderer.sspb$invokeGetQuadDimensions(blockRenderView, blockState, pos, vertexData, face, aoData, controlBits);
    }
}