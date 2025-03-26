package ru.planet.gpt.cache;

import ru.tinkoff.kora.cache.annotation.Cache;
import ru.tinkoff.kora.cache.caffeine.CaffeineCache;

@Cache("cache.caffeine.queryCache")
public interface QueryCache extends CaffeineCache<String, String> {
}
