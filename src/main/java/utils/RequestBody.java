package utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestBody {
    private String objectClass;
    private Object objectInstance;

    public RequestBody(Class objectClass, Object objectInstance) {
        this.objectClass = objectClass.getName();
        this.objectInstance = objectInstance;
    }

    public String getBodyString() {
        if (objectInstance.getClass() == String.class)
            return objectInstance.toString();
        return TestHelper.getJsonString(objectInstance);
    }

    public RequestBody(String name, String value) {
        objectClass = name;
        objectInstance = value;

    }

    public RequestBody(String name, int value) {
        objectClass = name;
        objectInstance = value;
    }

    public RequestBody(String name, float value) {
        objectClass = name;
        objectInstance = value;
    }
}
