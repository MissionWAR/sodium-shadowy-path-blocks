/*
 * This file is a modified version of BlockModelRendererMixin.java from
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

package rynnavinx.sspb.common.mixin.minecraft.frapi;

import net.minecraft.client.renderer.block.ModelBlockRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rynnavinx.sspb.common.client.render.frapi.aocalc.VanillaAoHelper;


@Mixin(ModelBlockRenderer.class)
public abstract class MixinModelBlockRenderer {

    @Inject(at = @At("RETURN"), method = "<init>*")
    private void onInit(CallbackInfo ci) {
        VanillaAoHelper.initialize((ModelBlockRenderer) (Object) this);
    }
}
