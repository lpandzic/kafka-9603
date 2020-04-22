package com.lpandzic;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@AllArgsConstructor
@Value
public class Bar {
    private final String bar;
    private final Instant createdAt;
}
