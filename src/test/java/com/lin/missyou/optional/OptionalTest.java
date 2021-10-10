package com.lin.missyou.optional;

import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OptionalTest {
    @Test
    public void testOptional(){
        Optional<String> empty = Optional.empty();
//        Optional<String> t1 = Optional.of(null);
        Optional<String> t2 = Optional.ofNullable("a");
//        empty.get();
//        return empty;
//        String s = t2.get();
        t2.ifPresent(System.out::println);

        String s = t2.orElse("默认值");
        System.out.println(s);

        String s1 = t2.map(t->t+"b").orElse("c");
        System.out.println(s1);

        t2.map(t->t+"b").ifPresent(System.out::println);


    }
}
