import java.sql.*;
import java.io.*;
import java.util.*;

public class CDFLibrary{
    static Connection conn;
    static List<ChooseDataList> cdls;  //プレイリスト用のデータベースの設計が詰められてないのでまだデータベースにしない。2020/10/03
    static PnList pnl;
    static{
        try{
            String url="jdbc:mysql://localhost/mykaraokeplayerdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC";
            String user="mkp";
            String pass="pass";
            conn=DriverManager.getConnection(url,user,pass);
        }catch(SQLException e){e.printStackTrace();}
        reload();
    }
    public static void setPnList(PnList pnl){
        CDFLibrary.pnl = pnl;
        ChooseData[] a = new ChooseData[0];
        pnl.lupdate(getCDSs());
    }
    
    public static void update(String f,boolean isSong){
        if(isSong){
            pnl.lupdate(getMatchCDSs(f));
            
        }else{
            List<ChooseDataList> result = new ArrayList<ChooseDataList>(0);
            for(ChooseDataList all : cdls){
                if(all.getName().startsWith(f)){
                    result.add(all);
                }
            }
            pnl.lupdate(result.toArray(new ChooseData[0]));
        }
    }
    public static void reload(){
        try(FileReader fr = new FileReader("C:\\Users\\Owner\\Desktop\\Karaokewavs\\L.MKP")){
            cdls = lFileRead(fr);
        }catch(FileNotFoundException e){
            System.out.println("L.MKPが開けませんでした。");
        }catch(IOException e){
            System.out.println("reloadL:frが閉じられませんでした。");
        }
    }
    
    public static void songEdit(ChooseDataSong cds){
        try{
            PreparedStatement ps=conn.prepareCall(
                "update songs set"+
                " title="+cds.getName()+
                ",eval="+cds.getGrade()+
                ",date="+cds.getDate()+
                ",score="+cds.getScore()+
                ",comm"+cds.getComment()+
                ",comp"+cds.getWith()+
                " where file='"+cds.getFname()+"'"
            );
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e);
        }
    }
    
    public static void addNewSongData(String fname,String name,int grade,String comment,String date,String with,String score){
        try{
            PreparedStatement ps=conn.prepareCall(
                "insert into songs values ("+
                "defaut,"+name+","+fname+","+grade+","+date+","+score+","+comment+","+with
                +")"
            );
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e);
        }
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
        reload();
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
        reload();
        
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
        reload();
        
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
    
    private static ChooseDataSong[] getMatchCDSs(String s){
        List<ChooseDataSong> cdss=new ArrayList<>();
        try{
            PreparedStatement ps=conn.prepareCall(
                "select file,title,eval,comm,date,comp,score from songs where title=%"+s+"%"
            );
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                String fname=rs.getNString(1);
                String name=rs.getNString(2);
                int grade=rs.getInt(3);
                String comment=rs.getNString(4);
                String date=rs.getNString(5);
                String with=rs.getNString(6);
                String score=rs.getNString(7);
                cdss.add(new ChooseDataSong(fname,name,grade,comment,date,with,score));
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return cdss.toArray(new ChooseDataSong[0]);
    }
    
    private static ChooseDataSong[] getCDSs(){
        List<ChooseDataSong> cdss=new ArrayList<>();
        try{
            PreparedStatement ps=conn.prepareCall(
                "select file,title,eval,comm,date,comp,score from songs"
            );
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                String fname=rs.getNString(1);
                String name=rs.getNString(2);
                int grade=rs.getInt(3);
                String comment=rs.getNString(4);
                String date=rs.getNString(5);
                String with=rs.getNString(6);
                String score=rs.getNString(7);
                cdss.add(new ChooseDataSong(fname,name,grade,comment,date,with,score));
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return cdss.toArray(new ChooseDataSong[0]);
    }
    
    public static String[] getFNames(){
        List<String> fns=new ArrayList<>();
        try{
            PreparedStatement ps=conn.prepareCall(
                "select file from songs"
            );
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                String fname=rs.getNString(1);
                fns.add(fname);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return fns.toArray(new String[0]);
    }
    
    public static ChooseDataSong getCDS(int i){
        try{
            PreparedStatement ps=conn.prepareCall(
                "select file,title,eval,comm,date,comp,score from songs where id="+i
            );
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                String fname=rs.getNString(1);
                String name=rs.getNString(2);
                int grade=rs.getInt(3);
                String comment=rs.getNString(4);
                String date=rs.getNString(5);
                String with=rs.getNString(6);
                String score=rs.getNString(7);
                return new ChooseDataSong(fname,name,grade,comment,date,with,score);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }
    
    public static ChooseDataSong getCDS(String fname){
        try{
            PreparedStatement ps=conn.prepareCall(
                "select title,eval,comm,date,comp,score from songs where file="+fname
            );
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                String name=rs.getNString(1);
                int grade=rs.getInt(2);
                String comment=rs.getNString(3);
                String date=rs.getNString(4);
                String with=rs.getNString(5);
                String score=rs.getNString(6);
                return new ChooseDataSong(fname,name,grade,comment,date,with,score);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }
    
    
    public static int getSongNumber(String fname){
        try{
            PreparedStatement ps=conn.prepareCall(
                "select id from songs where file="+fname
            );
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                int id=rs.getInt(1);
                return id;
            }
        }catch(SQLException e){
            System.out.println(e);
        }
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