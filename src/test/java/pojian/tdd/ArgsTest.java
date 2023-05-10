package pojian.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        //SUT Args.parse


        //before
        MultiOptions options;

        //exercise
        options = Args.parse(MultiOptions.class, "-l", "-p", "8080", "-d", "/usr/logs");


        //verify
        assertTrue(options.logging());
        assertEquals(8080, options.port());
        assertEquals("/usr/logs", options.directory());

        //teardown
    }

    @Test
    public void should_throw_illegal_option_if_annotation_not_present() {

        // before
        IllegalOptionException e;

        e = /*verify*/ assertThrows(IllegalOptionException.class, () -> /*exercise*/ Args.parse(OptionWithoutAnnotation.class,"-l", "-p", "8080", "-d", "/usr/logs"));

        // verify
        assertEquals("port", e.getParameter());

        // teardown
    }

    // setup
    static record OptionWithoutAnnotation(@Option("l")boolean logging, int port, @Option("d")String directory){}

    static record MultiOptions(@Option("l")boolean logging, @Option("p")int port, @Option("d")String directory){}


    @Test
    public void should_example_2() {
        ListOptions options = Args.parse(ListOptions.class,"-g", "this", "is", "a", "list", "-d", "1", "2", "-3", "5");

        assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.group());
        assertArrayEquals(new Integer[]{1, 2, -3, 5}, options.decimals());
    }


    static record ListOptions(@Option("g") String[] group, @Option("d") Integer[] decimals) {

    }

}
