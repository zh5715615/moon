# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build
./mvnw clean package -DskipTests

# Run (default profile: dev)
./mvnw spring-boot:run

# Run with a specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=tst

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=WatchApplicationTests
```

The app runs on **HTTPS port 8443** using a PKCS12 keystore (`src/main/resources/keystore.p12`). Active profile is set in `application.yml` (`spring.profiles.active`); profile-specific config lives in `application-dev.yml`, `application-tst.yml`, `application-pro.yml`.

## Architecture

Spring Boot 2.5.0 / Java 17 / Maven. The application name is `star-wars`. The domain is a blockchain-based game platform on BSC (Binance Smart Chain).

### Package structure (`tcbv.zhaohui.moon`)

| Package | Purpose |
|---|---|
| `controller/` | REST controllers, all under `/api/v1/moon/` |
| `service/` | Business logic interfaces + `impl/` |
| `service/chain/` | Blockchain interaction (Web3j wrappers) |
| `dao/` | MyBatis mapper interfaces |
| `entity/` | DB entities (mapped by MyBatis) |
| `dto/` | Request objects (inbound) |
| `vo/` | Response/view objects (outbound) |
| `beans/` | Internal data beans, including `events/` for ABI event decode results |
| `game/bullfighting/` | Poker game logic (`PokerDealer`, `Card`) |
| `jwt/` | JWT auth (`JwtUtil`, `JwtAddressInterceptor`, `@JwtAddressRequired`, `JwtContext`) |
| `tasks/` | `@Scheduled` tasks (reward sending, liquidity pool creation) |
| `oss/` | MinIO object storage integration |
| `syslog/` | AOP-based system operation logging (`@Syslog` annotation) |
| `config/` | Spring beans config (Web3, Swagger, CORS, AOP logging) |
| `exceptions/` | `BizException`, `ChainException`, `Web3TxGuard` |
| `enums/` | Domain enums (pledge region, NFT order status, presale stage) |

### Data layer

MyBatis with XML mappers in `src/main/resources/mapper/*Dao.xml`. Druid connection pool. PageHelper used for pagination. No JPA/Hibernate.

### Auth flow

JWT tokens carry `userId` and wallet `address` as claims. The `JwtAddressInterceptor` (registered for `/**`) checks for `@JwtAddressRequired` on handler methods and validates the `Authorization` header. Authenticated user context is available via `JwtContext.getUserId()` / `JwtContext.getAddress()` (thread-local).

### Blockchain integration

`EthereumService` is the central Web3j abstraction (connects to BSC). Contract-specific services (`Token20Service`, `DappPoolService`, `CardNFTTokenService`, `BullfightingGameSampleService`) wrap individual smart contracts. ABI event decoding uses `AbiEventLogDecoder` and `AbiInputDecoder`. Contract addresses and account credentials are configured per profile under the `web3:` config key.

### Key custom config namespace

`star-wars.*` in yml files controls: JWT secret/expiry, auth enable flag, OSS (MinIO) credentials, AES encryption key, promote reward percentage, and external service URL.
