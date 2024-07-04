package com.example.bookshop.service;

import com.example.bookshop.dto.OrdersDTO;
import com.example.bookshop.entity.Orders;
import com.example.bookshop.entity.OrdersItem;
import com.example.bookshop.entity.Product;
import com.example.bookshop.entity.UserMember;
import com.example.bookshop.repository.ImageRepository;
import com.example.bookshop.repository.OrdersRepository;
import com.example.bookshop.repository.ProductRepository;
import com.example.bookshop.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    //주문(구매)
    public Long order(OrdersDTO ordersDTO, String userId) {
        //1. 상품 찾기
        Product product = productRepository
                .findById(ordersDTO.getPno())
                .orElseThrow(EntityExistsException::new);

        //2. 로그인한 사람 찾기
        UserMember userMember =
                userRepository.findByUserId(userId);

        List<OrdersItem> ordersItemList = new ArrayList<>();

        OrdersItem ordersItem = OrdersItem.createOrdersItem(product, ordersDTO.getCount());

        //하나의 주문DTO를 주문아이템리스트 넣기
        ordersItemList.add(ordersItem);

        //만들어진 주문아이템리스트을 아이디와 함께 order 엔티티 만들기
        Orders orders = Orders.createOrder(userMember, ordersItemList);
        // orders entity를 만들어서 저장을 수행

        ordersRepository.save(orders); //반환타입은 orders인데
        // 어디에도 orders에 다시 set한 적은 없다

        log.info("order 테이블에 저장 : " + orders.getOrdersId());
        // 리턴값은 저장된 ordersID를 반환

        return orders.getOrdersId();

    }

    //주문(구매)+리스트 형태
    public Long order(List<OrdersDTO> ordersDTOList, String userId) {

        UserMember userMember = userRepository.findByUserId(userId);

        List<OrdersItem> ordersItemList = new ArrayList<>();

        for (OrdersDTO ordersDTO : ordersDTOList) {
            Product product = productRepository
                    .findById(ordersDTO.getPno())
                    .orElseThrow(EntityNotFoundException::new);

            OrdersItem ordersItem = OrdersItem.createOrdersItem(product, ordersDTO.getCount());

            ordersItemList.add(ordersItem);

        }

        Orders orders = Orders.createOrder(userMember, ordersItemList); //dtoToEntity

        ordersRepository.save(orders);

        return orders.getOrdersId();



    }


}
