package com.p2p.oms.dto.request;

import com.p2p.oms.entity.ad.AdStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeAdStatusRequest(

        @NotNull
        AdStatus status
) {
}