package pojian.tdd;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.lang.annotation.Annotation;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionParsersTest {

    @Nested
    class UnaryParserTest {

        @Test //sad path
        public void should_not_accept_extra_argument_for_single_valued_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
            });

            assertEquals("p", e.getOption());
        }

        @ParameterizedTest //sad path
        @ValueSource(strings = {"-p -l", "-p"})
        public void should_not_accept_insufficient_argument_for_single_value_option(String arguments) {
            InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
            });

            assertEquals("p", e.getOption());
        }

        @Test //default value
        public void should_set_default_value_to_0_for_int_option() {
            assertEquals(0, OptionParsers.unary(0, Integer::parseInt).parse(asList(), option("p")));
        }

        @Test //happy path
        public void should_parse_value_if_int_option_format_is_correct() {
            assertEquals(8080, OptionParsers.unary(0, Integer::parseInt).parse(asList("-p", "8080"), option("p")));
        }

        @Test //happy path
        public void should_parse_value_if_flag_present() {
            Object parsed = new Object();
            Function<String, Object> parse = (it) -> parsed;
            Object whatever = new Object();
            assertSame(parsed, OptionParsers.unary(whatever, parse).parse(asList("-p", "8080"), option("p")));
        }

        @Test
        public void should_not_accept_extra_argument_for_string_valued_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.unary("", String::valueOf).parse(asList("-d", "/usr/logs", "/usr/vars"), option("d"));
            });

            assertEquals("d", e.getOption());
        }
    }


    @Nested
    class BooleanParserTest {

        @Test
        public void should_not_accept_extra_argument_for_boolean_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.bool().parse(asList("-l", "t"), option("l"));
            });
            assertEquals("l", e.getOption());
        }

        @Test
        public void should_not_accept_extra_arguments_for_boolean_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.bool().parse(asList("-l", "t", "f"), option("l"));
            });
            assertEquals("l", e.getOption());
        }

        @Test //default value
        public void should_set_default_value_to_false_if_option_not_present() {
            // setup
            OptionParser<Boolean> parser = OptionParsers.bool();
            Boolean result;

            // exercise
            result = parser.parse(asList(), option("l"));

            // verify
            assertFalse(result);

            // teardown
        }

        @Test //happy path
        public void should_set_value_to_true_if_option_not_present() {
            assertTrue(OptionParsers.bool().parse(asList("-l"), option("l")));
        }
    }


    @Nested
    class ListOptionParser {
        //TODO: -g "this" "is" {"this", is"}
        @Test
        public void should_parse_list_value() {
            assertArrayEquals(new String[]{"this", "is"}, OptionParsers.list(String[]::new, String::valueOf).parse(asList("-g", "this", "is"), option("g")));
        }
        // TODO: default value []
        @Test
        public void should_use_empty_array_as_Default_value() {
            String[] value = OptionParsers.list(String[]::new, String::valueOf).parse(asList(), option("g"));
            assertEquals(0, value.length);
        }
        // TODO: -d a throw exception
        @Test
        public void should_throw_exception_if_value_parser_cant_parse_value() {
            Function<String, String> parser = (it) -> {
                throw new RuntimeException();
            };
            IllegalValueException e = assertThrows(IllegalValueException.class, () -> OptionParsers.list(String[]::new, parser).parse(asList("-g", "this", "is"), option("g")));
            assertEquals("g", e.getOption());
            assertEquals("this", e.getValue());
        }

        @Test
        public void should_not_treat_negative_int_as_flag() {
           assertArrayEquals(new Integer[]{-1, -2}, OptionParsers.list(Integer[]::new, Integer::parseInt).parse(asList("-g", "-1", "-2"), option("g")));
        }


    }

    static Option option(String value) {
        return new Option() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Option.class;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }
}
