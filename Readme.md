# SparkLib

A library Minecraft mod provides powerful tools.

Uses [Architectury Toolchain](https://github.com/architectury).

Wiki is coming soon.

| Minecraft version | Latest version | Fabric support | Forge support | NeoForge support | Git branch | Maintaining status |
|-------------------|----------------|----------------|---------------|------------------|------------|--------------------|
| 1.20.5-1.20.6     | 1.0.2          | ✅              | ❌             | ✅                | 1.20.6     | ✅                  |
| 1.20.2-1.20.4     | 1.0.2          | ✅              | ✅             | ✅                | 1.20.4     | ✅                  |
| 1.20-1.20.1       | 1.1.0-beta.2   | ✅              | ✅             | ❌                | 1.20       | ✅                  |
| 1.19-1.19.2       | 1.0.2          | ✅              | ✅             | ❌                | 1.19       | ✅                  |
| 1.18-1.19.2       | 1.0.2          | ✅              | ✅             | ❌                | 1.18       | ✅                  |

## Dependencies

| Dependencies     | Version | Fabric | (Neo)Forge |
|------------------|---------|--------|------------|
| Fabric API       | Any     | ✅      | ❌          |
| Architectury API | Any     | ✅      | ✅          |

## Features

- Basic classes
- Version checker
- Data generation API (by `SDataGeneration`)
  - Custom `DataProvider`(s) (based on [Fabric Data Generation Api](https://github.com/FabricMC/fabric/tree/1.20.1/fabric-data-generation-api-v1), under Apache-2.0 License)
- `DeferredRegisterManager`
- `SRegistrar` API (A more user-friendly alternative to `DeferredRegister`)
  - Create custom registry

## Todos

### High Priority

- [x] Config API (by [Spark Config](https://github.com/erha134/SparkConfig))
  - [ ] In-game config screens

### Low Priority

- [ ] Bucket with Mob
- [ ] Fluid API
- [ ] Transfer API
- [ ] Energy API
- [ ] Better BiomeModifications API (refer to [Architectury API](https://github.com/architectury/architectury-api/blob/1.20/common/src/main/java/dev/architectury/registry/level/biome/BiomeModifications.java))
- [ ] Lookup API

### No Plan

## Maven

[![](https://jitpack.io/v/erha134/SparkLib.svg)](https://jitpack.io/#erha134/SparkLib)

We use [jitpack](https://jitpack.io) to publish our library.

Artifact ID Table:

| Loader   | Artifact ID         |
|----------|---------------------|
| Common   | `sparklib-common`   |
| Fabric   | `sparklib-fabric`   |
| Forge    | `sparklib-forge`    |
| NeoForge | `sparklib-neoforge` |

## License

This project is licensed under the Apache 2.0 License.
