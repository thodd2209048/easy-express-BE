package com.example.easyexpressbackend.domain.statistic;

import com.example.easyexpressbackend.domain.pickUpOrder.PickUpOrderRepository;
import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.utils.Utils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class PickUpOrderStatisticService {
    private final PickUpOrderStatisticRepository repository;
    private final PickUpOrderStatisticMapper mapper;
    private final PickUpOrderRepository pickUpOrderRepository;

    public PickUpOrderStatisticService(PickUpOrderStatisticRepository repository,
                                       PickUpOrderStatisticMapper mapper,
                                       PickUpOrderRepository pickUpOrderRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.pickUpOrderRepository = pickUpOrderRepository;
    }

    @Scheduled(cron = "0 */3 8-21 * * *", zone = "Asia/Ho_Chi_Minh")
    public void statisticPickUpOrder() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        ZonedDateTime momentStartOfDate = Utils.getStartOfDate(now);
        ZonedDateTime momentEndOfDate = Utils.getEndOfDate(now);

        Long unfinishedOrdersCount = pickUpOrderRepository.countByStatusAndStartTimeBetween(
                PickUpOrderStatus.ASSIGNED_TO_HUB,
                momentStartOfDate,
                momentEndOfDate
        );

        Long inProcessOrdersCount = pickUpOrderRepository.countByStatusAndStartTimeBetweenAndStartTimeBeforeAndEndTimeAfter(
                PickUpOrderStatus.ASSIGNED_TO_HUB,
                momentStartOfDate,
                momentEndOfDate,
                now,
                now
        );

        Long missedDeadlineOrdersCount = pickUpOrderRepository.countByStatusAndStartTimeBetweenAndEndTimeBefore(
                PickUpOrderStatus.ASSIGNED_TO_HUB,
                momentStartOfDate,
                momentEndOfDate,
                now
        );

        PickUpOrderStatistic pickUpOrderStatistic = PickUpOrderStatistic.builder()
                .unfinishedOrder(unfinishedOrdersCount)
                .inProcessOrder(inProcessOrdersCount)
                .missedDeadlineOrder(missedDeadlineOrdersCount)
                .build();

        repository.save(pickUpOrderStatistic);
    }

    public PickUpOrderStatisticResponse getNewestStatistic() {
        ZonedDateTime momentAtStartOfDate = Utils.getStartOfDate(ZonedDateTime.now());
        ZonedDateTime momentAtEndOfDate = Utils.getEndOfDate(ZonedDateTime.now());

        PickUpOrderStatistic pickUpOrderStatistic = repository.findFirstByCreatedAtBetweenOrderByCreatedAtDesc(
                momentAtStartOfDate,
                momentAtEndOfDate)
                .orElse(new PickUpOrderStatistic(0L,0L,0L));
        return mapper.toPickUpOrderStatisticResponse(pickUpOrderStatistic);
    }
}
