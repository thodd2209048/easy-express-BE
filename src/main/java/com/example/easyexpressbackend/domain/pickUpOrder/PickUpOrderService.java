package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.domain.hub.Hub;
import com.example.easyexpressbackend.domain.hub.HubService;
import com.example.easyexpressbackend.domain.hub.response.HubNameAndIdResponse;
import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AddPickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.AdminUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.dto.CustomerUpdatePickUpOrderDto;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.AdminUpdatePickUpOrderResponse;
import com.example.easyexpressbackend.domain.pickUpOrder.reponse.CustomerPickUpOrderResponse;
import com.example.easyexpressbackend.domain.region.RegionService;
import com.example.easyexpressbackend.domain.region.response.DistrictWithNameResponse;
import com.example.easyexpressbackend.domain.staff.Staff;
import com.example.easyexpressbackend.domain.staff.StaffService;
import com.example.easyexpressbackend.domain.staff.response.StaffIdNameResponse;
import com.example.easyexpressbackend.exception.ActionNotAllowedException;
import com.example.easyexpressbackend.exception.DuplicateObjectException;
import com.example.easyexpressbackend.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class PickUpOrderService {
    private final PickUpOrderRepository repository;
    private final PickUpOrderMapper mapper;
    private final RegionService regionService;
    private final HubService hubService;
    private final StaffService staffService;

    @Autowired
    public PickUpOrderService(PickUpOrderRepository repository,
                              PickUpOrderMapper mapper,
                              RegionService regionService,
                              HubService hubService,
                              StaffService staffService) {
        this.repository = repository;
        this.mapper = mapper;
        this.regionService = regionService;
        this.hubService = hubService;
        this.staffService = staffService;
    }

    public Page<CustomerPickUpOrderResponse> getPickUpOrders(Pageable pageable,
                                                             PickUpOrderStatus status,
                                                             ZonedDateTime startTime) {
        ZonedDateTime endTime = startTime != null ? startTime.plusDays(1) : null;
        Page<PickUpOrder> orders = repository.getPickUpOrderByCondition(pageable, status, startTime, endTime);
        return orders.map(this::convertToCustomerPickUpOrderResponse);
    }


    public CustomerPickUpOrderResponse addPickupOrder(AddPickUpOrderDto addPickUpOrderDto) {
        PickUpOrder order = mapper.fromAddPickUpOrderDto(addPickUpOrderDto);

        order.setStatus(PickUpOrderStatus.READY_FOR_PICK_UP);

        repository.save(order);

        return this.convertToCustomerPickUpOrderResponse(order);
    }

    public CustomerPickUpOrderResponse updatePickUpOrder(Long id, CustomerUpdatePickUpOrderDto customerUpdatePickUpOrderDto) {
        PickUpOrder orderForDto = new PickUpOrder();
        mapper.updatePickUpOrder(customerUpdatePickUpOrderDto, orderForDto);


        PickUpOrder savedOrder = this.updatePickUpOrder(id, orderForDto);

        return this.convertToCustomerPickUpOrderResponse(savedOrder);

    }

    public AdminUpdatePickUpOrderResponse updatePickUpOrder(Long id, AdminUpdatePickUpOrderDto updatePickUpOrderDto) {
        Hub hub = hubService.getHubById(updatePickUpOrderDto.getHubId());
        Staff staff = staffService.getStaffById(updatePickUpOrderDto.getStaffId());

        PickUpOrder orderForDto = new PickUpOrder();

        mapper.updatePickUpOrder(updatePickUpOrderDto, orderForDto);
        System.out.println(orderForDto);

        PickUpOrder savedOrder = this.updatePickUpOrder(id, orderForDto);

        return this.convertToAdminUpdatePickUpOrderResponse(savedOrder, hub, staff);
    }



    private PickUpOrder updatePickUpOrder(Long id, PickUpOrder orderForDto) {
        PickUpOrder currentOrder = this.getOrder(id);
        this.validateUpdatePickUpOrderStatus(currentOrder);

        PickUpOrder newOrder = mapper.copyPickUpOrder(currentOrder);
        mapper.updatePickUpOrder(orderForDto, newOrder);

        if (newOrder.equals(currentOrder))
            throw new DuplicateObjectException("The updated object is the same as the existing one.");

        return repository.save(newOrder);
    }

    private void validateUpdatePickUpOrderStatus(PickUpOrder currentOrder) {
        PickUpOrderStatus currentStatus = currentOrder.getStatus();
        if (currentStatus == PickUpOrderStatus.CANCELLED || currentStatus == PickUpOrderStatus.PICKED_UP) {
            throw new ActionNotAllowedException("Cannot update order with status " + currentStatus + ".");
        }
    }

    public void deletePickUpOrder(Long id) {
        PickUpOrder order = this.getOrder(id);

        order.setStatus(PickUpOrderStatus.CANCELLED);

        repository.save(order);
    }

    private PickUpOrder getOrder(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pick up order with id: " + id + " does not exist."));
    }

    private CustomerPickUpOrderResponse convertToCustomerPickUpOrderResponse(PickUpOrder order) {
        CustomerPickUpOrderResponse response = mapper.toCustomerPickUpOrderResponse(order);

        String districtCode = order.getDistrictCode();
        DistrictWithNameResponse districtResponse = regionService.getDistrictWithNameResponse(districtCode);
        response.setDistrict(districtResponse);

        return response;
    }

    private AdminUpdatePickUpOrderResponse convertToAdminUpdatePickUpOrderResponse(PickUpOrder newOrder,
                                                                                   Hub hub,
                                                                                   Staff staff) {
        AdminUpdatePickUpOrderResponse orderResponse = mapper.toAdminUpdateOrderResponse(newOrder);

        HubNameAndIdResponse hubResponse = hubService.convertHubToHubNameIdResponse(hub);

        StaffIdNameResponse staffResponse = staffService.convertStaffToStaffNameIdResponse(staff);

        String districtCode = newOrder.getDistrictCode();
        DistrictWithNameResponse districtResponse = regionService.getDistrictWithNameResponse(districtCode);
        orderResponse.setDistrict(districtResponse);

        orderResponse.setHub(hubResponse);
        orderResponse.setStaff(staffResponse);

        return orderResponse;
    }
}
