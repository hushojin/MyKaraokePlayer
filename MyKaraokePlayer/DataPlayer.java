import java.io.*;
import javax.swing.Timer;
import javax.sound.sampled.*;

public class DataPlayer{
    private static final String SONG_FILE_DIRECTORY="C:/\\Users\\Owner\\Desktop\\Karaokewavs\\";
    private static StringDisplay sDisp;
    private static PlayStateDisplay psDisp;
    private static Thread psdThr;
    private static Timer timer=new Timer(100,(e)->psDisplayUpdate());
    private static Data cd = null;
    private static AudioInputStream ais;
    private static AudioInputStream dais;
    private static AudioFormat af;
    private static Clip clip;
    private static java.util.List<LineListener> listeners=new java.util.ArrayList<>();
    private static int number;//再生中のCDSがCDLの配列のどのインデックスの奴かを指す。0～CDL.size()-1
    
    public static void setStringDisplay(StringDisplay sDisp){
        DataPlayer.sDisp = sDisp;
    }
    public static void setPlayStateDisplay(PlayStateDisplay psDisp){
        DataPlayer.psDisp = psDisp;
    }
    private static void psDisplayUpdate(){
        if(psDisp==null){return;}
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
    public static void setData(Data cd){
        stop();
        DataPlayer.cd = cd;
        if(cd.isSong()){
            SongData cds=(SongData)cd;
            sDisp.setString(cds.getName()+" MyKarakePlayer");
            mplay(cds.getFname());
        }
        else{
            ListData cdl=(ListData)cd;
            number = 0;
            if(cdl.size()<1){
                sDisp.setString("0/0 MyKarakePlayer");
                return;
            }
            SongData cds=cdl.getSongs()[number];
            mplay(cds.getFname());
            sDisp.setString((number+1)+"/"+cdl.size()+" "+cds.getName()+" MyKarakePlayer");
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
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(SONG_FILE_DIRECTORY,fname));
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
                        timer.start();
                    }else if(e.getType()==LineEvent.Type.STOP){
                        psDisplayUpdate();
                        timer.stop();
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
        if( !cd.isSong() && number+1 < ((ListData)cd).size() ){
            ListData cdl=(ListData)cd;
            stop();
            number++;
            mplay(cdl.getSongs()[number].getFname());
            sDisp.setString((number+1)+"/"+cdl.size()+" "+cdl.getSongs()[number].getName()+" MyKarakePlayer");
        }
    }
    public static void prev(){
        if(!cd.isSong() && 1<= number && clip.getMicrosecondPosition() < 2000000L){
            ListData cdl=(ListData)cd;
            stop();
            number--;
            mplay(cdl.getSongs()[number].getFname());
            sDisp.setString((number+1)+"/"+cdl.size()+" "+cdl.getSongs()[number].getName()+" MyKarakePlayer");
        }else{
            DataPlayer.setFramePosition(0);
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