package com.board.common;

import org.springframework.core.io.Resource;

import lombok.Data;

@Data
public class ResourceAndOriginName {
    private final Resource resource;
    private final String originName;
}
