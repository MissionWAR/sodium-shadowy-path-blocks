package rynnavinx.sspb.common.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rynnavinx.sspb.common.client.gui.SSPBGameOptions;


public class SSPBClientMod {

	public static final Logger LOGGER = LoggerFactory.getLogger("SSPB");

	private static SSPBGameOptions CONFIG;


	public static SSPBGameOptions options() {
		return CONFIG;
	}

	public static void onInitClient() {
		CONFIG = SSPBGameOptions.load();
		CONFIG.updateShadowyness(CONFIG.shadowynessPercent);

		LOGGER.info("[SSPB] Broken dirt path lighting is best dirt path lighting lol");
	}
}
