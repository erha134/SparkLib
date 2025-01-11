# SparkLib

A library Minecraft mod provides powerful tools.

Uses [Architectury Toolchain](https://github.com/architectury).

Wiki is coming soon.

| Minecraft version | Latest version | Fabric support | Forge support | NeoForge support | Git branch | Maintaining status |
|-------------------|----------------|----------------|---------------|------------------|------------|--------------------|
| 1.20-1.20.1       | 1.0.0-beta.1   | ✅              | ✅             | ❌                | 1.20       | ✅                  |

## Dependencies

| Dependencies     | Version | Fabric | (Neo)Forge |
|------------------|---------|--------|------------|
| Fabric API       | Any     | ✅      | ❌          |
| Architectury API | Any     | ✅      | ✅          |

## Features

- [`WrappedDeferredRegister`](https://github.com/erha134/SparkLib/blob/1.20/common/src/main/java/io/github/erha134/mc/sparklib/registry/api/WrappedDeferredRegister.java) API: Wrap a `DeferredRegister` and add more features.
- [`Registrable`](https://github.com/erha134/SparkLib/blob/1.20/common/src/main/java/io/github/erha134/mc/sparklib/registration/api/Registrable.java) API: Get `RegistryEntry`s from items, blocks, etc.
- Shield API: Make your own shields in Minecraft.
- Basic classes

## Todos

### High Priority

- [ ] Config system and auto-generated in-game config screens
- [ ] Version checker
- [ ] Network API
- [ ] Annotation-driven register

### Low Priority

- [ ] Bucket with Mob
- [ ] Data Generation API
- [ ] Fluid API
- [ ] Energy API
- [ ] Transfer API

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
