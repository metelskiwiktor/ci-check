package rules.success;

import java.util.logging.Logger;

public class SystemErrUsage {
    private static final Logger LOGGER = Logger.getLogger(SystemErrUsage.class.getName());
    
    public void method() {
        LOGGER.severe("This is an error message!");
    }
}