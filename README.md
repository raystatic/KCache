# KMP File Cache

A Kotlin Multiplatform (KMP) file-based cache library with LRU (Least Recently Used) eviction and TTL (Time-To-Live) support.

## Features

- ✅ **Multiplatform Support**: Android, iOS, and JVM
- ✅ **LRU Eviction**: Automatically evicts least recently used entries when cache size limit is reached
- ✅ **TTL Support**: Time-to-live expiration for cached entries
- ✅ **Persistent Storage**: Cache persists across app restarts
- ✅ **Thread-Safe**: Safe for concurrent access

## Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    // or your Maven repository
}

dependencies {
    implementation("com.raystatic.filecache:shared:1.0.0")
}
```

**Note:** The artifact name is `shared` (the module name). If you're publishing to Maven Central, you may want to rename the module to match your preferred artifact name.

## Usage

### Android

```kotlin
import com.raystatic.filecache.FileCacheInit
import com.raystatic.filecache.FileCacheFactory
import kotlin.time.Duration

// Initialize (typically in Application.onCreate())
FileCacheInit(context)

// Create cache instance
val cache = FileCacheFactory.create(
    maxSizeMB = 50,  // Maximum cache size in MB
    defaultTTL = Duration.parse("1h")  // Default TTL
)

// Use the cache
lifecycleScope.launch {
    // Put data
    cache.put("key1", "Hello World".encodeToByteArray())
    
    // Get data
    val data = cache.get("key1")
    
    // Remove specific key
    cache.remove("key1")
    
    // Clear all cache
    cache.clear()
}
```

### iOS

```swift
import shared

// Create cache instance
let cache = FileCacheFactory.Companion.shared.create(
    maxSizeMB: 50,
    defaultTTL: Duration.Companion.shared.parse("1h")
)

// Use the cache
Task {
    // Put data
    try await cache.put(key: "key1", data: "Hello World".data(using: .utf8)!)
    
    // Get data
    let data = try await cache.get(key: "key1")
    
    // Remove specific key
    try await cache.remove(key: "key1")
    
    // Clear all cache
    try await cache.clear()
}
```

### JVM

```kotlin
import com.raystatic.filecache.FileCacheFactory
import kotlin.time.Duration

// Create cache instance
val cache = FileCacheFactory.create(
    maxSizeMB = 100,
    defaultTTL = Duration.parse("30m")
)

// Use the cache
runBlocking {
    // Put data
    cache.put("key1", "Hello World".encodeToByteArray())
    
    // Get data
    val data = cache.get("key1")
    
    // Remove specific key
    cache.remove("key1")
    
    // Clear all cache
    cache.clear()
}
```

## API

### FileCache Interface

```kotlin
interface FileCache {
    suspend fun put(key: String, data: ByteArray, ttl: Duration? = null)
    suspend fun get(key: String): ByteArray?
    suspend fun remove(key: String)
    suspend fun clear()
}
```

### Parameters

- `maxSizeMB`: Maximum cache size in megabytes (default: 20 MB)
- `defaultTTL`: Default time-to-live for entries (default: 30 minutes)
- `ttl`: Optional per-entry TTL override in `put()` method

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Publishing

This library uses the official `gradle-nexus-publish-plugin` for publishing to Maven Central.

**Before publishing, you must:**
1. Read [SETUP_GUIDE.md](SETUP_GUIDE.md) - Complete guide for setting up accounts and credentials
2. Read [PUBLISHING.md](PUBLISHING.md) - Quick reference for publishing commands

**Quick start:**
```bash
# Configure credentials in ~/.gradle/gradle.properties first!
./gradlew :shared:publishToSonatype
```

