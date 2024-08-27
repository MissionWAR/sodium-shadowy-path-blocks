package rynnavinx.sspb.fabric.services;

import net.fabricmc.loader.api.FabricLoader;

import rynnavinx.sspb.common.services.IPlatformHelper;

import java.nio.file.Path;


public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
