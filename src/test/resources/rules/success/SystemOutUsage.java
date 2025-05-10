package rules.success;

import java.util.logging.Logger;

public class SystemOutUsage {
    private static final Logger LOGGER = Logger.getLogger(SystemOutUsage.class.getName());
    
    public void method() {
        LOGGER.info("This is the proper way to log!");
    }
}