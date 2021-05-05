package design.view;

import design.controllers.Controller;
import design.gui.GEmpty;
import design.gui.GShelf;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class HeatmapView {

    private Stage heatmap;
    private int rect_s;

    private ArrayList<GShelf> gshelves;
    private ArrayList<GEmpty> gempties;

    private GridPane heatm;

    public HeatmapView(){
        this.heatmap = new Stage();
        heatmap.setTitle("HeatMap");
        heatmap.setWidth(400);
        heatmap.setHeight(300);
        Controller.storage.getRect_s();
        heatm= new GridPane();

        gshelves = new ArrayList<GShelf>();
        gempties = new ArrayList<GEmpty>();
    }

    public int getMaxVal(ArrayList<GShelf> sh){
        int cnt=0;
        for(GShelf s : sh){
            if (s.getShelf().getHeatCounter()>cnt){
                cnt = s.getShelf().getHeatCounter();
            }
        }
    return cnt;
    }
    public int getMinVal(ArrayList<GShelf> sh){
        int cnt=Integer.MAX_VALUE;
        for(GShelf s : sh){
            if (s.getShelf().getHeatCounter()<cnt){
                cnt = s.getShelf().getHeatCounter();
            }
        }
    return cnt;
    }

    public void drawHeatmap(Group root, Scene info,ArrayList<GShelf> shelfs,int x, int y){

        VBox vbox= new VBox();
        vbox.getChildren().add(heatm);
        int rect_size = 700/((x>y)? x : y);

        float maxVal = (float) getMaxVal(shelfs);
        float minVal = (float) getMinVal(shelfs);

        System.out.println("MAX: "+maxVal);
        System.out.println("MIN: "+minVal);

        for(int i=0;i<y;i++){
            for(int j=0;j<x;j++){
                GEmpty g = new GEmpty(Controller.storage);
                g.drawEmptyHeat(j,i,heatm);
                g.rectSize(rect_size,rect_size);
                gempties.add(g);

            }


        }

        for(GShelf s : shelfs){
            GShelf gshelf = new GShelf(s.getShelf(), Controller.storage);
            gshelf.drawHeat(heatm);
            gshelf.rectSize(rect_size,rect_size);

            float val= s.getShelf().getHeatCounter();
            float ratio = 2 * (val-minVal) / (maxVal - minVal);
            int b = (int)Math.max(0, 255*(1 - ratio));
            int r = (int)Math.max(0, 255*(ratio - 1));
            int g = 255 - b - r;

            Color my = new Color(r,g,b);

            gshelf.setColor(my);
            gshelves.add(gshelf);
            //heatm.add(gshelf,s.getShelf().getPosX(),s.getShelf().getPosY());

        }

        ImageView bar = new ImageView("/design/res/grad.png");

        vbox.setSpacing(10);
        vbox.getChildren().add(bar);

        root.getChildren().add(vbox);



    }


    public Stage getStage(){
        return this.heatmap;
    }


}
