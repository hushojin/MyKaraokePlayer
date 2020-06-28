import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChooseDataSong extends ChooseData{
    String fname;
    String name;
    int grade;
    String comment;
    String date;
    String with;
    String score;
    
    ChooseDataSong(String fname,String name,int grade,String comment,String date,String with,String score){
        this.fname=fname;
        this.name=name;
        this.grade=grade;
        this.comment=comment;
        this.date=date;
        this.with=with;
        this.score=score;
    }
    
    String getFname(){
        return fname;
    }
    String getName(){
        return name;
    }
    int getGrade(){
        return grade;
    }
    String getComment(){
        return comment;
    }
    String getDate(){
        return date;
    }
    String getWith(){
        return with;
    }
    String getScore(){
        return score;
    }
    Boolean isSong(){return true;}
}