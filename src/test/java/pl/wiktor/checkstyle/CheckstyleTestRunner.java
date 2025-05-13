package pl.wiktor.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Utility class for running Checkstyle tests.
 */
public class CheckstyleTestRunner {
    private static final String CONFIG_FILE = "checkstyle.xml";
    
    /**
     * Runs Checkstyle on a single file and returns the list of audit events.
     */
    public static List<AuditEvent> runCheckstyle(String filePath) throws Exception {
        Configuration config = loadConfiguration();
        Checker checker = createChecker(config);
        
        List<AuditEvent> events = new LinkedList<>();
        checker.addListener(createAuditListener(events));
        
        try {
            checker.process(Collections.singletonList(new File(filePath)));
        } finally {
            checker.destroy();
        }
        
        return events;
    }
    
    private static Configuration loadConfiguration() throws Exception {
        try (FileInputStream configStream = new FileInputStream(CONFIG_FILE)) {
            return ConfigurationLoader.loadConfiguration(
                    new InputSource(configStream),
                    new PropertiesExpander(new Properties()),
                    ConfigurationLoader.IgnoredModulesOptions.EXECUTE
            );
        }
    }
    
    private static Checker createChecker(Configuration config) throws Exception {
        Checker checker = new Checker();
        checker.setModuleClassLoader(Checker.class.getClassLoader());
        checker.configure(config);
        return checker;
    }
    
    private static AuditListener createAuditListener(List<AuditEvent> events) {
        return new AuditListener() {
            @Override
            public void auditStarted(AuditEvent e) {
            }
            
            @Override
            public void auditFinished(AuditEvent e) {
            }
            
            @Override
            public void fileStarted(AuditEvent e) {
            }
            
            @Override
            public void fileFinished(AuditEvent e) {
            }
            
            @Override
            public void addError(AuditEvent e) {
                events.add(e);
            }
            
            @Override
            public void addException(AuditEvent e, Throwable t) {
            }
        };
    }
}