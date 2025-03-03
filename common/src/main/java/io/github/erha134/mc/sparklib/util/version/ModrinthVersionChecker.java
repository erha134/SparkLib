package io.github.erha134.mc.sparklib.util.version;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import io.github.erha134.easylib.string.StringFormatter;
import io.github.erha134.mc.sparklib.util.SPlatform;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.maven.artifact.versioning.ComparableVersion;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class ModrinthVersionChecker {
    private static final Gson GSON = new Gson();

    public static CompletableFuture<CheckerContext> check(String modId, String idOrSlug) {
        return CompletableFuture.supplyAsync(() -> ModrinthVersionChecker.doCheck(modId, idOrSlug));
    }

    private static CheckerContext doCheck(String modId, String idOrSlug) {
        String url = StringFormatter.format("https://api.modrinth.com/v2/project/{}/version?loaders=[\"{}\"]&game_versions=[\"{}\"]",
                        idOrSlug, SPlatform.getLoaderType(), Platform.getMinecraftVersion())
                .replace("\"", "%22");
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            String body = EntityUtils.toString(response.getEntity());
            JsonArray array = GSON.fromJson(body, JsonElement.class).getAsJsonArray();
            JsonObject object = array.get(0).getAsJsonObject();
            String targetVersion = object.get("version_number").getAsString();
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
