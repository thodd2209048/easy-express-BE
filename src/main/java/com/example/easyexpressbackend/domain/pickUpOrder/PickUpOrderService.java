package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.domain.hub.Hub;
import com.example.easyexpressbackend.domain.hub.HubService;
import com.example.easyexpressbackend.domain.hub.response.HubNameAndIdResponse;
import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AddPickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AdminUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.CustomerUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.StaffUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.modal.PickUpOrderMessage;
import com.example.easyexpressbackend.domain.pickUpOrder.producer.AddHubToPickUpOrderProducer;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.*;
import com.example.easyexpressbackend.domain.region.RegionService;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import com.example.easyexpressbackend.exception.ActionNotAllowedException;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PickUpOrderService {
    private final PickUpOrderRepository repository;
    private final PickUpOrderMapper mapper;
    private final RegionService regionService;
    private final HubService hubService;
    private final AddHubToPickUpOrderProducer addHubToPickUpOrderProducer;

    private static final List<PickUpOrderStatus> completedStatuses = List.of(
            PickUpOrderStatus.CANCELLED,
            PickUpOrderStatus.PICKED_UP,
            PickUpOrderStatus.EXPIRED,
            PickUpOrderStatus.SHIPMENT_NOT_READY
    );

    @Autowired
    public PickUpOrderService(PickUpOrderRepository repository,
                              PickUpOrderMapper mapper,
                              RegionService regionService,
                              HubService hubService,
                              AddHubToPickUpOrderProducer addHubToPickUpOrderProducer) {
        this.repository = repository;
        this.mapper = mapper;
        this.regionService = regionService;
        this.hubService = hubService;
        this.addHubToPickUpOrderProducer = addHubToPickUpOrderProducer;
    }

    public Page<ShortPickUpOrderForCustomerResponse> listPickUpOrdersForCustomer(Pageable pageable,
                                                                                 PickUpOrderStatus status,
                                                                                 ZonedDateTime startTime) {
        Page<PickUpOrder> orders = this.listPickUpOrders(
                pageable,
                status,
                null,
                startTime);
        return orders.map(this::convertToShortPickUpOrderForCustomerResponse);
    }

    public Page<ShortPickUpOrderForAdminResponse> listPickUpOrdersForAdmin(
            Pageable pageable,
            PickUpOrderStatus status,
            Long hubId,
            ZonedDateTime startTime) {
        return this.listPickUpOrders(pageable, status, hubId, startTime)
                .map(mapper::toShortPickUpOrderForAdminResponse);
    }

    private Page<PickUpOrder> listPickUpOrders(
            Pageable pageable,
            PickUpOrderStatus status,
            Long hubId,
            ZonedDateTime startTime) {
        ZonedDateTime endTime = startTime != null ? startTime.plusDays(1) : null;
        return repository.getPickUpOrderByCondition(pageable, status, hubId, startTime, endTime);
    }

    public GetPickUpOrderResponse getPickupOrder(Long id) {
        PickUpOrder order = this.getOrder(id);
        return mapper.toGetPickUpOrderResponse(order);
    }

    public GetPickUpOrderResponse addPickupOrder(AddPickUpOrderDto addPickUpOrderDto) {
        PickUpOrder order = mapper.fromAddPickUpOrderDto(addPickUpOrderDto);
        String number = "ORDER_1" + RandomStringUtils.randomNumeric(11);

//        Comment do dang bi loi
//        String cellAddress = locationService.getCellAddressFromAddress(order.getSenderAddress());

        order.setOrderNumber(number);
        order.setStatus(PickUpOrderStatus.INFORMATION_RECEIVED);

        repository.save(order);

        PickUpOrderMessage orderMessage = mapper.toPickUpOrderMessage(order);
        addHubToPickUpOrderProducer.convertAndSendRequest(orderMessage);

        return mapper.toGetPickUpOrderResponse(order);
    }

    public GetPickUpOrderResponse updatePickUpOrder(Long id, CustomerUpdatePickUpOrderDto customerUpdatePickUpOrderDto) {
        PickUpOrder currentOrder = this.getOrder(id);
        this.validateUpdatePickUpAllowedForCustomerNow(currentOrder);

        PickUpOrder orderForDto = new PickUpOrder();
        mapper.updatePickUpOrder(customerUpdatePickUpOrderDto, orderForDto);

        PickUpOrder savedOrder = this.updatePickUpOrder(id, orderForDto);

        return mapper.toGetPickUpOrderResponse(savedOrder);
    }

    private PickUpOrder updatePickUpOrder(Long id, PickUpOrder orderForDto) {
        PickUpOrder currentOrder = this.getOrder(id);

        this.validateUpdatePickUpAllowedForCustomerNow(currentOrder);
        this.validateUpdatePickUpOrderStatus(currentOrder);

        PickUpOrder newOrder = mapper.copyPickUpOrder(currentOrder);
        mapper.updatePickUpOrder(orderForDto, newOrder);

        if (newOrder.equals(currentOrder))
            throw new DuplicateObjectException("The updated object is the same as the existing one.");

        return repository.save(newOrder);
    }

    private void validateUpdatePickUpAllowedForCustomerNow(PickUpOrder currentOrder) {
        ZonedDateTime oneHourAfterNow = ZonedDateTime.now().plusHours(1);
        if (currentOrder.getStartTime().isBefore(oneHourAfterNow)) {
            throw new ActionNotAllowedException(
                    "You cannot update an order too close to the start time. You can only update an order at least 1 hour before the start time.");
        }
    }

    private void validateUpdatePickUpOrderStatus(PickUpOrder currentOrder) {
        PickUpOrderStatus currentStatus = currentOrder.getStatus();
        if (completedStatuses.contains(currentStatus)) {
            throw new ActionNotAllowedException("Cannot update order with status " + currentStatus + ".");
        }
    }

    public GetPickUpOrderResponse deletePickUpOrder(Long id) {
        PickUpOrder order = this.getOrder(id);
        this.validateUpdatePickUpOrderStatus(order);

        order.setStatus(PickUpOrderStatus.CANCELLED);

        repository.save(order);

        return mapper.toGetPickUpOrderResponse(order);
    }

    private PickUpOrder getOrder(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pick up order with id: " + id + " does not exist."));
    }

    private ShortPickUpOrderForCustomerResponse convertToShortPickUpOrderForCustomerResponse(PickUpOrder pickUpOrder) {
        return mapper.toShortPickUpOrderForCustomerResponse(pickUpOrder);
    }

    public GetPickUpOrderResponse updatePickUpOrderForStaff(Long id, StaffUpdatePickUpOrderDto pickUpOrderDto) {
        PickUpOrder currentOrder = this.getOrder(id);

        this.validateUpdatePickUpOrderStatus(currentOrder);

        mapper.updatePickUpOrder(pickUpOrderDto, currentOrder);

        repository.save(currentOrder);

        return mapper.toGetPickUpOrderResponse(currentOrder);
    }

    @Scheduled(cron = "0 0 1 * * *")
    private void updateStatusToExpired(){
        List<PickUpOrder> yesterdayIncompleteOrders = this.getIncompleteOrdersYesterday();
        for (PickUpOrder order: yesterdayIncompleteOrders
             ) {
            order.setStatus(PickUpOrderStatus.EXPIRED);
        }

        repository.saveAll(yesterdayIncompleteOrders);
    }

    private List<PickUpOrder> getIncompleteOrdersYesterday(){
        List<PickUpOrderStatus> waitingToPickUpStatuses = List.of(PickUpOrderStatus.INFORMATION_RECEIVED, PickUpOrderStatus.ASSIGNED_TO_HUB);
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime atEndOfYesterday = now.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime atStartOfYesterday = atEndOfYesterday.minusHours(24);


        List<PickUpOrder> yesterdayIncompleteOrders = new ArrayList<>();
        for (PickUpOrderStatus status: waitingToPickUpStatuses
        ) {
            yesterdayIncompleteOrders.addAll(
                    repository.findByStatusAndStartTimeBetween(status, atStartOfYesterday, atEndOfYesterday));
        }
        return yesterdayIncompleteOrders;
    }


}
