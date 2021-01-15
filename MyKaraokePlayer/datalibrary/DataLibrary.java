package datalibrary;

import java.sql.*;
import java.util.*;

public class DataLibrary{
    private Connection conn;
    public DataLibrary(){
        try{
            String url="jdbc:mysql://localhost/mkpdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC";//https://www.nakamuri.info/mw/index.php/Mysql-connector-java_%E3%81%AE%E3%83%90%E3%82%B0%E3%81%A7_Java%E3%81%8B%E3%82%89MySQL%E3%81%AB%E6%8E%A5%E7%B6%9A%E3%81%A7%E3%81%8D%E3%81%AA%E3%81%84辺りを参考に色々つけた。正直よく分かってない
            String user="mkp";
            String pass="pass";
            conn=DriverManager.getConnection(url,user,pass);
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void editSong(int songId,String name,int grade,String comment,String date,String with,String score){
        try{
            String sql="UPDATE SONGS SET"+
                " TITLE='"+escape(name)+"'"+
                ",EVAL="+grade+
                ",DATE="+(date.length()>0?("'"+escape(date)+"'"):"DEFAULT")+
                ",SCORE='"+escape(score)+"'"+
                ",COMM='"+escape(comment)+"'"+
                ",COMP='"+escape(with)+"'"+
                " WHERE ID='"+songId+"'";
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void addNewSongData(String fname,String name,int grade,String comment,String date,String with,String score){
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
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void addNewListData(String name,int[] songIds){
        try{
            String sql="INSERT INTO LISTS VALUES (0,'"+escape(name)+"')";
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
            int listid=getListId(name);
            for(int i=1;i<=songIds.length;i++){
                sql="INSERT INTO LISTSONGS VALUES ('"+listid+"',"+i+","+songIds[i-1]+")";
                //System.out.println("["+sql+"]");
                ps=conn.prepareCall(sql);
                ps.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void deleteList(int listId){
        try{
            String sql="DELETE FROM LISTS WHERE ID='"+listId+"'";
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void editList(int listId,String newName,int[] newSongIds){
        try{
            String sql="DELETE FROM LISTSONGS WHERE LISTID="+listId;
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ps.executeUpdate();
            for(int num=1;num<=newSongIds.length;num++){
                sql="INSERT INTO LISTSONGS VALUES ('"+listId+"',"+num+","+newSongIds[num-1]+")";
                //System.out.println("["+sql+"]");
                ps=conn.prepareCall(sql);
                ps.executeUpdate();
            }
            sql="UPDATE LISTS SET "+
                "LIST='"+escape(newName)+"' "+
                "WHERE ID="+listId;
            //System.out.println("["+sql+"]");
            ps=conn.prepareCall(sql);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public SongData[] getMatchSongs(String s){
        List<SongData> songs=new ArrayList<>();
        try{
            String sql="SELECT ID FROM SONGS WHERE TITLE LIKE '%"+escapeForLike(s)+"%'";
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                int songId=rs.getInt(1);
                songs.add(new SongData(this,songId));
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        return songs.toArray(new SongData[0]);
    }
    
    public ListData[] getMatchLists(String s){
        List<ListData> lists=new ArrayList<>();
        try{
            String sql="SELECT ID FROM LISTS WHERE LIST LIKE '%"+escapeForLike(s)+"%'";
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                int listId=rs.getInt(1);
                lists.add(new ListData(this,listId));
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        return lists.toArray(new ListData[0]);
    }
    public boolean existsSongFile(String fname){
        return getSongId(fname)!=-1;
    }
    private int getSongId(String fname){
        try{
            String sql="SELECT ID FROM SONGS WHERE FILE='"+escape(fname)+"'";
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                int res=rs.getInt(1);
                return res;
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        return -1;
    }
    public boolean existsListName(String lname){
        return getListId(lname)!=-1;
    }
    private int getListId(String listName){
        try{
            String sql="SELECT ID FROM LISTS WHERE LIST='"+escape(listName)+"'";
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            if(rs.next()){
                int res=rs.getInt(1);
                return res;
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        return -1;
    }
    
    String getSongName(int songId){
        return getStringValueFromTableById("TITLE","SONGS",songId);
    }
    String getSongFile(int songId){
        return getStringValueFromTableById("FILE","SONGS",songId);
    }
    String getSongDate(int songId){
        return getStringValueFromTableById("DATE","SONGS",songId);
    }
    String getSongWith(int songId){
        return getStringValueFromTableById("COMP","SONGS",songId);
    }
    String getSongComment(int songId){
        return getStringValueFromTableById("COMM","SONGS",songId);
    }
    String getSongScore(int songId){
        return getStringValueFromTableById("SCORE","SONGS",songId);
    }
    int getSongEval(int songId){
        int res=-1;
        try{
            String sql="SELECT EVAL FROM SONGS WHERE ID="+songId;
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            rs.next();
            res=rs.getInt(1);
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        return res;
    }
    String getListName(int listId){
        return getStringValueFromTableById("LIST","LISTS",listId);
    }
    SongData[] getListSongs(int listId){
        List<SongData> list=new ArrayList<>();
        try{
            String sql="SELECT SONGID FROM LISTSONGS WHERE LISTID="+listId+" ORDER BY NUM";
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            while(rs.next()){
                int songId=rs.getInt(1);
                list.add(new SongData(this,songId));
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        return list.toArray(new SongData[0]);
    }
    int getListSize(int listId){
        int res=-1;
        try{
            String sql="SELECT COUNT(NUM) FROM LISTSONGS WHERE LISTID="+listId;
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            rs.next();
            res=rs.getInt(1);
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        return res;
    }
    
    private String getStringValueFromTableById(String value,String table,int id){
        String res=null;
        try{
            String sql="SELECT "+value+" FROM "+table+" WHERE ID="+id;
            //System.out.println("["+sql+"]");
            PreparedStatement ps=conn.prepareCall(sql);
            ResultSet rs=ps.executeQuery(); 
            rs.next();
            res=rs.getString(1);
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        return res;
    }
    
    private static String escape(String raw){
        return raw.replaceAll("'","''").replaceAll("\\\\","\\\\\\\\");
    }
    private static String escapeForLike(String raw){
        return escape(raw).replaceAll("%","\\\\%").replaceAll("_","[_]");
    }
}