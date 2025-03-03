package io.github.erha134.mc.sparklib.util.version;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.maven.artifact.versioning.ComparableVersion;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class ForgeVersionChecker {
    private static final Gson GSON = new Gson();

    public static CompletableFuture<CheckerContext> check(String modId) {
        return CompletableFuture.supplyAsync(() -> ForgeVersionChecker.doCheck(modId));
    }

    private static CheckerContext doCheck(String modId) {
        Optional<String> optional = getUpdateUrl(modId);
        if (optional.isEmpty()) {
            return CheckerContext.ofMessage(Status.FAILED, "No update url found for mod " + modId);
        }

        String updateUrl = optional.get();
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(new HttpGet(updateUrl))) {
            String body = EntityUtils.toString(response.getEntity());
            JsonObject root = GSON.fromJson(body, JsonElement.class).getAsJsonObject();
            JsonObject promos = root.get("promos").getAsJsonObject();

            Map<String, String> map = promos.entrySet()
                    .stream()
                    .filter(e -> e.getKey().endsWith("-latest"))
                    .collect(Collectors.toMap(e -> e.getKey().substring(0, e.getKey().indexOf('-')),
                            e -> e.getValue().getAsString()));

            String targetVersion = map.get(Platform.getMinecraftVersion());
            ComparableVersion target = new ComparableVersion(targetVersion);
            ComparableVersion current = new ComparableVersion(Platform.getMod(modId).getVersion());

            int i = current.compareTo(target);
            if (i < 0) {
                return CheckerContext.ofVersion(Status.OUTDATED, (targetVersion));
            } else if (i > 0) {
                return CheckerContext.ofVersion(Status.AHEAD, targetVersion);
            } else {
                return CheckerContext.ofVersion(Status.LATEST, targetVersion);
            }
        } catch (IOException e) {
            return CheckerContext.ofMessage(Status.FAILED, e.getMessage());
        }
    }

    @ExpectPlatform
    private static Optional<String> getUpdateUrl(String modId) {
        throw new AssertionError();
    }
}
