package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.service.impl.DefaultOrderService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final Long THRESHOLDS = 20L;
    private static final Long DELAY = 60000L;
    private class ThresholdPair {
        private Long lastThreshold;
        private Long count;

        public ThresholdPair(Long lastThreshold, Long count) {
            this.lastThreshold = lastThreshold;
            this.count = count;
        }

        public Long getLastThreshold() {
            return lastThreshold;
        }

        public void setLastThreshold(Long lastThreshold) {
            this.lastThreshold = lastThreshold;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }
    private Map<String, ThresholdPair> countMap = new ConcurrentHashMap<>();
    private static class SingletonHelper {
        private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }
    private DefaultDosProtectionService() {

    }
    public static DefaultDosProtectionService getInstance() {
        return SingletonHelper.INSTANCE;
    }
    @Override
    public boolean isAllowed(String ip) {
        ThresholdPair pair = countMap.get(ip);
        if (pair == null) {
            pair = new ThresholdPair(1L, System.currentTimeMillis());
            countMap.put(ip, pair);
        } else {
            if (System.currentTimeMillis() - pair.getLastThreshold() > DELAY) {
                pair.setCount(1L);
                pair.setLastThreshold(System.currentTimeMillis());
            } else {
                if (pair.getCount() > THRESHOLDS) {
                    return false;
                }
                pair.setCount(pair.getCount() + 1L);
            }
        }
        return true;
    }
}
