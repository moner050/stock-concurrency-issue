package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        // update 를 실패했을 때 재시도를 해야 하니 while 문으로 감쌈
        while(true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;
            }
            catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
