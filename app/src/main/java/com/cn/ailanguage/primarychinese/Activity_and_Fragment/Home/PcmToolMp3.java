package com.cn.ailanguage.primarychinese.Activity_and_Fragment.Home;


//import com.arthenica.mobileffmpeg.FFmpeg;

import com.arthenica.ffmpegkit.FFmpegKit;

public class PcmToolMp3 {
    public void convertWavToMp3(String inputFilePath, String outputFilePath) {
//        String[] ffmpegCommand = {
//                "-i",
//                inputFilePath,
//                "-c:a",
//                "libmp3lame",
//                "-q:a",
//                "2",
//                outputFilePath
//        };
        String ffmpegCommand = String.format("-hide_banner -y -i %s -c:a libmp3lame -qscale:a 2 %s", inputFilePath, outputFilePath);
         FFmpegKit.execute(String.valueOf(ffmpegCommand));
//
//        int executionId =if (executionId == RETURN_CODE_SUCCESS) {
//            System.out.println("WAV to MP3 conversion completed successfully.");
//        } else {
////            System.err.println("Error converting WAV to MP3: " + FFmpeg.getLastCommandOutput());
//        }
    }
}
