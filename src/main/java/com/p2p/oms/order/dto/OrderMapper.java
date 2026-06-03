package com.p2p.oms.order.dto;

import com.p2p.oms.config.CentralMapperConfig;
import com.p2p.oms.order.dto.response.OrderResponse;
import com.p2p.oms.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface OrderMapper {

    @Mapping(target = "makerAdId", source = "makerAd.id")
    @Mapping(target = "makerUserId", source = "makerUser.id")
    @Mapping(target = "takerUserId", source = "takerUser.id")
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}