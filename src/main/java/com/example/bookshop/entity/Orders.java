package com.example.bookshop.entity;

import com.example.bookshop.constant.OrderStatus;
import com.example.bookshop.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Orders {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersId; //주문Id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserMember userMember; //구매자ID

    @Column(nullable = false)
    private String delivery; //배송주소

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true
            , fetch = FetchType.LAZY)
    private List<OrdersItem> ordersItems = new ArrayList<>();

    private LocalDateTime orderDate;

    //주문 취소, 주문
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //주문 아이템들을 만든다. 위에 List 주문아이템들 있다.

    public void addOrderItem(OrdersItem ordersItem){//아이템 하나를 받음
        ordersItems.add(ordersItem); // 위에 아이템리스트에 넣는다.
        //주문은 주문아이템들을 가지고 있다.
        ordersItem.setOrders(this); //주문아이템을 주문을 참조한다.
    }

    public static Orders createOrder(UserMember userMember, List<OrdersItem> ordersItemList){
        Orders orders = new Orders(); //저장에 쓰일 주문 엔티티 만드는 메소드
        orders.setUserMember(userMember);

        for (OrdersItem ordersItem : ordersItemList){
            orders.addOrderItem(ordersItem); //주문에 있는 주문 아이템 리스트 넣기 반복해서
        }

        orders.setOrderStatus(OrderStatus.ORDER); //주문상태
        orders.setOrderDate(LocalDateTime.now()); //주문시간
        orders.setDelivery(userMember.getAddress()); //주소

        //그렇게 만들어진 엔티티를 반환한다.
        return orders;

    }

    public Long getTotalPrice(){
        //하나의 주문에 있는 전체 아이템들의 가격 총합
        Long totalPrice = 0L;

        for (OrdersItem ordersItem : ordersItems){
            //주문한 하나의 아이템의 가격 * 수량
            totalPrice += ordersItem.getTotalPrice();
        }
        return totalPrice;

    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for (OrdersItem ordersItem : ordersItems){
            ordersItem.cancel();
        }
    }


}
