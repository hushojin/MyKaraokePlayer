import java.io.*;
import javax.swing.*;

public class ChooseDataList extends ChooseData{
    String name;
    int cdsl[];
    ChooseDataList(String name,int[] cdsl){
        this.name = name;
        this.cdsl = cdsl;
    }
    
    String getName(){
        return name;
    }
    
    int getSong(int index){
        return cdsl[index];
    }
    int[] getSongs(){
        return cdsl;
    }
    
    int number(){
        return cdsl.length;
    }
    boolean isSong(){return false;}
}