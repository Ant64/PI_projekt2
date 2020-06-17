package pl.piotr.catalog.lib;

import org.apache.commons.lang3.StringUtils;
import pl.piotr.catalog.domain.exception.DomainException;

import java.util.Objects;
import java.util.function.Supplier;

public class Assertion {
    private Assertion(){}

    public static void isTrue(boolean condition, Supplier<DomainException> exception){
        if(!condition){
            throw exception.get();
        }
    }

    public static void notEmpty(String value, Supplier<DomainException> exception){
        if(StringUtils.isBlank(value)){
            throw exception.get();
        }
    }

    public static void notNull(Object object, Supplier<DomainException> exception){
        if(Objects.isNull(object)){
            throw exception.get();
        }
    }
}
