import java.sql.*;
import java.io.*;
import java.util.*;

public class CDFLibrary{
    static Connection conn;
    static{
        try{
            String url="jdbc:mysql://localhost/mkpdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC";
            String user="mkp";
            String pass="pass";
            conn=DriverManager.getConnection(url,user,pass);
        }catch(SQLException e){e.printStackTrace();}
    }
    
    public static void update(LsList lsl,String f,boolean isSong){
        if(isSong){
            lsl.lupdate(getMatchCDSs(f));
        }else{
            lsl.lupdate(getMatchCDLs(f));
        }
    }
    
    public static void songEdit(ChooseDataSong cds){
        try{
            String sql="update songs set"+
                " title='"+cds.getName().replaceAll("'","''")+"'"+
                ",eval="+cds.getGrade()+
                ",date="+(cds.getDate().length()>0?("'"+cds.getDate().replaceAll("'","''")+"'"):"default")+
                ",score='"+cds.getScore().replaceAll("'","''")+"'"+
                ",comm='"+cds.getComment().replaceAll("'","''")+"'"+
                ",comp='"+cds.getWith().replaceAll("'","''")+"'"+
                " where file='"+cds.getFname().replaceAll("'","''")+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public static void addNewSongData(String fname,String name,int grade,String comment,String date,String with,String score){
        try{
            String sql="insert into songs values ("+
                "default,'"+
                name.replaceAll("'","''")+"','"+
                fname.replaceAll("'","''")+"',"+
                grade+","+(date.length()>0?date:"default")+",'"+
                score.replaceAll("'","''")+"','"+
                comment.replaceAll("'","''")+"','"+
                with.replaceAll("'","''")
            +"')";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public static void addNewPlayListData(String name,int[] cdss){
        try{
            String sql="insert into lists values (0,'"+
                name.replaceAll("'","''")+
            "')";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
            for(int i=1;i<=cdss.length;i++){
                sql="insert into listsongs values ('"+
                    name.replaceAll("'","''")+"',"+
                    i+","+
                    cdss[i-1]+
                ")";
                System.out.println("["+sql+"]");
                ps=conn.prepareCall(sql);
                ps.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void playListDelete(String name){
        try{
            String sql="delete from lists where list='"+name.replaceAll("'","''")+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void playListEdit(String oldName,ChooseDataList cdl){
        playListDelete(oldName);
        addNewPlayListData(cdl.getName(),cdl.getSongs());
    }
    private static ChooseDataSong[] getMatchCDSs(String s){
        List<ChooseDataSong> cdss=new ArrayList<>();
        try{
            String sql="select file,title,eval,comm,date,comp,score from songs where title like '%"+s.replaceAll("%","\\%").replaceAll("_","[_]").replaceAll("'","''")+"%'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                String fname=rs.getString(1);
                String name=rs.getString(2);
                int grade=rs.getInt(3);
                String comment=rs.getString(4);
                String date=rs.getString(5);
                String with=rs.getString(6);
                String score=rs.getString(7);
                cdss.add(new ChooseDataSong(fname,name,grade,comment,date,with,score));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return cdss.toArray(new ChooseDataSong[0]);
    }
    private static ChooseDataList[] getMatchCDLs(String s){
        List<ChooseDataList> cdls=new ArrayList<>();
        try{
            String sql="select list from lists where list like '%"+s.replaceAll("%","\\%").replaceAll("_","[_]").replaceAll("'","''")+"%'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                cdls.add(getCDL(rs.getString(1)));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return cdls.toArray(new ChooseDataList[0]);
    }
    
    private static ChooseDataSong[] getCDSs(){
        List<ChooseDataSong> cdss=new ArrayList<>();
        try{
            PreparedStatement ps=conn.prepareCall(
                "select file,title,eval,comm,date,comp,score from songs"
            );
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                String fname=rs.getString(1);
                String name=rs.getString(2);
                int grade=rs.getInt(3);
                String comment=rs.getString(4);
                String date=rs.getString(5);
                String with=rs.getString(6);
                String score=rs.getString(7);
                cdss.add(new ChooseDataSong(fname,name,grade,comment,date,with,score));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return cdss.toArray(new ChooseDataSong[0]);
    }
    
    public static String[] getFNames(){
        List<String> fns=new ArrayList<>();
        try{
            String sql=("select file from songs");
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                String fname=rs.getString(1);
                fns.add(fname);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return fns.toArray(new String[0]);
    }
    
    public static ChooseDataSong getCDS(int i){
        try{
            String sql="select file,title,eval,comm,date,comp,score from songs where id="+i;
            PreparedStatement ps=conn.prepareCall(sql);
            System.out.println("["+sql+"]");
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                String fname=rs.getString(1);
                String name=rs.getString(2);
                int grade=rs.getInt(3);
                String comment=rs.getString(4);
                String date=rs.getString(5);
                String with=rs.getString(6);
                String score=rs.getString(7);
                return new ChooseDataSong(fname,name,grade,comment,date,with,score);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static ChooseDataSong getCDS(String fname){
        try{
            String sql="select title,eval,comm,date,comp,score from songs where file='"+fname.replace("'","''")+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                String name=rs.getString(1);
                int grade=rs.getInt(2);
                String comment=rs.getString(3);
                String date=rs.getString(4);
                String with=rs.getString(5);
                String score=rs.getString(6);
                return new ChooseDataSong(fname,name,grade,comment,date,with,score);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static ChooseDataList getCDL(String lname){
        try{
            String sql="select songid from listsongs where list='"+lname.replace("'","''")+"'";
            List<Integer> cdss=new ArrayList<>(); 
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                int songid=rs.getInt(1);
                cdss.add(songid);
            }
            if(!cdss.isEmpty()){
                int[] ary=new int[cdss.size()];
                for(int i=0;i<cdss.size();i++){
                    ary[i]=cdss.get(i);
                }
                return new ChooseDataList(lname,ary);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static int getSongNumber(String fname){
        try{
            String sql="select id from songs where file='"+fname.replace("'","''")+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                int id=rs.getInt(1);
                return id;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
    public static int getPlayListNumber(String lname){
        try{
            String sql="select id from lists where list='"+lname.replace("'","''")+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                int id=rs.getInt(1);
                return id;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
}