package ru.planet.gpt.cache;

import ru.tinkoff.kora.cache.annotation.Cache;
import ru.tinkoff.kora.cache.caffeine.CaffeineCache;

@Cache("cache.caffeine.tokenCache")
public interface TokenCache extends CaffeineCache<Long, String> {
}
