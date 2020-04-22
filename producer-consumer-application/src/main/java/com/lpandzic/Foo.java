package com.lpandzic;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@AllArgsConstructor
@Value
public class Foo {
    private final String foo;
    private final Instant createdAt;
}
