package gg.kazerspigot.knockback;

public class KnockBackValue<T> {
  public String id;
  
  public String name;
  
  public Class type;
  
  public T value;
  
  public KnockBackValue(String id, String name, Class type, T value) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.value = value;
  }
}
