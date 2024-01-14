package view;

import javax.sound.sampled.*;
import java.io.File;

public class GlobalSound {
    String audioPath = "./image/music/sound.wav";

    protected static GlobalSound instance;
    private static BGMThread bgmThread;

    /**
     * 负责人：旅箔
     * 功能：构造器
     */
    private GlobalSound(){
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 负责人：旅箔
     * 功能：返回一个GlobalSound，根据instance的值判断是否要创建新的GlobalSound实例
     * 参数：void
     * 返回值：GlobalSound
     */
    public static GlobalSound getInstance(){
        if (instance == null){
            instance = new GlobalSound();
        }
        return instance;
    }

    /**
     * 负责人：旅箔
     * 功能：开启音乐线程
     * 参数：void
     * 返回值：void
     */
    public void Start(){
        bgmThread = new BGMThread();
        bgmThread.run();
    }
    /**
     * 负责人：旅箔
     * 功能：关闭音乐线程
     * 参数：void
     * 返回值：void
     */
    public void Stop(){
        bgmThread.pause();
    }


    /**
     * 负责人：旅箔
     * 功能：内部类，负责创建音乐
     */
     class BGMThread extends Thread{
        private AudioInputStream aio;
        private Clip bgm;

        /**
         * 负责人：旅箔
         * 功能：构造器，初始化音乐线程
         */
        public BGMThread(){
            try{
                aio = AudioSystem.getAudioInputStream(new File(audioPath));
                bgm = AudioSystem.getClip();
                bgm.open(aio);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        /**
         * 负责人：旅箔
         * 功能：开启线程调用的方法，开启音乐，并设置循环播放
         * 参数：void
         * 返回值：void
         */
        @Override
        public void run(){
            bgm.start();
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        }

        /**
         * 负责人：旅箔
         * 功能：关闭音乐线程
         * 参数：void
         * 返回值：void
         */
        public void pause(){
            bgm.close();
            bgm.stop();
        }
    }


}
