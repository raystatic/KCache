# Setup Guide for Publishing to Maven Central

This guide walks you through all the accounts, credentials, and repositories you need to set up before publishing your library to Maven Central.

## Prerequisites Checklist

- [ ] Sonatype OSSRH Account
- [ ] GPG Key for Signing
- [ ] GitHub Repository (optional but recommended)
- [ ] Domain Verification (if using custom domain)

---

## Step 1: Create Sonatype OSSRH Account

### 1.1 Create Account
1. Go to https://issues.sonatype.org/
2. Click **Sign Up** and create an account
3. Verify your email address

### 1.2 Create a JIRA Ticket for Repository Access

1. Log in to https://issues.sonatype.org/
2. Click **Create** → **New Project** → **Community Support - Open Source Project Repository Hosting (OSSRH)**
3. Fill out the ticket with:
   - **Summary**: Create repository for com.raystatic.filecache
   - **Group Id**: `com.raystatic.filecache`
   - **Project URL**: `https://github.com/raystatic/kmp-file-cache` (or your actual URL)
   - **SCM URL**: `https://github.com/raystatic/kmp-file-cache.git` (or your actual URL)
   - **Already Synced to Central**: No

4. **Important**: You need to prove ownership of the domain `raystatic.com`:
   - Option A: If you own the domain, add a DNS TXT record
   - Option B: If you don't own the domain, you'll need to use a different group ID like:
     - `io.github.yourusername` (if using GitHub)
     - `com.github.yourusername`
     - Or create a GitHub organization

5. Wait for approval (usually 1-2 business days)

### 1.3 Get Your Credentials
Once approved, you'll use:
- **Username**: Your Sonatype JIRA username
- **Password**: Your Sonatype JIRA password

---

## Step 2: Generate GPG Key for Signing

Maven Central requires all artifacts to be signed with GPG.

### 2.1 Install GPG

**macOS:**
```bash
brew install gnupg
```

**Linux:**
```bash
sudo apt-get install gnupg
```

**Windows:**
Download from https://www.gnupg.org/download/

### 2.2 Generate GPG Key

```bash
gpg --full-generate-key
```

Follow the prompts:
1. **Kind of key**: RSA and RSA (default)
2. **Key size**: 4096
3. **Expiration**: 0 (no expiration) or set a date
4. **Real name**: Your name
5. **Email**: Your email (should match Sonatype account)
6. **Comment**: (optional)
7. **Passphrase**: Create a strong passphrase (you'll need this)

### 2.3 List Your Keys

```bash
gpg --list-secret-keys --keyid-format LONG
```

You'll see output like:
```
sec   rsa4096/ABC123DEF456 2024-01-01 [SC]
      ABC123DEF4567890ABCDEF1234567890ABCDEF12
uid                 [ultimate] Your Name <your.email@example.com>
```

**Note the key ID**: `ABC123DEF456` (the part after the slash)

### 2.4 Export Your Private Key

```bash
gpg --export-secret-keys --armor ABC123DEF456 > private-key.asc
```

**⚠️ SECURITY WARNING**: Keep this file secure! Never commit it to Git.

### 2.5 Publish Your Public Key

```bash
gpg --keyserver keyserver.ubuntu.com --send-keys ABC123DEF456
```

Also try:
```bash
gpg --keyserver keys.openpgp.org --send-keys ABC123DEF456
gpg --keyserver pgp.mit.edu --send-keys ABC123DEF456
```

Wait a few minutes, then verify:
```bash
gpg --keyserver keyserver.ubuntu.com --recv-keys ABC123DEF456
```

---

## Step 3: Create GitHub Repository (Recommended)

### 3.1 Create Repository
1. Go to https://github.com/new
2. Repository name: `kmp-file-cache` (or your preferred name)
3. Description: "Kotlin Multiplatform file-based cache library"
4. Visibility: Public (required for open source)
5. Initialize with README: Yes
6. Add .gitignore: Kotlin
7. License: Apache License 2.0

### 3.2 Update Build Files
Update the following in `shared/build.gradle.kts`:
- Replace `raystatic` with your GitHub username in SCM URLs
- Update repository URLs in POM

---

## Step 4: Configure Gradle Properties

### 4.1 Create/Locate gradle.properties

**Option A: Project-level** (for testing)
Create `gradle.properties` in the project root (⚠️ Don't commit this if it contains secrets)

**Option B: User-level** (recommended for production)
Create/edit `~/.gradle/gradle.properties` in your home directory

### 4.2 Add Credentials

```properties
# Sonatype OSSRH Credentials
ossrhUsername=your-sonatype-jira-username
ossrhPassword=your-sonatype-jira-password

# GPG Signing
signing.keyId=ABC123DEF456
signing.password=your-gpg-passphrase
signing.secretKey=-----BEGIN PGP PRIVATE KEY BLOCK-----
... (paste your entire private key from private-key.asc)
-----END PGP PRIVATE KEY BLOCK-----
```

**Important Notes:**
- `signing.keyId`: The key ID from Step 2.3 (without spaces)
- `signing.password`: Your GPG key passphrase
- `signing.secretKey`: The entire content of your `private-key.asc` file (including BEGIN/END lines)

---

## Step 5: Update Group ID (If Needed)

If you don't own `raystatic.com`, you have two options:

### Option A: Use GitHub-based Group ID
1. Update `shared/build.gradle.kts`:
   ```kotlin
   group = "io.github.yourusername"
   ```

2. Update `build.gradle.kts` namespace:
   ```kotlin
   namespace = "io.github.yourusername.filecache"
   ```

3. Update all package declarations in source files
4. Create a new Sonatype ticket with the new group ID

### Option B: Create GitHub Organization
1. Create a GitHub organization at https://github.com/organizations/new
2. Use the organization name in your group ID
3. Request access for the organization domain

---

## Step 6: Test Publishing

### 6.1 Test Locally
```bash
./gradlew :shared:publishToMavenLocal
```

### 6.2 Publish to Staging
```bash
./gradlew :shared:publishToSonatype
```

This will:
1. Build all artifacts
2. Sign them with GPG
3. Upload to Sonatype staging repository
4. Automatically close the staging repository
5. Automatically release (if configured)

### 6.3 Verify on Sonatype
1. Go to https://s01.oss.sonatype.org/
2. Log in with your Sonatype credentials
3. Check "Staging Repositories"
4. Find your repository (should be closed)
5. If everything looks good, click "Release"

---

## Step 7: Verify on Maven Central

After release:
1. Wait 10-30 minutes for sync
2. Check: https://repo1.maven.org/maven2/com/raystatic/filecache/
3. Your library should appear!

---

## Troubleshooting

### Issue: "Group ID not verified"
- Make sure you created the Sonatype ticket
- Verify domain ownership (DNS TXT record or GitHub)
- Wait for ticket approval

### Issue: "GPG signing failed"
- Verify key ID is correct (no spaces)
- Check passphrase is correct
- Ensure private key is properly formatted in gradle.properties

### Issue: "Authentication failed"
- Double-check Sonatype username/password
- Make sure you're using the correct Sonatype instance (s01.oss.sonatype.org)

### Issue: "Staging repository not found"
- Check Sonatype dashboard manually
- The plugin should auto-close, but you can do it manually if needed

---

## Quick Reference

### Required Accounts
- ✅ Sonatype OSSRH (https://issues.sonatype.org/)
- ✅ GPG Key (generated locally)
- ✅ GitHub (optional but recommended)

### Required Credentials
- `ossrhUsername`: Sonatype JIRA username
- `ossrhPassword`: Sonatype JIRA password
- `signing.keyId`: GPG key ID (8+ characters)
- `signing.password`: GPG key passphrase
- `signing.secretKey`: Full GPG private key

### Important URLs
- Sonatype Dashboard: https://s01.oss.sonatype.org/
- Maven Central: https://repo1.maven.org/maven2/
- GPG Key Servers: keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu

---

## Next Steps

Once everything is set up:
1. Update version in `shared/build.gradle.kts`
2. Update `CHANGELOG.md`
3. Run `./gradlew :shared:publishToSonatype`
4. Verify on Maven Central
5. Create a Git tag
6. Update documentation

Good luck! 🚀

