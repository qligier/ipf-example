package ch.qligier.ipf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * The application event handler for startup.
 *
 * @author Quentin Ligier
 */
@Configuration
public class StartupEventHandler implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger log = LoggerFactory.getLogger(StartupEventHandler.class);

    /**
     * Handler for the application ready event.
     *
     * @param event Details of the event.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.info("IPF demo application has started :)");
    }
}
