package main;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by FLisochenko on 18.07.2016.
 */
public final class SessionIdentifierGenerator {
    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
