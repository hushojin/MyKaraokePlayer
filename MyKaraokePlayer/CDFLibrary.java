import java.sql.*;
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
    
    public static void songEdit(ChooseDataSong cds){
        try{
            String sql="UPDATE SONGS SET"+
                " TITLE='"+escape(cds.getName())+"'"+
                ",EVAL="+cds.getGrade()+
                ",DATE="+(cds.getDate().length()>0?("'"+escape(cds.getDate())+"'"):"DEFAULT")+
                ",SCORE='"+escape(cds.getScore())+"'"+
                ",COMM='"+escape(cds.getComment())+"'"+
                ",COMP='"+escape(cds.getWith())+"'"+
                " WHERE FILE='"+escape(cds.getFname())+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
/**************************************************
public static void songEdit(int id,String name,int grade,String comment,String date,String with,String score){
    try{
            String sql="UPDATE SONGS SET"+
                " TITLE='"+escape(name)+"'"+
                ",EVAL="+grade+
                ",DATE="+date.length()>0?("'"+escape(date)+"'"):"DEFAULT")+
                ",SCORE='"+escape(score)+"'"+
                ",COMM='"+escape(comment)+"'"+
                ",COMP='"+escape(with)+"'"+
                " WHERE ID='"+id+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
}
*******************************************************/
    
    public static void addNewSongData(String fname,String name,int grade,String comment,String date,String with,String score){
        try{
            String sql="INSERT INTO SONGS VALUES ("+
                "DEFAULT,'"+
                escape(name)+"','"+
                escape(fname)+"',"+
                grade+","+(date.length()>0?date:"DEFAULT")+",'"+
                escape(score)+"','"+
                escape(comment)+"','"+
                escape(with)
            +"')";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
/****************************
SAME
****************************/
    
    public static void addNewPlayListData(String name,int[] cdss){
        try{
            String sql="INSERT INTO LISTS VALUES (0,'"+escape(name)+"')";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
            int listid=getPlayListNumber(name);
            for(int i=1;i<=cdss.length;i++){
                sql="INSERT INTO LISTSONGS VALUES ('"+listid+"',"+i+","+cdss[i-1]+")";
                System.out.println("["+sql+"]");
                ps=conn.prepareCall(sql);
                ps.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
/********************************************
SAME
********************************************/
    
    public static void playListDelete(String lname){
        try{
            String sql="DELETE FROM LISTS WHERE LIST='"+escape(lname)+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
/********************************************
public static void playListDelete(String lname){
playListDelete(getPlayListNumber(lname))
}
public static void playListDelete(int id){
        try{
            String sql="DELETE FROM LISTS WHERE ID='"+id+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
********************************************/
    
    public static void playListEdit(String oldName,ChooseDataList cdl){
        playListDelete(oldName);
        addNewPlayListData(cdl.getName(),cdl.getSongs());
    }
/*************************************************
public static void playListEdit(int id,String newName,int[] newSongIDs){
    try{
        String sql="DELETE FROM LISTSONGS WHERE LISTID="+id;
        System.out.println("["+sql+"]");
        for(int i=1;i<=NewSongIDs.length;i++){
                sql="INSERT INTO LISTSONGS VALUES ('"+id+"',"+i+","+newSongIDs[i-1]+")";
                System.out.println("["+sql+"]");
                PreparedStatement ps=conn.prepareCall(sql);
                ps.executeUpdate();
        }
        sql="UPDATE LISTS SET "+
            "LIST='"+escape(newName)+"' "+
            "WHERE ID="+id;
        System.out.println("["+sql+"]");
        PreparedStatement ps=conn.prepareCall(sql);
        ps.executeUpdate();
    }catch(SQLException e){
        e.printStackTrace();
    }
}
*************************************************/
    
    public static ChooseDataSong[] getMatchCDSs(String s){
        List<ChooseDataSong> cdss=new ArrayList<>();
        try{
            String sql="SELECT FILE,TITLE,EVAL,COMM,DATE,COMP,SCORE FROM SONGS WHERE TITLE LIKE '%"+escapeForLike(s)+"%'";
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
/**************************************************
public static ChooseDataSong[] getMatchCDSs(String s){
        List<ChooseDataSong> cdss=new ArrayList<>();
        try{
            String sql="SELECT ID FROM SONGS WHERE TITLE LIKE '%"+escapeForLike(s)+"%'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                int id=rs.getInt(1);
                cdss.add(new ChooseDataSong(id));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return cdss.toArray(new ChooseDataSong[0]);
}
**************************************************/
    
    public static ChooseDataList[] getMatchCDLs(String s){
        List<ChooseDataList> cdls=new ArrayList<>();
        try{
            String sql="SELECT LIST FROM LISTS WHERE LIST LIKE '%"+escapeForLike(s)+"%'";
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
/***************************************************
public static ChooseDataList[] getMatchCDLs(String s){
        List<ChooseDataList> cdls=new ArrayList<>();
        try{
            String sql="SELECT ID FROM LISTS WHERE LIST LIKE '%"+escapeForLike(s)+"%'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                cdls.add(getCDL(rs.getInt(1)));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return cdls.toArray(new ChooseDataList[0]);
    }
***************************************************/
    
    public static String[] getFNames(){
        List<String> fns=new ArrayList<>();
        try{
            String sql="SELECT FILE FROM SONGS";
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
/****************************************************
SAME
****************************************************/
    
    public static ChooseDataSong getCDS(int i){
        try{
            String sql="SELECT FILE,TITLE,EVAL,COMM,DATE,COMP,SCORE FROM SONGS WHERE ID="+i;
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
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
/*******************************************************
public static ChooseDataSong getCDS(int i){
        try{
            String sql="SELECT ID FROM SONGS WHERE ID="+i;
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                return new ChooseDataSong(id);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
*******************************************************/
    
    public static ChooseDataSong getCDS(String fname){
        return getCDS(getSongNumber(fname));
    }
/***
SAME
***/
    
    public static ChooseDataList getCDL(String lname){
        try{
            String sql="SELECT SONGID FROM LISTSONGS WHERE LISTID='"+getPlayListNumber(lname)+"' ORDER BY NUM";
            System.out.println("["+sql+"]");
            List<Integer> cdss=new ArrayList<>();
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                int songid=rs.getInt(1);
                cdss.add(songid);
            }
            int[] ary=new int[cdss.size()];
            for(int i=0;i<cdss.size();i++){
                ary[i]=cdss.get(i);
            }
            return new ChooseDataList(lname,ary);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
/***********************************************
public static ChooseDataList getCDL(int id){
    try{
            String sql="SELECT SONGID FROM LISTS WHERE LISTID='"+id+"' ORDER BY NUM";
            System.out.println("["+sql+"]");
            List<Integer> cdss=new ArrayList<>();
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                int songid=rs.getInt(1);
                return new ChooseDataList(songid);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
}
public static ChooseDataList getCDL(String lname){
return getCDL(getListNumber(lname));
}
***********************************************/
    
    
    public static int getSongNumber(String fname){
        try{
            String sql="SELECT ID FROM SONGS WHERE FILE='"+escape(fname)+"'";
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
            String sql="SELECT ID FROM LISTS WHERE LIST='"+escape(lname)+"'";
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
    private static String escape(String raw){
        return raw.replaceAll("'","''").replaceAll("\\\\","\\\\\\\\");
    }
    private static String escapeForLike(String raw){
        return escape(raw).replaceAll("%","\\\\%").replaceAll("_","[_]");
    }
}