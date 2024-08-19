package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        // lock 을 획득하지 못하면 0.1 초 뒤에 다시 시도
        while(!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }

        // Lock 을 획득하면 재고를 감소시켜 주고 마지막에 unLock
        try {
            stockService.decrease(id, quantity);
        }
        finally {
            redisLockRepository.unlock(id);
        }
    }
}
