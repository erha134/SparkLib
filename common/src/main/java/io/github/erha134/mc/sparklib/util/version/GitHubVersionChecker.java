package io.github.erha134.mc.sparklib.util.version;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.architectury.platform.Platform;
import io.github.erha134.easylib.string.StringFormatter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.maven.artifact.versioning.ComparableVersion;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class GitHubVersionChecker {
    private static final Gson GSON = new Gson();

    public static CompletableFuture<CheckerContext> check(String modId, String owner, String repo) {
        return CompletableFuture.supplyAsync(() -> GitHubVersionChecker.doCheck(modId, owner, repo));
    }

    private static CheckerContext doCheck(String modId, String owner, String repo) {
        String url = StringFormatter.format("https://api.github.com/repos/{}/{}/tags", owner, repo);
        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "application/vnd.github+json");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(get)) {
            String body = EntityUtils.toString(response.getEntity());
            JsonArray array = GSON.fromJson(body, JsonElement.class).getAsJsonArray();
            String targetVersion = array.asList()
                    .stream()
                    .filter(e -> e.getAsJsonObject()
                            .get("name")
                            .getAsString()
                            .startsWith(Platform.getMinecraftVersion()))
                    .findFirst()
                    .map(JsonElement::getAsString)
                    .orElseThrow();
            ComparableVersion target = new ComparableVersion(targetVersion);
            ComparableVersion current = new ComparableVersion(Platform.getMod(modId).getVersion());

            int i = current.compareTo(target);
            if (i < 0) {
                return CheckerContext.ofVersion(Status.OUTDATED, targetVersion);
            } else if (i > 0) {
                return CheckerContext.ofVersion(Status.AHEAD, targetVersion);
            } else {
                return CheckerContext.ofVersion(Status.LATEST, targetVersion);
            }
        } catch (IOException e) {
            return CheckerContext.ofMessage(Status.FAILED, e.getMessage());
        }
    }
}
