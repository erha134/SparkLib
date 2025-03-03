package io.github.erha134.mc.sparklib.util.version;

import com.mojang.datafixers.util.Either;
import lombok.Getter;

@Getter
public final class CheckerContext {
    private final Status status;
    private final Either<String, String> either;

    private CheckerContext(Status status, Either<String, String> either) {
        this.status = status;
        this.either = either;
    }

    static CheckerContext ofVersion(Status status, String version) {
        return new CheckerContext(status, Either.left(version));
    }

    static CheckerContext ofMessage(Status status, String message) {
        return new CheckerContext(status, Either.right(message));
    }
}
