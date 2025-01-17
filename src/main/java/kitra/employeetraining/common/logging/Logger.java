package kitra.employeetraining.common.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    private final String label;

    private Logger(String label) {
        this.label = label;
    }

    public static Logger getInstance(String name) {
        return new Logger(name);
    }

    public void debug(String o) {
        System.out.printf("%s DEBUG | %s: %s\n", LocalDateTime.now().format(format), this.label, o);
    }

    public void info(String o) {
        System.out.printf("%s INFO | %s: %s\n", LocalDateTime.now().format(format), this.label, o);
    }

    public void err(String o) {
        System.err.printf("%s ERROR | %s: %s\n", LocalDateTime.now().format(format), this.label, o);
    }
}
