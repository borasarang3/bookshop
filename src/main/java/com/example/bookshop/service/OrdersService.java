package com.example.bookshop.service;

import com.example.bookshop.dto.OrdersDTO;
import com.example.bookshop.dto.OrdersHistDTO;
import com.example.bookshop.dto.OrdersItemDTO;
import com.example.bookshop.entity.*;
import com.example.bookshop.repository.ImageRepository;
import com.example.bookshop.repository.OrdersRepository;
import com.example.bookshop.repository.ProductRepository;
import com.example.bookshop.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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

    //주문 목록
    @Transactional(readOnly = true) //읽기만 entity 수정 안되도록
    public Page<OrdersHistDTO> getOrdersList(String userId, Pageable pageable) {

        List<Orders> ordersList = ordersRepository.findOrders(userId, pageable);
        Long totalCount = ordersRepository.countOrder(userId);

        List<OrdersHistDTO> ordersHistDTOList = new ArrayList<>();

        for (Orders orders : ordersList){
            OrdersHistDTO ordersHistDTO = new OrdersHistDTO(orders);

            List<OrdersItem> ordersItemList = orders.getOrdersItems();

            for (OrdersItem ordersItem : ordersItemList){
                // orders에는 아이템의 이미지가 없기에
                // 아이템의 아이디를 통해, 대표이미지인 거 찾기
                Image image = imageRepository
                        .findByProductPnoAndRepimgYn(ordersItem.getProduct().getPno(), "Y");

                OrdersItemDTO ordersItemDTO
                        = new OrdersItemDTO(ordersItem, image.getImgUrl());

                ordersItemDTO.setPno(ordersItem.getProduct().getPno());
                ordersItemDTO.setWriter(ordersItem.getProduct().getWriter());
                ordersItemDTO.setPublish(ordersItem.getProduct().getPublish());

                ordersHistDTO.addOrdersItemDTO(ordersItemDTO);

            }

            ordersHistDTOList.add(ordersHistDTO);

        }

        ordersHistDTOList.forEach(ordersHistDTO -> log.info(ordersHistDTO));

        return new PageImpl<OrdersHistDTO>(ordersHistDTOList, pageable, totalCount);

    }

    //주인 확인
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String userId) {
        //로그인한 사람의 아이디로 찾은 유저
        UserMember userMember = userRepository.findByUserId(userId);
        //상품이 참조하고 있는 유저와
        Orders orders = ordersRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        UserMember savedMember = orders.getUserMember();

        //로그인한 사람과 주문한 사람이 같지 않다면 false
        if (!StringUtils.equals(userMember.getUserId(), savedMember.getUserId())){
            return false;
        }

        //같다면 true
        return true;

    }

    public void cancelOrder(Long ordersId) {

        Orders orders = ordersRepository.findById(ordersId).orElseThrow(EntityNotFoundException::new);

        orders.cancelOrder();

    }


}
