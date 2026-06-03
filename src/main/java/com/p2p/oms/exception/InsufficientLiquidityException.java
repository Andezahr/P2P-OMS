package com.p2p.oms.exception;

import java.util.function.Supplier;

public class InsufficientLiquidityException extends IllegalArgumentException {

    public InsufficientLiquidityException(String message) {
        super("NOT_ENOUGH_RESERVED_LIQUIDITY:" + message);
    }

    public static Supplier<InsufficientLiquidityException> of(String message) {
        return () -> new InsufficientLiquidityException(message);
    }
}
