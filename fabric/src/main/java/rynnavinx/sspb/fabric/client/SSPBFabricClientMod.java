package rynnavinx.sspb.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import rynnavinx.sspb.common.client.SSPBClientMod;


@Environment(EnvType.CLIENT)
public class SSPBFabricClientMod implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SSPBClientMod.onInitClient();
	}
}
