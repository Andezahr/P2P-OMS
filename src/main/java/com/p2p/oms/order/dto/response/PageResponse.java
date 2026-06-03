package com.p2p.oms.order.dto.response; // или com.p2p.oms.common.dto.response

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int number,
        int size,
        long totalElements,
        int totalPages
) {}