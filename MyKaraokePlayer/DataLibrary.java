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
    
    public static void editSong(int songId,String name,int grade,String comment,String date,String with,String score){
        try{
            String sql="UPDATE SONGS SET"+
                " TITLE='"+escape(name)+"'"+
                ",EVAL="+grade+
                ",DATE="+(date.length()>0?("'"+escape(date)+"'"):"DEFAULT")+
                ",SCORE='"+escape(score)+"'"+
                ",COMM='"+escape(comment)+"'"+
                ",COMP='"+escape(with)+"'"+
                " WHERE ID='"+songId+"'";
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
    
    public static void addNewListData(String name,int[] songIds){
        try{
            String sql="INSERT INTO LISTS VALUES (0,'"+escape(name)+"')";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
            int listid=getListId(name);
            for(int i=1;i<=songIds.length;i++){
                sql="INSERT INTO LISTSONGS VALUES ('"+listid+"',"+i+","+songIds[i-1]+")";
                System.out.println("["+sql+"]");
                ps=conn.prepareCall(sql);
                ps.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public static void deleteList(int listId){
        try{
            String sql="DELETE FROM LISTS WHERE ID='"+listId+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public static void editList(int listId,String newName,int[] newSongIds){
        try{
            String sql="DELETE FROM LISTSONGS WHERE LISTID="+listId;
            System.out.println("["+sql+"]");
            for(int num=1;num<=newSongIds.length;num++){
                sql="INSERT INTO LISTSONGS VALUES ('"+listId+"',"+num+","+newSongIds[num-1]+")";
                System.out.println("["+sql+"]");
                PreparedStatement ps=conn.prepareCall(sql);
                ps.executeUpdate();
            }
            sql="UPDATE LISTS SET "+
                "LIST='"+escape(newName)+"' "+
                "WHERE ID="+listId;
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public static SongData[] getMatchSongs(String s){
        List<SongData> songs=new ArrayList<>();
        try{
            String sql="SELECT ID FROM SONGS WHERE TITLE LIKE '%"+escapeForLike(s)+"%'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                int songId=rs.getInt(1);
                songs.add(new SongData(songId));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return songs.toArray(new SongData[0]);
    }
    
    public static ListData[] getMatchLists(String s){
        List<ListData> lists=new ArrayList<>();
        try{
            String sql="SELECT ID FROM LISTS WHERE LIST LIKE '%"+escapeForLike(s)+"%'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                int listId=rs.getInt(1);
                lists.add(new ListData(listId));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return lists.toArray(new ListData[0]);
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
                int songId=rs.getInt(1);
                return songId;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
    public static boolean existsListName(String lname){
        return getListId(lname)!=-1;
    }
    private static int getListId(String listName){
        try{
            String sql="SELECT ID FROM LISTS WHERE LIST='"+escape(listName)+"'";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                int listId=rs.getInt(1);
                return listId;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
    
    public static String getSongName(int songId){
        return getStringValueFromTableById("TITLE","SONGS",songId);
    }
    public static String getSongFile(int songId){
        return getStringValueFromTableById("FILE","SONGS",songId);
    }
    public static String getSongDate(int songId){
        return getStringValueFromTableById("DATE","SONGS",songId);
    }
    public static String getSongWith(int songId){
        return getStringValueFromTableById("COMP","SONGS",songId);
    }
    public static String getSongComment(int songId){
        return getStringValueFromTableById("COMM","SONGS",songId);
    }
    public static String getSongScore(int songId){
        return getStringValueFromTableById("SCORE","SONGS",songId);
    }
    public static int getSongEval(int songId){
        try{
            String sql="SELECT EVAL FROM SONGS WHERE ID="+songId;
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
    public static String getListName(int listId){
        return getStringValueFromTableById("LIST","LISTS",listId);
    }
    public static SongData[] getListSongs(int listId){
        List<SongData> list=new ArrayList<>();
        try{
            String sql="SELECT SONGID FROM LISTSONGS WHERE LISTID="+listId+" ORDER BY NUM";
            System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                int songId=rs.getInt(1);
                list.add(new SongData(songId));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list.toArray(new SongData[0]);
    }
    public static int getListSize(int listId){
        try{
            String sql="SELECT COUNT(NUM) FROM LISTSONGS WHERE LISTID="+listId;
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