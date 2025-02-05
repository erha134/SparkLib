package io.github.erha134.mc.sparklib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.erha134.sparkconfig.impl.GsonConfigApi;
import net.minecraft.util.Identifier;

import java.lang.reflect.Modifier;

public class SGsonConfigApi extends GsonConfigApi {
    private static final Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .registerTypeAdapter(Identifier.class, new Identifier.Serializer())
            .setPrettyPrinting()
            .create();

    @Override
    public <T> T readInternal(Class<T> clazz, String config) {
        return gson.fromJson(config, clazz);
    }

    @Override
    public <T> String write(T object) {
        return gson.toJson(object);
    }
}
