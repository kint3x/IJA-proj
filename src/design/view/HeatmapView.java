package design.view;

import design.controllers.Controller;
import design.gui.GEmpty;
import design.gui.GShelf;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    /**
     * Konštruktor triedy zobrazujúcej heatmapu
     */
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

    /**
     * Získa maximálnu hodnotu z heatmapy
     * @param sh pole s regálmi
     * @return maximálna hodnota
     */
    public int getMaxVal(ArrayList<GShelf> sh){
        int cnt=0;
        for(GShelf s : sh){
            if (s.getShelf().getHeatCounter()>cnt){
                cnt = s.getShelf().getHeatCounter();
            }
        }
    return cnt;
    }
    /**
     * Získa minimálnu hodnotu z heatmapy
     * @param sh pole s regálmi
     * @return minimálna hodnota
     */
    public int getMinVal(ArrayList<GShelf> sh){
        int cnt=Integer.MAX_VALUE;
        for(GShelf s : sh){
            if (s.getShelf().getHeatCounter()<cnt){
                cnt = s.getShelf().getHeatCounter();
            }
        }
    return cnt;
    }

    /**
     * Vykreslí heatmapu a zmestí ju do okna 700*700px
     * @param root Skupina kde sa bude vykreslovať
     * @param info Scéna do ktorej sa bude vykreslovať
     * @param shelfs Pole s regálmi
     * @param x šírka skladu
     * @param y výška skladu
     */
    public void drawHeatmap(Group root, Scene info,ArrayList<GShelf> shelfs,int x, int y){

        VBox vbox= new VBox();
        vbox.getChildren().add(heatm);
        int rect_size = 700/((x>y)? x : y);

        float maxVal = (float) getMaxVal(shelfs);
        float minVal = (float) getMinVal(shelfs);


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
            if(maxVal == 0){
                my = new Color(0,0,255);
            }
            gshelf.setColor(my);
            gshelves.add(gshelf);
            //heatm.add(gshelf,s.getShelf().getPosX(),s.getShelf().getPosY());

        }

        ImageView bar = new ImageView("/design/res/grad.png");
        Label mer = new Label();
        mer.setText(minVal + " - " + maxVal);
        vbox.setSpacing(10);
        vbox.getChildren().add(bar);
        vbox.getChildren().add(mer);

        root.getChildren().add(vbox);



    }


    /**
     * Vráti inštanciu Stageu s ktorým trieda pracuje
     * @return Stage
     */
    public Stage getStage(){
        return this.heatmap;
    }


}
