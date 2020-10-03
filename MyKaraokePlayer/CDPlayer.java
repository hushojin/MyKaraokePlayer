import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;

public class CDPlayer{
    Gamen gamen;
    PnPlayer pnp;
    ChooseData cd = null;
    AudioInputStream ais;
    AudioInputStream dais;
    AudioFormat af;
    Clip clip;
    int number;//再生中のCDSがCDLの配列のどのインデックスの奴かを指す。0～CDL.number()-1
    
    CDPlayer(){
    }
    void setGamen(Gamen gamen){
        this.gamen = gamen;
    }
    void setPnPlayer(PnPlayer pnp){
        this.pnp = pnp;
    }
    
    void set(ChooseData cd){
        teishi();
        this.cd = cd;
        if(cd.isSong()){
            mplay(((ChooseDataSong)cd).getFname());
            gamen.setTitle(cd.getName()+" MyKarakePlayer");
        }
        else{
            int number = 0;
            mplay((CDFLibrary.getCDS(((ChooseDataList)cd).getSongs()[number])).getFname());
            gamen.setTitle((CDFLibrary.getCDS(((ChooseDataList)cd).getSongs()[number])).getName()+" MyKarakePlayer");
        }
    }
    
    void teishi(){
        if(clip != null && clip.isOpen()){
            try {
                clip.stop();
                if(ais!=null){
                    ais.close();
                }
                System.out.println("CDP.teishi:aisNull"+(ais==null));
                if(dais!=null){
                    dais.close();
                }
                System.out.println("CDP.teishi:daisNull"+(dais==null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    void play(String fname){
        
        try {
            ais = AudioSystem.getAudioInputStream(new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\",fname));
            af = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, af);
            clip = (Clip)AudioSystem.getLine(info);
            clip.addLineListener(
                new LineListener(){
                    public void update(LineEvent e){
                        pnp.henka((int)clip.getFrameLength(),(int)clip.getMicrosecondLength());
                        if(clip.getFramePosition() >= clip.getFrameLength()){
                            endplay();
                        }
                    }
                }
            );
            clip.open(ais);
            clip.loop(0);
            clip.flush();
            /*while(clip.isActive()) {
                Thread.sleep(300);
            }*/
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException /*| InterruptedException*/ e) {
            e.printStackTrace();
        }finally {
            try {
                ais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void mplay(String fname){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("C:/\\Users\\Owner\\Desktop\\Karaokewavs\\",fname));
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,baseFormat.getSampleRate(),
                                                        16,baseFormat.getChannels(),
                                                        baseFormat.getChannels() * 2,
                                                        baseFormat.getSampleRate(),
                                                        false);
            dais = AudioSystem.getAudioInputStream(decodedFormat,ais);
            rawplayclip(decodedFormat,dais);
            ais.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    private void rawplayclip(AudioFormat targetFormat, AudioInputStream din) throws IOException,LineUnavailableException{
        try {
            DataLine.Info info = new DataLine.Info(Clip.class, targetFormat);
            for(AudioFormat af:info.getFormats()){
                System.out.println("CDP.rawplayclip "+af);
            }
            clip = (Clip)AudioSystem.getLine(info);
            /*
            System.out.println(clip.isControlSupported(FloatControl.Type.VOLUME));
            System.out.println(clip.isControlSupported(FloatControl.Type.SAMPLE_RATE));
            System.out.println(clip.isControlSupported(FloatControl.Type.REVERB_SEND));
            System.out.println(clip.isControlSupported(FloatControl.Type.REVERB_RETURN));
            System.out.println(clip.isControlSupported(FloatControl.Type.PAN));
            System.out.println(clip.isControlSupported(FloatControl.Type.MASTER_GAIN));
            System.out.println(clip.isControlSupported(FloatControl.Type.BALANCE));
            System.out.println(clip.isControlSupported(FloatControl.Type.AUX_SEND));
            System.out.println(clip.isControlSupported(FloatControl.Type.AUX_RETURN));
            //ctrl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);*/
            System.out.println("\n===== コントロールズ =====");
            javax.sound.sampled.Control[] ctrls = clip.getControls();
            for(javax.sound.sampled.Control ctrl : ctrls) {
                System.out.println(ctrl.toString());
            }
            
            clip.addLineListener(
                new LineListener(){
                    public void update(LineEvent e){
                        pnp.henka((int)clip.getFrameLength(),(int)clip.getMicrosecondLength());
                        if(clip.getFramePosition() >= clip.getFrameLength()){
                            endplay();
                        }
                    }
                }
            );
            
            clip.open(din);
            clip.loop(0);
            clip.flush();
            //while(clip.isActive()) {
            //    Thread.sleep(100);
            //}
        } catch (/*UnsupportedAudioFileException | */IOException | LineUnavailableException /*| InterruptedException*/ e) {
            e.printStackTrace();
        }finally {
            try {
                din.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    long getFrameLength(){
        return clip.getFramePosition();
    }
    long getMicrosecondLength(){
        return clip.getMicrosecondPosition();
    }
    long getFramePosition(){
        return clip.getFramePosition();
    }
    void setFramePosition(int frame){
        clip.setFramePosition(frame);
    }
    long getMicrosecondPosition(){
        return clip.getMicrosecondPosition();
    }
    void setMicrosecondPosition(long frame){
        clip.setMicrosecondPosition(frame);
    }
    boolean isRunning(){
        return clip.isRunning();
    }
    void start(){
        clip.start();
    }
    void stop(){
        clip.stop();
    }
    boolean next(){//次があるかどうかはここが確かめる リストかどうかも判断しろ
        if( !cd.isSong() && number+1 < ((ChooseDataList)cd).number() ){
            teishi();
            number++;
            mplay((CDFLibrary.getCDS(((ChooseDataList)cd).getSongs()[number])).getFname());
            gamen.setTitle((CDFLibrary.getCDS(((ChooseDataList)cd).getSongs()[number])).getName()+" MyKarakePlayer");
            return true;
        }
        else{
            return false;
        }
    }
    boolean prev(){//前があるかどうかはここが確かめる リストかどうかも
        if( !cd.isSong() && number >= 1 ){
            teishi();
            number--;
            mplay((CDFLibrary.getCDS(((ChooseDataList)cd).getSongs()[number])).getFname());
            gamen.setTitle((CDFLibrary.getCDS(((ChooseDataList)cd).getSongs()[number])).getName()+" MyKarakePlayer");
            return true;
        }
        else{
            return false;
        }
    }
    void volume(){
        //死
    }
    
    void endplay(){
        if(cd.isSong()){
            clip.setFramePosition(0);
        }
        else{
            if(next()){//次があるかどうかはネクストが確かめる
                clip.setFramePosition(0);
            }
        }
    }
}