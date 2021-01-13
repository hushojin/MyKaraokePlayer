import java.io.*;
import javax.swing.Timer;
import javax.sound.sampled.*;

public class DataPlayer{
    private static final String SONG_FILE_DIRECTORY="C:/\\Users\\Owner\\Desktop\\Karaokewavs\\";
    private static StringDisplay sDisp;
    private static PlayStateDisplay psDisp;
    private static boolean hasSong;
    private static SongData sd = null;
    private static ListData ld = null;
    private static SongData[] sds = null;
    private static int index;
    private static FilePlayer player=new FilePlayer();
    
    public static void setStringDisplay(StringDisplay sDisp){
        DataPlayer.sDisp = sDisp;
    }
    public static void setPlayStateDisplay(PlayStateDisplay psDisp){
        DataPlayer.psDisp = psDisp;
    }
    private static void psDisplayUpdate(){
        if(psDisp==null){
            return;
        }
        psDisp.setPlayState(player.getPlayState());
    }
    public static void setData(Data d){
        if(d.isSong()){
            setSong((SongData)d);
        }else{
            setList((ListData)d);
        }
        player.start();
    }
    public static void setSong(SongData sd){
        hasSong=true;
        DataPlayer.sd=sd;
        player.setFile(new File(SONG_FILE_DIRECTORY,sd.getFname()));
    }
    public static void setList(ListData ld){
        hasSong=false;
        DataPlayer.ld=ld;
        DataPlayer.sds=ld.getSongs();
        index = 0;
        if(sds.length<1){
            sDisp.setString("0/0 MyKarakePlayer");
            return;
        }
        player.setFile(new File(SONG_FILE_DIRECTORY,sds[index].getFname()));
        sDisp.setString((index+1)+"/"+sds.length+" "+sds[index].getName()+" MyKarakePlayer");
    }
    public static void next(){
        if( !hasSong && index+1 < sds.length ){
            index++;
            SongData sd=sds[index];
            player.setFile(new File(SONG_FILE_DIRECTORY,sd.getFname()));
            player.start();
            sDisp.setString((index+1)+"/"+sds.length+" "+sd.getName()+" MyKarakePlayer");
        }
    }
    public static void prev(){
        if(!hasSong && 1<= index && player.startFromSecond(2)){
            index--;
            SongData sd=sds[index];
            player.setFile(new File(SONG_FILE_DIRECTORY,sd.getFname()));
            player.start();
            sDisp.setString((index+1)+"/"+sds.length+" "+sd.getName()+" MyKarakePlayer");
        }else{
            DataPlayer.setFramePosition(0);
        }
    }
    
    public static void setFramePosition(int frame){
        player.setFramePosition(frame);
        psDisplayUpdate();
    }
    public static void shiftSecond(int sec){
        player.shiftSecond(sec);
        psDisplayUpdate();
    }
    public static void start(){
        player.start();
    }
    public static void pause(){
        player.pause();
    }
    public static void endPlay(LineEvent e){
        if(hasSong){
            player.setFramePosition(0);
         }else{
            next();
         }
    }
    
    private static class FilePlayer{
        private AudioInputStream ais;
        private AudioInputStream dais;
        private AudioFormat af;
        private Clip clip;
        private static Timer timer=new Timer(100,(e)->psDisplayUpdate());
        
        public void setFile(File file){
            stop();
            prepare(file);
        }
        private void prepare(File file){
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                AudioFormat baseFormat = ais.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,baseFormat.getSampleRate(),16,baseFormat.getChannels(),baseFormat.getChannels() * 2,baseFormat.getSampleRate(),false);
                dais = AudioSystem.getAudioInputStream(decodedFormat,ais);
                rawplayclip(decodedFormat,dais);
                ais.close();
            }catch (Exception e){
                System.out.println(e);
            }
        }
        private void rawplayclip(AudioFormat targetFormat, AudioInputStream din){
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
                                DataPlayer.endPlay(e);
                            }
                        }
                    }
                );
                clip.open(din);
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
        
        public void stop(){
            try{
                if(clip!=null&&clip.isOpen()){
                    clip.stop();
                    clip.close();
                    clip=null;
                }
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
        public void start(){
            if(clip==null){
                return;
            }
            clip.start();
        }
        public void pause(){
            if(clip==null){
                return;
            }
            clip.stop();
        }
        
        public void shiftSecond(int sec){
            if(clip==null){
                return;
            }
            clip.setMicrosecondPosition(clip.getMicrosecondPosition()+sec*1000000L);
            psDisplayUpdate();
        }
        public boolean startFromSecond(int sec){
           return clip.getMicrosecondPosition() < sec*1000000L; 
        }
        public void setFramePosition(int frame){
            if(clip==null){
                return;
            }
            clip.setFramePosition(frame);
            psDisplayUpdate();
        }
        public PlayState getPlayState(){
          if(clip==null){
              return null;
          }
          return new PlayState(clip.isRunning(),clip.getFrameLength(),clip.getFramePosition(),clip.getMicrosecondLength(),clip.getMicrosecondPosition());
        }
    }
}