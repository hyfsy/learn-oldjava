package test.music;

import java.io.*;

/**
 * @author baB_hyf
 * @date 2022/04/25
 */
public class IMDReader {

    public static void main(String[] args) {
        String file = "C:\\Users\\baB_hyf\\Desktop\\MusicScoreConvter\\sample\\my_76991048_ms.imd";
        parse(file);
    }

    public static void parse(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            byte[] bytes = baos.toByteArray();
            parse(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void parse(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        try {
            parseSongTime(bytes, sb);
            parseBeatNumber(bytes, sb);
            parseBeats(bytes, sb);
            parseSeparator(bytes, sb);
            parseActionNumber(bytes, sb);
            parseActions(bytes, sb);
        } catch (Exception e) {
            System.out.println(sb.toString());
            throw e;
        }
        System.out.println(sb.toString());
    }

    private static void parseSongTime(byte[] bytes, StringBuilder sb) {
        sb.append(hex(bytes[0])).append(' ');
        sb.append(hex(bytes[1])).append(' ');
        sb.append(hex(bytes[2])).append(' ');
        sb.append(hex(bytes[3])).append('\n');
    }

    private static void parseBeatNumber(byte[] bytes, StringBuilder sb) {
        sb.append(hex(bytes[4])).append(' ');
        sb.append(hex(bytes[5])).append(' ');
        sb.append(hex(bytes[6])).append(' ');
        sb.append(hex(bytes[7])).append('\n');
    }

    private static void parseBeats(byte[] bytes, StringBuilder sb) {

        int beatNumber = Integer.parseInt(hex(bytes[7]) + hex(bytes[6]) + hex(bytes[5]) + hex(bytes[4]), 16);

        int offset = 8;
        int unit = 12; // 4 -> beat timestamp(int), 8 -> bpm(double)
        for (int i = 0; i < beatNumber; i++) {
            for (int j = 0; j < unit; j++) {
                sb.append(hex(bytes[offset + j]));
                if (j < unit - 1) {
                    sb.append(' ');
                }
                offset++;
            }
            sb.append('\n');
        }
    }

    private static void parseSeparator(byte[] bytes, StringBuilder sb) {
        int beatNumber = Integer.parseInt(hex(bytes[7]) + hex(bytes[6]) + hex(bytes[5]) + hex(bytes[4]), 16);
        int i = beatNumber * 12 + 8;
        sb.append(hex(bytes[i++])).append(' ').append(hex(bytes[i])).append('\n');
    }

    private static void parseActionNumber(byte[] bytes, StringBuilder sb) {
        int beatNumber = Integer.parseInt(hex(bytes[7]) + hex(bytes[6]) + hex(bytes[5]) + hex(bytes[4]), 16);
        int i = beatNumber * 12 + 8 + 2;
        sb.append(hex(bytes[i++])).append(' ');
        sb.append(hex(bytes[i++])).append(' ');
        sb.append(hex(bytes[i++])).append(' ');
        sb.append(hex(bytes[i])).append('\n');
    }

    private static void parseActions(byte[] bytes, StringBuilder sb) {
        int beatNumber = Integer.parseInt(hex(bytes[7]) + hex(bytes[6]) + hex(bytes[5]) + hex(bytes[4]), 16);
        int offset = beatNumber * 12 + 8 + 2;

        int actionNumber = Integer.parseInt(hex(bytes[offset + 3]) + hex(bytes[offset + 2]) + hex(bytes[offset + 1]) + hex(bytes[offset]), 16);
        offset += 4;

        int unit = 11;
        for (int i = 0; i < actionNumber; i++) {
            for (int j = 0; j < unit; j++) {
                sb.append(hex(bytes[offset + j]));
                if (j < unit - 1) {
                    sb.append(' ');
                }
                offset++;
            }
            sb.append('\n');
        }
    }

    private static String hex(byte b) {
        return String.format("%02x", b);
    }
}
