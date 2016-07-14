package metrics;

import java.io.*;

/**
 * Created by FLisochenko on 13.07.2016.
 */
public final class SerializeHelper {
    private SerializeHelper(){}

    public static Metrics deserialize(byte[] in) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(in);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        return (Metrics) obj;
    }

    public static byte[] serialize(Metrics obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        return bos.toByteArray();
    }
}
