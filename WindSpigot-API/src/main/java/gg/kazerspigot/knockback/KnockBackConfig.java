package gg.kazerspigot.knockback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KnockBackConfig {

  private static final KnockBackConfig instance = new KnockBackConfig();
  public static KnockBackConfig getInstance() {
    return instance;
  }

  public static Map<String, KnockBackProfile> profiles;

  public KnockBackConfig() {
    File knockback = new File("knockback");
    profiles = new HashMap<>();
    if (!knockback.exists()) {
      knockback.mkdir();
    }
    File[] files = knockback.listFiles();
    if (files != null) {
      byte b;
      int i;
      File[] arrayOfFile;
      for (i = (arrayOfFile = files).length, b = 0; b < i; ) {
        File file = arrayOfFile[b];
        profiles.put(file.getName().replace(".yml", ""), new KnockBackProfile(file.getName().replace(".yml", "")));
        b++;
      } 
    } 
    if (!profiles.containsKey("default")) {
      profiles.put("default", new KnockBackProfile("default"));
    }
  }
  
  public static KnockBackProfile getDefault() {
    return profiles.get("default");
  }
}
