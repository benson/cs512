import java.io.Serializable;

public class MethodCall implements Serializable {
    public int method;
    public Vector arguments;

    public static MethodCall(int method, Vector arguments) {
        this.method = method;
        this.arguments = arguments;
    }
}
