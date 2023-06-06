package gg.kazerspigot.knockback;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public class KnockBackProfile {
  public String title;
  
  public List<KnockBackValue> values = new ArrayList<>();
  
  public KnockBackValue<Double> frictionH = new KnockBackValue<>("frictionH", "Friction Horizontal", Double.class, 100.0D);
  
  public KnockBackValue<Double> frictionV = new KnockBackValue<>("frictionV", "Friction Vertical", Double.class, 100.0D);
  
  public KnockBackValue<Double> vlimit = new KnockBackValue<>("vlimit", "Vertical Limit", Double.class, 0.34D);
  
  public KnockBackValue<Double> sprintFrictionH = new KnockBackValue<>("sprintFrictionH", "Sprint Friction Horizontal", Double.class, 100.0D);
  
  public KnockBackValue<Double> sprintFrictionV = new KnockBackValue<>("sprintFrictionV", "Sprint Friction Vertical", Double.class, 100.0D);
  
  public KnockBackValue<Double> sprintVlimit = new KnockBackValue<>("sprintVlimit", "Sprint Vertical Limit", Double.class, 0.34D);
  
  public KnockBackValue<Double> horizontal = new KnockBackValue<>("horizontal", "Horizontal", Double.class, 0.58D);
  
  public KnockBackValue<Double> vertical = new KnockBackValue<>("vertical", "Vertical", Double.class, 0.34D);
  
  public KnockBackValue<Double> extraH = new KnockBackValue<>("extraH", "Extra Horizontal", Double.class, 0.252D);
  
  public KnockBackValue<Double> extraV = new KnockBackValue<>("extraV", "Extra Vertical", Double.class, 0.0165D);
  
  public KnockBackValue<Double> sprintHorizontal = new KnockBackValue<>("sprintH", "Sprint Horizontal", Double.class, 0.58D);
  
  public KnockBackValue<Double> sprintVertical = new KnockBackValue<>("sprintV", "Sprint Vertical", Double.class, 0.34D);
  
  public KnockBackValue<Double> sprintExtraH = new KnockBackValue<>("sprintExtraH", "Sprint Extra Horizontal", Double.class, 0.252D);
  
  public KnockBackValue<Double> sprintExtraV = new KnockBackValue<>("sprintExtraV", "Sprint Extra Vertical", Double.class, 0.0165D);
  
  public KnockBackValue<Boolean> sprint = new KnockBackValue<>("sprint", "Sprint", Boolean.class, Boolean.FALSE);
  
  public KnockBackValue<Boolean> doubleDamage = new KnockBackValue<>("doubledamage", "Double Damage", Boolean.class, Boolean.FALSE);
  
  public KnockBackValue<Double> rodH = new KnockBackValue<>("rodh", "Rod Horizontal Multiplier", Double.class, 1.02D);
  
  public KnockBackValue<Double> rodV = new KnockBackValue<>("rodv", "Rod Vertical Multiplier", Double.class, 1.02D);
  
  public KnockBackValue<Double> rodSpeed = new KnockBackValue<>("rodSpeed", "Rod Speed Multiplier", Double.class, 1.02D);
  
  public KnockBackValue<Double> bowH = new KnockBackValue<>("bowh", "Bow Horizontal Multiplier", Double.class, 1.02D);
  
  public KnockBackValue<Double> bowV = new KnockBackValue<>("bowv", "Bow Vertical Multiplier", Double.class, 1.02D);
  
  public KnockBackValue<Double> slowDown = new KnockBackValue<>("slowdown", "Slow Down", Double.class, 0.6D);
  
  public KnockBackValue<Integer> sprintTicks = new KnockBackValue<>("sprintTicks", "Sprint Ticks", Integer.class, 1);
  
  public KnockBackValue<Double> potionFallSpeed = new KnockBackValue<>("potionfall", "Potion Fall Speed", Double.class, 0.05D);
  
  public KnockBackValue<Double> potionThrowMultiplier = new KnockBackValue<>("potionmultiplier", "Potion Multiplier", Double.class, 0.5D);
  
  public KnockBackValue<Double> potionThrowOffset = new KnockBackValue<>("potionoffset", "Potion Throw Offset", Double.class, -10.0D);
  
  public KnockBackValue<Integer> potionPlayerSpeed = new KnockBackValue<>("potionplayerspeed", "Potion Player Speed", Integer.class, 5);
  
  public KnockBackValue<Double> potionDistanceRadius = new KnockBackValue<>("potiondistanceradius", "Potion Distance Radius", Double.class, 0.16D);
  
  public KnockBackValue<Boolean> smoothPotting = new KnockBackValue<>("smothpotting", "Smoth Potting", Boolean.class, Boolean.TRUE);
  public KnockBackValue<Boolean> comboMode = new KnockBackValue<>("combomode", "Combo Mode", Boolean.class, Boolean.FALSE);
  public KnockBackValue<Integer> comboTick = new KnockBackValue<>("combotick", "Combo Tick", Integer.class, 5);

  public KnockBackProfile(String title) {
    this.title = title;
    load();
  }
  
  public void load() {
    try {
      this.values.clear();
      byte b;
      int i;
      Field[] arrayOfField;
      for (i = (arrayOfField = getClass().getFields()).length, b = 0; b < i; ) {
        Field f = arrayOfField[b];
        if (f.getType() == KnockBackValue.class) {
          this.values.add((KnockBackValue)f.get(this));
        }
        b++;
      } 
      File file = new File("knockback" + File.separator + this.title + ".yml");
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        file.createNewFile();
      } 
      YamlConfiguration config = new YamlConfiguration();
      config.load(file);
      for (KnockBackValue value : this.values) {
        Object val = config.get(value.id);
        if (val == null) {
          config.set(value.id, value.value);
          continue;
        } 
        value.value = val; // Todo (T)
      } 
      config.save(file);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void save() {
    try {
      File file = new File("knockback" + File.separator + this.title + ".yml");
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        file.createNewFile();
      } 
      YamlConfiguration config = new YamlConfiguration();
      for (KnockBackValue value : this.values) {
        config.set(value.id, value.value);
      }
      config.save(file);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
