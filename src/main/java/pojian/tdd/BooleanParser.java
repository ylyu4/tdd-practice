package pojian.tdd;

import java.util.List;

import static pojian.tdd.SingeValueParser.values;

class BooleanParser implements OptionParser<Boolean> {
    @Override
    public Boolean parse(List<String> arguments, Option option) {
        return values(arguments, option, 0).map(it -> true).orElse(false);
    }
}
