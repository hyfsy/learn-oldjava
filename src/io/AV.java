package io;

import java.io.File;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;

/**
 * 音视频相关操作，底层ffmpeg
 * 
 * @see 兼容：wav/mp4
 * @see 不兼容：mkv
 * 
 * @author baB_hyf
 *
 */
public class AV
{

    public static void main(String[] args) {
        String file = "E:\\电子政务4部\\部门学习\\学习\\02代码规范.mp4";
        String file2 = "E:\\music\\kugou\\孙语赛&萧全-不仅仅是喜欢.mp3";
        System.out.println(getAVTime(file2));
    }

    /**
     * 获取音视频的时长
     * 
     * @param avPath
     * @return
     */
    public static String getAVTime(String avPath) {
        // 存放输出的视频时长信息
        StringBuilder duration = new StringBuilder();

        // 重要的解码器
        Encoder encoder = new Encoder();
        try {
            MultimediaInfo info = encoder.getInfo(new File(avPath));
            long ls = info.getDuration() / 1000;
            int hour = (int) (ls / 3600);
            int minute = (int) (ls % 3600 / 60);
            int second = (int) (ls - hour * 3600 - minute * 60);
            duration.append(hour).append(":").append(minute).append(":").append(second);
        }
        catch (EncoderException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            System.out.println(cause);
        }
        return duration.toString();
    }

}
