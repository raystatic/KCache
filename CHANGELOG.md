# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-XX-XX

### Added
- Initial release of KMP File Cache library
- Multiplatform support for Android, iOS, and JVM
- LRU (Least Recently Used) eviction strategy
- TTL (Time-To-Live) support for cache entries
- Persistent file-based storage
- Thread-safe operations
- Configurable cache size limits
- Per-entry TTL override support

### Features
- `FileCache` interface with `put`, `get`, `remove`, and `clear` operations
- Automatic index persistence across app restarts
- Platform-specific file storage implementations:
  - Android: Uses app's files directory
  - iOS: Uses NSCachesDirectory
  - JVM: Uses local file system

