package com.github.tix_measurements.time.model.reporting.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class TixDataPacketMixin {
    @JsonIgnore
    abstract boolean isValid();
}