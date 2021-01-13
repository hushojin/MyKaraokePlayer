import java.awt.*;
import java.awt.event.*;

public class CDSEditor extends Dialog{
    CDSEditor(SongData cds){
        super((Frame)null,"曲編集",true);
        
        Label fnl = new Label("ファイル名:",Label.RIGHT);
        Label fname = new Label(cds.getFname());
        
        Label nl = new Label("曲名",Label.RIGHT);
        TextField name = new TextField(cds.getName());
        
        Label gl = new Label("評価:",Label.RIGHT);
        TextField grade = new TextField(String.valueOf(cds.getGrade()));
        
        Label cl = new Label("コメント:",Label.RIGHT);
        TextField comment = new TextField(cds.getComment());
        
        Label wl = new Label("同伴:",Label.RIGHT);
        TextField with = new TextField(cds.getWith());
        
        Label dl = new Label("日付:",Label.RIGHT);
        TextField date = new TextField(cds.getDate());
        
        Label sl = new Label("スコア:",Label.RIGHT);
        TextField score = new TextField(cds.getScore());
        
        Button finish = new Button("保存");
        Button cansell = new Button("キャンセル");
        
        addWindowListener(
            new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    setVisible(false);
                }
            }
        );
        
        add(fnl);
        fnl.setBounds(20,40,70,20);
        add(fname);
        fname.setBounds(fnl.getX()+fnl.getWidth(),fnl.getY(),200,20);
        
        add(nl);
        nl.setBounds(20,70,70,20);
        add(name);
        name.setBounds(nl.getX()+nl.getWidth(),nl.getY(),200,20);
        
        add(gl);
        gl.setBounds(20,100,70,20);
        add(grade);
        grade.setBounds(gl.getX()+gl.getWidth(),gl.getY(),30,20);
        
        add(cl);
        cl.setBounds(20,130,70,20);
        add(comment);
        comment.setBounds(cl.getX()+cl.getWidth(),cl.getY(),200,20);
        
        add(dl);
        dl.setBounds(20,160,70,20);
        add(date);
        date.setBounds(dl.getX()+dl.getWidth(),dl.getY(),200,20);
        
        
        add(wl);
        wl.setBounds(20,190,70,20);
        add(with);
        with.setBounds(wl.getX()+wl.getWidth(),wl.getY(),200,20);
        
        add(sl);
        sl.setBounds(20,220,70,20);
        add(score);
        score.setBounds(sl.getX()+sl.getWidth(),sl.getY(),200,20);
        
        add(finish);
        finish.setBounds(150,250,70,20);
        finish.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    CDFLibrary.songEdit(cds.getId(),name.getText(),Integer.valueOf(grade.getText()),comment.getText(),date.getText(),with.getText(),score.getText());
                    setVisible(false);
                }
            }
        );
        add(cansell);
        cansell.setBounds(240,250,70,20);
        cansell.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    setVisible(false);
                }
            }
        );
        
        setSize(380,290);
        setResizable(false);
        setLayout(null);
        setVisible(true);
    }
}