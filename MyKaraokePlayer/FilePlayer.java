import java.io.*;
import javax.swing.Timer;
import javax.sound.sampled.*;

class FilePlayer{
    private Clip clip;
    private Timer timer=new Timer(100,(e)->running());
    
    void setFile(File file){
        stop();
        try(AudioInputStream ais= AudioSystem.getAudioInputStream(file) ){
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,baseFormat.getSampleRate(),16,baseFormat.getChannels(),baseFormat.getChannels() * 2,baseFormat.getSampleRate(),false);
            try( AudioInputStream dais = AudioSystem.getAudioInputStream(targetFormat,ais) ){
                DataLine.Info info = new DataLine.Info(Clip.class,targetFormat);
                clip = (Clip)AudioSystem.getLine(info);
                clip.addLineListener(
                    (e)->{
                        if(e.getType()==LineEvent.Type.START){
                            timer.start();
                        }else if(e.getType()==LineEvent.Type.STOP){
                            timer.stop();
                            if(clip.getFramePosition()>=clip.getFrameLength()){
                                endPlay();
                            }
                        }
                    }
                );
                clip.open(dais);
            }catch(LineUnavailableException | IOException e){
                clip=null;
                throw e;
            }
        }catch(LineUnavailableException | UnsupportedAudioFileException | IOException e){
            e.printStackTrace();
        }
    }
    
    void stop(){
        if(clip==null){
            return;
        }
        if(clip.isOpen()){
            clip.stop();
            clip.close();
        }
        clip=null;
        manipulated();
    }
    void play(){
        if(clip==null){
            return;
        }
        clip.start();
        manipulated();
        if(!clip.isRunning()&&clip.getFramePosition()>=clip.getFrameLength()){
            endPlay();
        }
    }
    void pause(){
        if(clip==null){
            return;
        }
        clip.stop();
        manipulated();
    }
    
    void setFramePosition(int frame){
        if(clip==null){
            return;
        }
        clip.setFramePosition(frame);
        manipulated();
    }
    void shiftSecond(int sec){
        if(clip==null){
            return;
        }
        clip.setMicrosecondPosition(clip.getMicrosecondPosition()+sec*1000000L);
        manipulated();
    }
    boolean startFromSecond(int sec){
        return clip.getMicrosecondPosition() < sec*1000000L; 
    }
    public PlayState getPlayState(){
        if(clip==null){
            return null;
        }
        return new PlayState(clip.isRunning(),clip.getFrameLength(),clip.getFramePosition(),clip.getMicrosecondLength(),clip.getMicrosecondPosition());
    }
    public void manipulated(){}
    public void endPlay(){}
    public void running(){}
}