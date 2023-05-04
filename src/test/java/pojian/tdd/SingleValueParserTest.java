package pojian.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static pojian.tdd.BooleanParserTest.option;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SingleValueParserTest {

    @Test //sad path
    public void should_not_accept_extra_argument_for_single_valued_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingeValueParser<>(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
        });

        assertEquals("p", e.getOption());
    }

    @ParameterizedTest //sad path
    @ValueSource(strings =  {"-p -l", "-p"})
    public void should_not_accept_insufficient_argument_for_single_value_option(String arguments) {
        InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
            new SingeValueParser<>(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
        });

        assertEquals("p", e.getOption());
    }

    @Test //default value
    public void should_set_default_value_to_0_for_int_option() {
        assertEquals(0, new SingeValueParser<>(0, Integer::parseInt).parse(asList(), option("p")));
    }

    @Test //happy path
    public void should_parse_value_if_int_option_format_is_correct() {
        assertEquals(8080, new SingeValueParser<>(0, Integer::parseInt).parse(asList("-p", "8080"), option("p")));
    }

    @Test //happy path
    public void should_parse_value_if_flag_present() {
        Object parsed = new Object();
        Function<String, Object> parse = (it) -> parsed;
        Object whatever = new Object();
        assertSame(parsed, new SingeValueParser<>(whatever, parse).parse(asList("-p", "8080"), option("p")));
    }

    @Test
    public void should_not_accept_extra_argument_for_string_valued_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingeValueParser<>("", String::valueOf).parse(asList("-d", "/usr/logs", "/usr/vars"), option("d"));
        });

        assertEquals("d", e.getOption());
    }
}
