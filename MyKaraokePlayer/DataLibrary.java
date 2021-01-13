import java.sql.*;
import java.util.*;

public class DataLibrary{
    static Connection conn;
    static{
        try{
            String url="jdbc:mysql://localhost/mkpdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC";
            String user="mkp";
            String pass="pass";
            conn=DriverManager.getConnection(url,user,pass);
        }catch(SQLException e){e.printStackTrace();}
    }
    
    public static void editSong(int id,String name,int grade,String comment,String date,String with,String score){
        try{
            String sql="UPDATE SONGS SET"+
                " TITLE='"+escape(name)+"'"+
                ",EVAL="+grade+
                ",DATE="+(date.length()>0?("'"+escape(date)+"'"):"DEFAULT")+
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
    
    public static void addNewListData(String name,int[] cdss){
        try{
            String sql="INSERT INTO LISTS VALUES (0,'"+escape(name)+"')";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
            int listid=getListId(name);
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
    
    public static void deleteList(int id){
        try{
            String sql="DELETE FROM LISTS WHERE ID='"+id+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public static void editList(int id,String newName,int[] newSongIDs){
        try{
            String sql="DELETE FROM LISTSONGS WHERE LISTID="+id;
            System.out.println("["+sql+"]");
            for(int i=1;i<=newSongIDs.length;i++){
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
    
    public static SongData[] getMatchCDSs(String s){
        List<SongData> cdss=new ArrayList<>();
        try{
            String sql="SELECT ID FROM SONGS WHERE TITLE LIKE '%"+escapeForLike(s)+"%'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                int id=rs.getInt(1);
                cdss.add(new SongData(id));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return cdss.toArray(new SongData[0]);
    }
    
    public static ListData[] getMatchCDLs(String s){
        List<ListData> cdls=new ArrayList<>();
        try{
            String sql="SELECT ID FROM LISTS WHERE LIST LIKE '%"+escapeForLike(s)+"%'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                int id=rs.getInt(1);
                cdls.add(new ListData(id));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return cdls.toArray(new ListData[0]);
    }
    public static boolean existsSongFile(String fname){
        return getSongId(fname)!=-1;
    }
    private static int getSongId(String fname){
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
    public static boolean existsListName(String lname){
        return getListId(lname)!=-1;
    }
    private static int getListId(String lname){
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
    
    public static String getSongName(int id){
        return getStringValueFromTableById("TITLE","SONGS",id);
    }
    public static String getSongFile(int id){
        return getStringValueFromTableById("FILE","SONGS",id);
    }
    public static String getSongDate(int id){
        return getStringValueFromTableById("DATE","SONGS",id);
    }
    public static String getSongWith(int id){
        return getStringValueFromTableById("COMP","SONGS",id);
    }
    public static String getSongComment(int id){
        return getStringValueFromTableById("COMM","SONGS",id);
    }
    public static String getSongScore(int id){
        return getStringValueFromTableById("SCORE","SONGS",id);
    }
    public static int getSongEval(int id){
        try{
            String sql="SELECT EVAL FROM SONGS WHERE ID="+id;
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                int res=rs.getInt(1);
                return res;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public static String getListName(int id){
        return getStringValueFromTableById("LIST","LISTS",id);
    }
    public static SongData[] getListSongs(int id){
        List<SongData> list=new ArrayList<>();
        try{
            String sql="SELECT SONGID FROM LISTSONGS WHERE LISTID="+id+" ORDER BY NUM";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                int songid=rs.getInt(1);
                list.add(new SongData(songid));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list.toArray(new SongData[0]);
    }
    public static int getListSize(int id){
        try{
            String sql="SELECT COUNT(NUM) FROM LISTSONGS WHERE LISTID="+id;
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                int size=rs.getInt(1);
                return size;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    
    private static String getStringValueFromTableById(String value,String table,int id){
        try{
            String sql="SELECT "+value+" FROM "+table+" WHERE ID="+id;
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                String res=rs.getString(1);
                return res;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    private static String escape(String raw){
        return raw.replaceAll("'","''").replaceAll("\\\\","\\\\\\\\");
    }
    private static String escapeForLike(String raw){
        return escape(raw).replaceAll("%","\\\\%").replaceAll("_","[_]");
    }
}