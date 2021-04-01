package customcraft;

import java.util.Base64;

public class Tools {

    private Tools() {

    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        return Base64.getDecoder().decode(hexString);
    }

    public static String bytesToHexString(byte[] src) {
        if (src == null || src.length <= 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(src);
    }

}
