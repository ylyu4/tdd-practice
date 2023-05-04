package pojian.tdd;

import java.util.List;
import java.util.function.Function;


class SingeValueParser<T> implements OptionParser {

    Function<String, T> valueParser;

    public SingeValueParser(Function<String, T> valueParser) {
        this.valueParser = valueParser;
    }


    @Override
    public Object parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        String value = arguments.get(index + 1);
        return valueParser.apply(value);
    }
}
