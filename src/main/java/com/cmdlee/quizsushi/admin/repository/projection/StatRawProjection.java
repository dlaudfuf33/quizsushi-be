package com.cmdlee.quizsushi.admin.dto.response;

import java.time.LocalDateTime;

public interface StatRawProjection {
    String getLabel();

    LocalDateTime getTime();

    long getCount();
}