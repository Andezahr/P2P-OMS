package com.p2p.oms.ad.dto.request;

import com.p2p.oms.ad.entity.AdStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeAdStatusRequest(

        @NotNull
        AdStatus status
) {
}