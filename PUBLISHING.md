# Publishing Guide

This document explains how to publish the KMP File Cache library to Maven Central using the official `gradle-nexus-publish-plugin`.

## Prerequisites

**⚠️ IMPORTANT: Read [SETUP_GUIDE.md](SETUP_GUIDE.md) first!**

You need to complete these steps before publishing:
1. Create Sonatype OSSRH account and request repository access
2. Generate GPG key for signing
3. Configure credentials in `gradle.properties`

## Quick Start

### 1. Configure Credentials

Add to `~/.gradle/gradle.properties`:

```properties
# Sonatype OSSRH
ossrhUsername=your-sonatype-jira-username
ossrhPassword=your-sonatype-jira-password

# GPG Signing
signing.keyId=YOUR_GPG_KEY_ID
signing.password=your-gpg-passphrase
signing.secretKey=-----BEGIN PGP PRIVATE KEY BLOCK-----
... (your full private key)
-----END PGP PRIVATE KEY BLOCK-----
```

### 2. Publish

```bash
# Test locally first
./gradlew :shared:publishToMavenLocal

# Publish to Maven Central (staging)
./gradlew :shared:publishToSonatype

# Automatically close and release (if configured)
./gradlew :shared:closeAndReleaseSonatypeStagingRepository
```

The plugin automatically:
- ✅ Signs all artifacts with GPG
- ✅ Uploads to Sonatype staging
- ✅ Closes the staging repository
- ✅ Releases to Maven Central (if auto-release is enabled)

## Available Tasks

- `publishToMavenLocal` - Publish to local Maven repository for testing
- `publishToSonatype` - Publish to Sonatype staging repository
- `closeAndReleaseSonatypeStagingRepository` - Close and release staging repo
- `closeSonatypeStagingRepository` - Close staging repo (manual release)
- `releaseSonatypeStagingRepository` - Release already closed staging repo

## Version Management

Update the version in `shared/build.gradle.kts`:
```kotlin
version = "1.0.0"  // Update this for each release
```

Follow semantic versioning:
- **MAJOR**: Breaking changes (1.0.0 → 2.0.0)
- **MINOR**: New features, backward compatible (1.0.0 → 1.1.0)
- **PATCH**: Bug fixes, backward compatible (1.0.0 → 1.0.1)

## Release Checklist

- [ ] Read [SETUP_GUIDE.md](SETUP_GUIDE.md) and complete all prerequisites
- [ ] Update version number in `shared/build.gradle.kts`
- [ ] Update `CHANGELOG.md` with new version
- [ ] Run tests: `./gradlew :shared:test`
- [ ] Build and verify locally: `./gradlew :shared:publishToMavenLocal`
- [ ] Publish to staging: `./gradlew :shared:publishToSonatype`
- [ ] Verify on Sonatype dashboard: https://s01.oss.sonatype.org/
- [ ] Release staging repository (auto or manual)
- [ ] Wait 10-30 minutes for sync to Maven Central
- [ ] Verify on Maven Central: https://repo1.maven.org/maven2/com/raystatic/filecache/
- [ ] Create Git tag: `git tag v1.0.0 && git push origin v1.0.0`
- [ ] Update documentation

## Troubleshooting

See [SETUP_GUIDE.md](SETUP_GUIDE.md) for detailed troubleshooting steps.

Common issues:
- **Authentication failed**: Check Sonatype credentials
- **GPG signing failed**: Verify key ID and passphrase
- **Group ID not verified**: Complete Sonatype ticket approval process

