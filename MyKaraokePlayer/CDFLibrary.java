import java.io.*;
import java.util.*;

public class CDFLibrary{
    static List<ChooseDataSong> cdss;
    static List<ChooseDataList> cdls;
    static PnList pnl;
    static{
        reload(true);
        reload(false);
    }
    public static void setPnList(PnList pnl){
        CDFLibrary.pnl = pnl;
        ChooseData[] a = new ChooseData[0];
        pnl.lupdate(cdss.toArray(a));
    }
    
    public static void update(String f,boolean isSong){
        if(isSong){
            ChooseData[] a = new ChooseData[0];
            pnl.lupdate(cdss.toArray(a));
            List<ChooseDataSong> result = new ArrayList<ChooseDataSong>(0);
            for(ChooseDataSong all : cdss){
                if(all.getName().startsWith(f)){
                    result.add(all);
                }
            }
            pnl.lupdate(result.toArray(a));
            
        }else{
            ChooseData[] a = new ChooseData[0];
            pnl.lupdate(cdls.toArray(a));
            List<ChooseDataList> result = new ArrayList<ChooseDataList>(0);
            for(ChooseDataList all : cdls){
                if(all.getName().startsWith(f)){
                    result.add(all);
                }
            }
            pnl.lupdate(result.toArray(a));
        }
    }
    public static void reload(boolean isSong){
        System.out.println("CDFL.reload:isSong"+isSong);
        if(isSong){
            try(FileReader fr = new FileReader("C:\\Users\\Owner\\Desktop\\Karaokewavs\\S.MKP")){
                cdss = sFileRead(fr);
            }catch(FileNotFoundException e){
                System.out.println("S.MKPが開けませんでした。");
            }catch(IOException e){
                System.out.println("reloadS:frが閉じられませんでした。");
            }
        }
        else{
            try(FileReader fr = new FileReader("C:\\Users\\Owner\\Desktop\\Karaokewavs\\L.MKP")){
                cdls = lFileRead(fr);
            }catch(FileNotFoundException e){
                System.out.println("L.MKPが開けませんでした。");
            }catch(IOException e){
                System.out.println("reloadL:frが閉じられませんでした。");
            }
        }
    }
    
    public static void songEdit(ChooseDataSong cds){
        File s = new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\S.MKP");
        File tmps = new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\tmpS.MKP");
        int target = getSongNumber(cds.getFname());
        int d;
        String str=null;
        try(FileReader fr = new FileReader(s);FileWriter fw = new FileWriter(tmps)){
            //編集部前まで書き写し
            for(int i=0;i<target;i++){
                for(int j=0;j<8;j++){
                    while( (d = fr.read()) != 10){
                        str = (str == null ? "" : str )+ (char)d;
                    }
                    if(str!=null){
                        fw.write(str);
                        str = null;
                    }
                    fw.write(10);
                }
            }
            
            
            //編集後データの差し込み
            fw.write(10);
            
            fw.write(cds.getFname());//fname
            fw.write(10);
            fw.write(cds.getName());//name
            fw.write(10);
            fw.write(String.valueOf(cds.getGrade()));//grade
            fw.write(10);
            fw.write(cds.getComment());//comment
            fw.write(10);
            fw.write(cds.getDate());//date
            fw.write(10);
            fw.write(cds.getWith());//with
            fw.write(10);
            fw.write(cds.getScore());//score
            fw.write(10);
            
            //編集された元データ分の読み飛ばし八行
            for(int j=0;j<8;j++){
                while( (d = fr.read()) != 10){}
            }
            
            //編集部後を書き写し
            while(fr.read() != -1){
                fw.write(10);
                for(int j=0;j<7;j++){
                    while( (d = fr.read()) != 10){
                        str = (str == null ? "" : str )+ (char)d;
                    }
                    if(str != null){
                        fw.write(str);
                        str = null;
                    }
                    fw.write(10);
                }
            }
            
            fw.flush();
            
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        
        try{
            System.out.println("SEdit.s.delate"+s.delete());
            System.out.println("SEdit.rename"+tmps.renameTo(new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\S.MKP")));
        }catch(SecurityException e){
            System.out.println(e);
        }
        reload(true);
    }
    
    public static void addNewSongData(String fname,String name,int grade,String comment,String date,String with,String score){
        try (FileWriter fw = new FileWriter("C:\\Users\\Owner\\Desktop\\Karaokewavs\\S.MKP",true)){
            
            fw.write(10);
            
            fw.write(fname);//fname
            fw.write(10);
            fw.write(name);//name
            fw.write(10);
            fw.write(String.valueOf(grade));//grade
            fw.write(10);
            fw.write(comment);//comment
            fw.write(10);
            fw.write(date);//date
            fw.write(10);
            fw.write(with);//with
            fw.write(10);
            fw.write(score);//score
            fw.write(10);
            
            fw.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
        reload(true);
    }
    
    public static void addNewPlayListData(String name,int[] cdss){
        try (FileWriter fw = new FileWriter("C:\\Users\\Owner\\Desktop\\Karaokewavs\\L.MKP",true)){
            
            fw.write(10);
            
            fw.write(name);//name
            fw.write(10);
            for(int i:cdss){
                fw.write(String.valueOf(i)+".");
            }
            fw.write(10);
            
            fw.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
        reload(false);
    }
    
    public static void playListDelete(int target){
        System.out.println("CDFL.PLDelete:target"+target);
        File l = new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\L.MKP");
        File tmpl = new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\tmpL.MKP");
        int d;
        try(FileReader fr = new FileReader(l);FileWriter fw = new FileWriter(tmpl)){
            //削除部前まで書き写し
            for(int i=0;i<target;i++){
                fr.read();//頭取り
                String name = null;
                fw.write(10);
                System.out.println("CDFL.PLDelete:d(asName)");
                while( (d = fr.read()) != 10 && d != -1 ){
                    fw.write(d);
                    System.out.print((char)d);
                }
                fw.write(10);
                System.out.println("\nCDFL.PLDelete:d(asNumber)");
                while( (d = fr.read()) != 10 && d != -1 ){
                    fw.write(d);
                    System.out.print((char)d);
                }
                fw.write(10);
                System.out.print("\n");
            }
            
            //削除する元データ分の読み飛ばし
            fr.read();
            while( (d = fr.read()) != 10 && d != -1 ){}
            while( (d = fr.read()) != 10 && d != -1 ){}
            
            //削除部後を書き写し
            while( (d=fr.read())!=-1 ){
                String name = null;
                fw.write(10);
                while( (d = fr.read()) != 10 && d != -1 ){
                    fw.write(d);
                }
                fw.write(10);
                while( (d = fr.read()) != 10 && d != -1 ){
                    fw.write(d);
                }
                fw.write(10);
            }
            
            fw.flush();
            
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        
        try{
            System.out.println("CDFL.PLDelete.l.delate"+l.delete());
            System.out.println("rename"+tmpl.renameTo(new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\L.MKP")));
        }catch(SecurityException e){
            System.out.println(e);
        }
        reload(false);
        
    }
    
    public static void playListEdit(int target,ChooseDataList cdl){
        System.out.println("CDFL.PLEdit:target"+target);
        File l = new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\L.MKP");
        File tmpl = new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\tmpL.MKP");
        
        int d;
        try(FileReader fr = new FileReader(l);FileWriter fw = new FileWriter(tmpl)){
            System.out.print("CDFL.PLEdit:target"+target);
            //編集部前まで書き写し
            for(int i=0;i<target;i++){
                fr.read();//頭取り
                String name = null;
                fw.write(10);
                System.out.println("CDFL.PLEdit:d(asName)");
                while( (d = fr.read()) != 10 && d != -1 ){
                    fw.write(d);
                    System.out.print((char)d);
                }
                fw.write(10);
                System.out.println("\nCDFL.PLEdit:d(asNumber)");
                while( (d = fr.read()) != 10 && d != -1 ){
                    fw.write(d);
                    System.out.print((char)d);
                }
                fw.write(10);
                System.out.print("\n");
            }
            
            //編集後データの差し込み
            fw.write(10);
            fw.write(cdl.getName());//name
            fw.write(10);
            for(int i:cdl.getSongs()){
                fw.write(String.valueOf(i)+".");
            }
            fw.write(10);
            
            
            //編集された元データ分の読み飛ばし
            fr.read();
            while( (d = fr.read()) != 10 && d != -1 ){}
            while( (d = fr.read()) != 10 && d != -1 ){}
            
            //編集部後を書き写し
            while( (d=fr.read())!=-1 ){
                String name = null;
                fw.write(10);
                while( (d = fr.read()) != 10 && d != -1 ){
                    fw.write(d);
                }
                fw.write(10);
                while( (d = fr.read()) != 10 && d != -1 ){
                    fw.write(d);
                }
                fw.write(10);
            }
            
            fw.flush();
            
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        
        try{
            System.out.println("CDFL.PLEdit.l.delate"+l.delete());
            System.out.println("rename"+tmpl.renameTo(new File("C:\\Users\\Owner\\Desktop\\Karaokewavs\\L.MKP")));
        }catch(SecurityException e){
            System.out.println(e);
        }
        reload(false);
        
    }
    
    private static List<ChooseDataSong> sFileRead(FileReader fr){
        List<ChooseDataSong> reads = new ArrayList<ChooseDataSong>(0);
        int d;
        String fname;
        String name;
        int grade;
        String comment;
        String date;
        String with;
        String score;
        
        try{
            while( fr.read() != -1){
                fname = null;
                name = null;
                grade = 0;
                comment = null;
                date = null;
                with = null;
                score = null;
                
                while( (d = fr.read()) != -1 && d != 10){
                    fname = (fname == null ? "" : fname )+ (char)d;
                }
                while( (d = fr.read()) != -1 && d != 10){
                    name = (name == null ? "" : name )+ (char)d;
                }
                while( (d = fr.read()) != -1 && d != 10){
                    grade = 100*grade + d-48;
                }
                while( (d = fr.read()) != -1 && d != 10){
                    comment = (comment == null ? "" : comment )+ (char)d;
                }
                while( (d = fr.read()) != -1 && d != 10){
                    date = (date == null ? "" : date )+ (char)d;
                }
                while( (d = fr.read()) != -1 && d != 10){
                    with = (with == null ? "" : with )+ (char)d;
                }
                while( (d = fr.read()) != -1 && d != 10){
                    score = (score == null ? "" : score )+ (char)d;
                }
                reads.add(new ChooseDataSong(fname,name,grade,comment,date,with,score));
            }
        }catch(IOException e){
            System.out.println("CDFL.sFileRead:readできなかったそうな。");
        }
        
        return reads;
    }
    
    private static List<ChooseDataList> lFileRead(FileReader fr){
        List<ChooseDataList> reads = new ArrayList<ChooseDataList>(0);
        int d;
        try{
            while( fr.read() != -1 ){
                String name = null;
                List<Integer> cdss = new ArrayList<Integer>(0);
                while( (d = fr.read()) != 10 && d != -1 ){
                    name = (name == null ? "" : name )+ (char)d;
                }
                while( (d = fr.read()) != 10 && d != -1 ){
                    String kazu = null;
                    while( d != 10 && d != -1 && (char)d != '.' ){
                        kazu = (kazu == null ? "" : kazu )+ (char)d;
                        d = fr.read();
                    }
                    cdss.add(Integer.valueOf(kazu));
                }
                
                int a[] = new int[cdss.size()];
                for(int i = 0;i < cdss.size();i++){
                    a[i] = cdss.get(i);
                }
                reads.add(new ChooseDataList(name,a));
            }
        }catch(IOException e){
            System.out.println("CDFL.lFileRead:readできなかったそうな。");
        }
        
        return reads;
    }
    
    
    
    
    public static ChooseDataSong[] getCDSs(){
        ChooseDataSong[] a = new ChooseDataSong[0];
        return cdss.toArray(a);
    }
    
    public static ChooseDataList[] getCDLs(){
        ChooseDataList[] a = new ChooseDataList[0];
        return cdls.toArray(a);
    }
    
    public static int getSongNumber(String fname){
        System.out.print("CDFL.getSongNumber"+ fname+":");
        for(int i = 0;i<cdss.size();i++){
            if(cdss.get(i).getFname().equals(fname)){System.out.println(i);
                return i;
            }
        }
        System.out.println("-1");
        return -1;
    }
    
    public static int getPlayListNumber(String lname){
        System.out.print("CDFL.getPlayListNumber"+ lname+":");
        for(int i = 0;i<cdls.size();i++){
            if(cdls.get(i).getName().equals(lname)){System.out.println(i);
                return i;
            }
        }
        System.out.println("-1");
        return -1;
    }
}