package com.xltrs.multilinguallib;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.neoforged.fml.ModList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

public class MultilingualService {
    private static final Gson Gson = new Gson();
    private static final Map<String, Map<String, String>> MultilingualCache = new HashMap<>();

    //注册多语言
    public static void Register(String client) {
        Debug.show("[MultilingualLib] Register %s mod multilingual service", client);
        //因为客户端模组是不知道自己jar路径的，为了给开发者省事就自己查询了awa，输个modid不难吧？
        try (ZipFile ClientJar = new ZipFile(ModList.get().getModFileById(client).getFile().getFilePath().toFile())) {
            String ClientEntry = "assets/" + client + "/lang/";
            //找到所有的json并解析
            ClientJar.stream()
                    .filter(e -> !e.isDirectory())
                    .filter(e -> e.getName().startsWith(ClientEntry))
                    .filter(e -> e.getName().endsWith(".json"))
                    .forEach(e -> {
                                try {
                                    String Language = e.getName().replace(ClientEntry, "").replace(".json", "");
                                    String Json = new String(ClientJar.getInputStream(e).readAllBytes(), StandardCharsets.UTF_8);
                                    Map<String, String> Key = Gson.fromJson(Json, new TypeToken<Map<String, String>>() {
                                    }.getType());
                                    MultilingualCache.computeIfAbsent(Language, k -> new HashMap<>()).putAll(Key);
                                } catch (Exception ex) {
                                    Debug.show("[MultilingualLib] Register %s mod multilingual service failed", client);
                                    ex.printStackTrace();
                                }
                            }
                    );
        } catch (IOException e) {
            Debug.show("[MultilingualLib] Register %s mod multilingual service failed", client);
            e.printStackTrace();
        }
    }

    public static String GetKey(String Key, String Language) {
        if (MultilingualCache.containsKey(Language)) {
            if (MultilingualCache.get(Language).containsKey(Key)) {
                return MultilingualCache.get(Language).get(Key);
            }
        }
        if (MultilingualCache.containsKey("en_us")) {
            if (MultilingualCache.get("en_us").containsKey(Key)) {
                return MultilingualCache.get("en_us").get(Key);
            }
        }
        return Key;
    }
}

