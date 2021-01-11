import java.io.*;
import javax.sound.sampled.*;

public class CDPlayer{
    static String songFileDirectory="C:/\\Users\\Owner\\Desktop\\Karaokewavs\\";
    static StringDisplay sDisp;
    static PlayStateDisplay psDisp;
    static Thread psdThr;
    static ChooseData cd = null;
    static AudioInputStream ais;
    static AudioInputStream dais;
    static AudioFormat af;
    static Clip clip;
    static java.util.List<LineListener> listeners=new java.util.ArrayList<>();
    static int number;//再生中のCDSがCDLの配列のどのインデックスの奴かを指す。0〜CDL.number()-1
    
    public static void setStringDisplay(StringDisplay sDisp){
        CDPlayer.sDisp = sDisp;
    }
    public static void setPlayStateDisplay(PlayStateDisplay psDisp){
        CDPlayer.psDisp = psDisp;
    }
    private static void psDisplayUpdate(){
        psDisp.setPlayState(
            new PlayState(
                clip.isRunning(),
                clip.getFrameLength(),
                clip.getFramePosition(),
                clip.getMicrosecondLength(),
                clip.getMicrosecondPosition()
            )
        );
    }
    public static void setCD(ChooseData cd){
        stop();
        CDPlayer.cd = cd;
        if(cd.isSong()){
            ChooseDataSong cds=(ChooseDataSong)cd;
            mplay(cds.getFname());
            sDisp.setString(cd.getName()+" MyKarakePlayer");
        }
        else{
            ChooseDataList cdl=(ChooseDataList)cd;
            number = 0;
            ChooseDataSong cds=CDFLibrary.getCDS(cdl.getSongs()[number]);
            mplay(cds.getFname());
            sDisp.setString((number+1)+"/"+cdl.number()+" "+cds.getName()+" MyKarakePlayer");
        }
    }
    private static void stop(){
        if(clip!=null&&clip.isOpen()){
            try{
                clip.stop();
                clip.close();
                if(ais!=null){
                    ais.close();
                }
                if(dais!=null){
                    dais.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    private static void mplay(String fname){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(songFileDirectory,fname));
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
    private static void rawplayclip(AudioFormat targetFormat, AudioInputStream din) throws IOException,LineUnavailableException{
        try {
            DataLine.Info info = new DataLine.Info(Clip.class, targetFormat);
            clip = (Clip)AudioSystem.getLine(info);
            
            clip.addLineListener(
                (e)->{
                    if(e.getType()==LineEvent.Type.START){
                        psdThr=new Thread(
                            ()->{
                                try{
                                    while(clip.isRunning()){
                                        psDisplayUpdate();
                                        psdThr.sleep(100);
                                    }
                                }catch(InterruptedException ex){}
                            }
                        );
                        psdThr.start();
                    }else if(e.getType()==LineEvent.Type.STOP){
                        psDisplayUpdate();
                        if(clip.getFramePosition()>=clip.getFrameLength()){
                            if(cd.isSong()){
                                setFramePosition(0);
                            }else{
                                next();
                            }
                        }
                    }
                }
            );
            
            clip.open(din);
            clip.loop(0);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }finally {
            try {
                din.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void next(){
        if( !cd.isSong() && number+1 < ((ChooseDataList)cd).number() ){
            ChooseDataList cdl=(ChooseDataList)cd;
            stop();
            number++;
            mplay(CDFLibrary.getCDS(cdl.getSongs()[number]).getFname());
            sDisp.setString((number+1)+"/"+cdl.number()+" "+CDFLibrary.getCDS(cdl.getSongs()[number]).getName()+" MyKarakePlayer");
        }
    }
    public static void prev(){
        if(!cd.isSong() && 1<= number && clip.getMicrosecondPosition() < 2000000L){
            ChooseDataList cdl=(ChooseDataList)cd;
            stop();
            number--;
            mplay(CDFLibrary.getCDS(cdl.getSongs()[number]).getFname());
            sDisp.setString((number+1)+"/"+cdl.number()+" "+CDFLibrary.getCDS(cdl.getSongs()[number]).getName()+" MyKarakePlayer");
        }else{
            CDPlayer.setFramePosition(0);
        }
    }
    
    public static void setFramePosition(int frame){
        clip.setFramePosition(frame);
        psDisplayUpdate();
    }
    public static void shiftSecond(int sec){
        clip.setMicrosecondPosition(clip.getMicrosecondPosition()+1000000L*sec);
        psDisplayUpdate();
    }
    public static void start(){
        clip.start();
    }
    public static void pause(){
        clip.stop();
    }
}