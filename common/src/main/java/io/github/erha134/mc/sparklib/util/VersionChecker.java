package io.github.erha134.mc.sparklib.util;

import com.google.gson.Gson;
import dev.architectury.platform.Platform;
import lombok.Getter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class VersionChecker {
    public enum Status {
        PENDING,
        FAILED,
        UP_TO_DATE,
        OUTDATED,
        AHEAD,
        BETA,
        BETA_OUTDATED
    }

    @Getter
    public static final class CheckContext {
        private Status status;
        private final String current;
        private String recommended;
        private String latest;

        public CheckContext(Status status, String current, String recommended, String latest) {
            this.status = status;
            this.current = current;
            this.recommended = recommended;
            this.latest = latest;
        }

        public CheckContext(Status status, String current) {
            this(status, current, null, null);
        }

        public boolean hasRecommendedVersion() {
            return this.recommended != null && !this.recommended.isEmpty();
        }

        public boolean hasLatestVersion() {
            return this.latest != null && !this.latest.isEmpty();
        }
    }

    public static CompletableFuture<CheckContext> check(String modId, String url) {
        String version = Platform.getMod(modId).getVersion();
        return CompletableFuture.supplyAsync(() -> doCheck(version, url));
    }

    @ApiStatus.Internal
    public static CheckContext doCheck(String version, String url) {
        CheckContext context = new CheckContext(Status.PENDING, version);
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(new HttpGet(url))) {

            String body = EntityUtils.toString(response.getEntity());
            Map<String, Object> json = new Gson().fromJson(body, Map.class);

            Map<String, String> promos = (Map<String, String>) json.get("promos");

            String mcVersion = Platform.getMinecraftVersion();
            String l = promos.get(mcVersion + "-latest");
            String r = promos.get(mcVersion + "-recommended");

            ComparableVersion current = new ComparableVersion(version);

            if (r != null) {
                ComparableVersion recommended = new ComparableVersion(r);

                int i = recommended.compareTo(current);
                if (i == 0) {
                    context.status = Status.UP_TO_DATE;
                } else if (i < 0) {
                    context.status = Status.AHEAD;

                    if (l != null) {
                        ComparableVersion latest = new ComparableVersion(l);
                        if (current.compareTo(latest) < 0) {
                            context.status = Status.OUTDATED;
                        }

                        context.latest = l;
                    }
                } else {
                    context.status = Status.OUTDATED;
                }

                context.recommended = r;
            } else if (l != null) {
                ComparableVersion latest = new ComparableVersion(l);

                if (current.compareTo(latest) < 0) {
                    context.status = Status.BETA_OUTDATED;
                } else {
                    context.status = Status.BETA;
                }

                context.latest = l;
            }
        } catch (IOException ignored) {
            context.status = Status.FAILED;
        }

        return context;
    }
}
