package pojian.tdd;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgsTest {

    //-l -p 8080 -d /usr/logs

    // sad path:
    // TODO: -bool -l t / -l t f
    // TODO: - int -p/ -p 8080 8081
    // TODO: - string -d/ -d /usr/logs /usr/vars
    // default value:
    // TODO: - bool : false
    // TODO: -int :0
    // TODO: - string ""

    @Test
    void should_parse_multi_options_as_option_value() {
        MultiOptions options = Args.parse(MultiOptions.class, "-l", "-p", "8080", "-d", "/usr/logs");

        assertTrue(options.logging());
        assertEquals(8080, options.port());
        assertEquals("/usr/logs", options.directory());
    }

    static record MultiOptions(@Option("l")boolean logging, @Option("p")int port, @Option("d")String directory){}


}
