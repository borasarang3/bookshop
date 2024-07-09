package com.example.bookshop.dto;

import com.example.bookshop.constant.OrderStatus;
import com.example.bookshop.entity.Orders;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OrdersHistDTO {

    private Long ordersId; //주문아이디
    private String orderDate; //주문 날짜
    private OrderStatus orderStatus; //주문상태

    //주문상품 리스트
    private List<OrdersItemDTO> ordersItemDTOList = new ArrayList<>();

    public OrdersHistDTO(Orders orders) {

        this.ordersId = orders.getOrdersId();
        //this.OrderDate = order.getOrderDate(); //String 타입으로 변환을 위해서
                                                // format 적용
        this.orderDate = orders.getOrderDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM--dd HH:mm"));
                                                    // 2024-06-07 10:30 로 표기
        this.orderStatus = orders.getOrderStatus();

    }

    //메소드
    public void addOrdersItemDTO(OrdersItemDTO ordersItemDTO) {
        ordersItemDTOList.add(ordersItemDTO);
    }


}
