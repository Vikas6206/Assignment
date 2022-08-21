package utils;

public class Param {
    private String Key;

    private String Value;

    public Param(String key, String value) {
        Key = key;
        Value = value;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @Override
    public Param clone() {
        return new Param(this.getKey(),this.getValue());
    }
}
