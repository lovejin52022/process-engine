package com.zzjj.process.engine.store;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 缓存流程状态处理器
 *
 * @author zengjin
 * @date 2023/12/09 00:22
 **/
@Slf4j
public class CacheProcessStateStore implements ProcessStateStore {
    private final ProcessRefreshPolicy processRefreshPolicy;

    private final Map<String, Map<String, String>> cacheMap = new ConcurrentHashMap<>(16);
    private final Map<String, Long> sortedProcess = new ConcurrentHashMap<>(16);
    private final Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>(16);

    public CacheProcessStateStore() {
        this(new AbortProcessRefreshPolicy());
    }


    public CacheProcessStateStore(ProcessRefreshPolicy processRefreshPolicy) {

        this.processRefreshPolicy = processRefreshPolicy;
    }

    @Override
    public void recordProcessMetadata(String name, Map<String, String> metadata) {
        log.debug("recordProcessMetadata name={}, metadata={}", name, metadata);
        // 记录对应流程的元数据
        this.cacheMap.put(name, metadata);

        // 添加开始时间
        LocalDateTime time = LocalDateTime.now();
        long timeOfSecond = time.toEpochSecond(ZoneOffset.ofHours(8));
        this.sortedProcess.put(name, timeOfSecond);
    }

    @Override
    public void updateMetadata(String name, String key, String value) {
        log.debug("updateMetadata name={}, key={}, value={}", name, key, value);
        Map<String, String> map = this.cacheMap.get(name);
        map.put(key, value);


        // 更新时间
        this.sortedProcess.remove(name);
        LocalDateTime time = LocalDateTime.now();
        long timeOfSecond = time.toEpochSecond(ZoneOffset.ofHours(8));
        this.sortedProcess.put(name, timeOfSecond);
    }

    @Override
    public void clearMetadata(String name) {
        log.debug("clearMetadata name={}", name);
        this.cacheMap.remove(name);

        this.sortedProcess.remove(name);
    }

    @Override
    public boolean lock(String key) {
        // 获取公平锁
        ReentrantLock lock = this.lockMap.computeIfAbsent(key, v -> new ReentrantLock());
        return lock.tryLock();
    }

    @Override
    public void unlock(String key) {
        ReentrantLock fairLock = this.lockMap.get(key);
        if (fairLock.isLocked()) {
            fairLock.unlock();
        }
    }

    @Override
    public Collection<String> pollUnCompletedProcess(int timeout, TimeUnit unit, boolean refresh) {
        LocalDateTime time = LocalDateTime.now();
        long timeOfSecond = time.toEpochSecond(ZoneOffset.ofHours(8));
        long offset = unit.toSeconds(timeout);
        double endScore = timeOfSecond - offset;
        List<String> keys = this.sortedProcess.entrySet().stream().filter(s -> s.getValue() >= 0 && s.getValue() <= endScore).sorted((o1, o2) -> (int) (o1.getValue() - o2.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
        if (!refresh) {
            return keys;
        } else {
            Set<String> result = new HashSet<>();
            for (String key : keys) {
                if (this.processRefreshPolicy.continueExecuteProcess(key, this.getMap(key))) {
                    result.add(key);
                } else {
                    this.clearMetadata(key);
                }
            }
            return result;
        }
    }

    @Override
    public Map<String, String> getMap(String key) {
        return this.cacheMap.get(key);
    }
}
