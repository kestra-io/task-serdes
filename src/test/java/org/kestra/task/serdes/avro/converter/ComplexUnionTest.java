package org.kestra.task.serdes.avro.converter;

import org.apache.avro.Schema;
import org.apache.avro.util.Utf8;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kestra.task.serdes.avro.AvroConverterTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComplexUnionTest {
    static Stream<Arguments> source() {
        return Stream.of(
            Arguments.of("null", Arrays.asList(Schema.Type.NULL, Schema.Type.BOOLEAN), null),
            Arguments.of(null, Arrays.asList(Schema.Type.NULL, Schema.Type.BOOLEAN), null),
            Arguments.of("null", Arrays.asList(Schema.Type.BOOLEAN, Schema.Type.NULL), null),
            Arguments.of("1", Arrays.asList(Schema.Type.INT, Schema.Type.NULL), 1),
            Arguments.of("n/a", Arrays.asList(Schema.Type.NULL, Schema.Type.STRING), null),
            Arguments.of("n/a", Arrays.asList(Schema.Type.STRING, Schema.Type.NULL), new Utf8("n/a"))
        );
    }

    static Stream<Arguments> invalidSource() {
        return Stream.of(
            Arguments.of("toto", Arrays.asList(Schema.Type.NULL, Schema.Type.BOOLEAN)),
            Arguments.of(null, Arrays.asList(Schema.Type.STRING, Schema.Type.BOOLEAN)),
            //TODO reflexion intime
            // Arguments.of("null", Arrays.asList(Schema.Type.BOOLEAN, Schema.Type.STRING)),
            Arguments.of(10000000L, Arrays.asList(Schema.Type.INT, Schema.Type.NULL)),
            Arguments.of(false, Arrays.asList(Schema.Type.INT, Schema.Type.NULL))
            //TODO reflexion time
            // Arguments.of("", Arrays.asList(Schema.Type.INT, Schema.Type.STRING, Schema.Type.FLOAT))
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    void convert(Object v, List<Schema.Type> schemas, Object expected) throws Exception {
        AvroConverterTest.Utils.oneField(v, expected, Schema.createUnion(schemas
            .stream()
            .map(Schema::create)
            .collect(Collectors.toList())
        ));
    }

    @ParameterizedTest
    @MethodSource("invalidSource")
    void testInvalidUnion(Object v, List<Schema.Type> schemas) {
        AvroConverterTest.Utils.oneFieldFailed(v, Schema.createUnion(schemas
            .stream()
            .map(Schema::create)
            .collect(Collectors.toList())
        ));
    }
}
