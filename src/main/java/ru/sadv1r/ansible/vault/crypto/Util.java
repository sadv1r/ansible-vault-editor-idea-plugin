package ru.sadv1r.ansible.vault.crypto;

public class Util {

    private static final int DEFAULT_LINE_LENGTH = 80;
    private static final String LINE_BREAK = "\n";

    public static byte[] unhex(String hexed) {
        int dataLen = hexed.length();
        byte[] output = new byte[dataLen / 2];
        for (int charIdx = 0; charIdx < dataLen; charIdx += 2) {
            output[charIdx / 2] = (byte) ((Character.digit(hexed.charAt(charIdx), 16) << 4)
                    + Character.digit(hexed.charAt(charIdx + 1), 16));
        }
        return output;
    }

    public static String hexit(byte[] unhexed) {
        return hexit(unhexed, DEFAULT_LINE_LENGTH);
    }

    public static String hexit(byte[] unhexed, int lineLength) {
        StringBuilder result = new StringBuilder();
        int colIdx = 0;
        for (byte val : unhexed) {
            result.append(String.format("%02x", val));
            colIdx++;
            if (lineLength > 0 && colIdx >= lineLength / 2) {
                result.append(LINE_BREAK);
                colIdx = 0;
            }
        }

        return result.toString();
    }

}
