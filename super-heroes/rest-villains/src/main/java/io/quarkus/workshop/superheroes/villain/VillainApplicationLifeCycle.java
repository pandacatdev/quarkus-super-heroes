package io.quarkus.workshop.superheroes.villain;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;

@ApplicationScoped
public class VillainApplicationLifeCycle {
    private static final Logger LOGGER = Logger.getLogger(VillainApplicationLifeCycle.class);

    void onStart(@Observes StartupEvent startupEvent) {
        LOGGER.info("The application VILLAIN is starting with profile " + ConfigUtils.getProfiles());
        LOGGER.info("!   ██▒   █▓ ██▓ ██▓     ██▓    ▄▄▄       ██▓ ███▄    █     ▄▄▄       ██▓███   ██▓");
        LOGGER.info("!  ▓██░   █▒▓██▒▓██▒    ▓██▒   ▒████▄    ▓██▒ ██ ▀█   █    ▒████▄    ▓██░  ██▒▓██▒");
        LOGGER.info("!   ▓██  █▒░▒██▒▒██░    ▒██░   ▒██  ▀█▄  ▒██▒▓██  ▀█ ██▒   ▒██  ▀█▄  ▓██░ ██▓▒▒██▒");
        LOGGER.info("!    ▒██ █░░░██░▒██░    ▒██░   ░██▄▄▄▄██ ░██░▓██▒  ▐▌██▒   ░██▄▄▄▄██ ▒██▄█▓▒ ▒░██░");
        LOGGER.info("!     ▒▀█░  ░██░░██████▒░██████▒▓█   ▓██▒░██░▒██░   ▓██░    ▓█   ▓██▒▒██▒ ░  ░░██░");
        LOGGER.info("!     ░ ▐░  ░▓  ░ ▒░▓  ░░ ▒░▓  ░▒▒   ▓▒█░░▓  ░ ▒░   ▒ ▒     ▒▒   ▓▒█░▒▓▒░ ░  ░░▓  ");
        LOGGER.info("!     ░ ░░   ▒ ░░ ░ ▒  ░░ ░ ▒  ░ ▒   ▒▒ ░ ▒ ░░ ░░   ░ ▒░     ▒   ▒▒ ░░▒ ░      ▒ ░");
        LOGGER.info("!       ░░   ▒ ░  ░ ░     ░ ░    ░   ▒    ▒ ░   ░   ░ ░      ░   ▒   ░░        ▒ ░");
        LOGGER.info("!        ░   ░      ░  ░    ░  ░     ░  ░ ░           ░          ░  ░          ░  ");
        LOGGER.info("!       ░                                                                         ");
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        LOGGER.info("The application VILLAIN is stopping...");
    }
}
