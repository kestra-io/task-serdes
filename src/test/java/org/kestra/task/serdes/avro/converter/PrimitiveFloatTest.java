package org.kestra.task.serdes.avro.converter;

import org.apache.avro.Schema;
import org.kestra.task.serdes.avro.AvroConverter;
import org.kestra.task.serdes.avro.AvroConverterTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class PrimitiveFloatTest {
    static Stream<Arguments> source() {
        return Stream.of(
            Arguments.of(-42F, -42F),
            Arguments.of("-42", -42F),
            Arguments.of(-42, -42F),
            Arguments.of(-42D, -42F),
            Arguments.of(42F, 42F),
            Arguments.of("42", 42F),
            Arguments.of(42, 42F),
            Arguments.of(42D, 42F)
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    void convert(Object v, float expected) throws Exception {
        AvroConverterTest.Utils.oneField(v, expected, Schema.create(Schema.Type.FLOAT));
    }

    static Stream<Arguments> separator() {
        return Stream.of(
            Arguments.of("-42.1", -42.1F, '.'),
            Arguments.of("42,1", 42.1F, ','),
            Arguments.of("42|1", 42.1F, '|')
        );
    }

    @ParameterizedTest
    @MethodSource("separator")
    void convertSeparator(Object v, float expected, Character separator) throws Exception {
        AvroConverterTest.Utils.oneField(
            AvroConverter.builder().decimalSeparator(separator).build(),
            v,
            expected,
            Schema.create(Schema.Type.FLOAT)
        );
    }

    static Stream<Arguments> failedSource() {
        return Stream.of(
            Arguments.of("a"),
            Arguments.of(9223372036854775807L),
            Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("failedSource")
    void failed(Object v) {
        AvroConverterTest.Utils.oneFieldFailed(v, Schema.create(Schema.Type.FLOAT));
    }
}
