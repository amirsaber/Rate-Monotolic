package Rate_Monotonic;

import java.util.Stack;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Rate_Monotonic.Process;
import Rate_Monotonic.InformationEnter;
import static javafx.application.Application.STYLESHEET_MODENA;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Rate_Monotonic_Class extends Application {

    @Override
    public void start(Stage primaryStage) {
        Process proc1 = new Process();
        Process proc2 = new Process();
        Process proc3 = new Process();
        BorderPane borderPane = new BorderPane();

        Line lP3 = new Line(20, 500, 620, 500);

        lP3.setFill(Color.BLACK);
        lP3.setStroke(Color.BLACK);
        Pane root = new Pane();
        InformationEnter t = new InformationEnter();
        Button btn = new Button("Start CPU");

        t.tf1.setFont(Font.font("SanSerif", 13));
        t.tf1.setPromptText("execution time for 1");
        t.tf1.getStyleClass().add("field-back");
        t.tf1.setMaxWidth(300);

        t.tf2.setFont(Font.font("SanSerif", 13));
        t.tf2.setPromptText("execution time for 2");
        t.tf2.getStyleClass().add("field-back");
        t.tf2.setMaxWidth(300);

        t.tf3.setFont(Font.font("SanSerif", 13));
        t.tf3.setPromptText("execution time for 3");
        t.tf3.getStyleClass().add("field-back");
        t.tf3.setMaxWidth(300);

        t.tf4.setFont(Font.font("SanSerif", 13));
        t.tf4.setPromptText("Period for 1");
        t.tf4.getStyleClass().add("field-back");
        t.tf4.setMaxWidth(300);

        t.tf5.setFont(Font.font("SanSerif", 13));
        t.tf5.setPromptText("Period for 2");
        t.tf5.getStyleClass().add("field-back");
        t.tf5.setMaxWidth(300);

        t.tf6.setFont(Font.font("SanSerif", 13));
        t.tf6.setPromptText("Period for 3");
        t.tf6.getStyleClass().add("field-back");
        t.tf6.setMaxWidth(300);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int p1, p2, p3, p4, p5, p6;
                Integer[] a = t.returnValues();
                p1 = proc1.time = a[0];
                p2 = proc2.time = a[1];
                p3 = proc3.time = a[2];
                p4 = proc1.period = a[3];
                p5 = proc2.period = a[4];
                p6 = proc3.period = a[5];
                proc1.name = "P1";
                proc2.name = "P2";
                proc3.name = "P3";

                int lcm = calRms(p4, p5, p6);

                System.out.println("rms" + lcm);
                int sector = 600 / lcm;
                int start = 20 + sector;

                Process fPirority, sPirority, thPirority;
                Process[] pir = calPir(p4, p5, p6, proc1, proc2, proc3);
                fPirority = pir[0];
                sPirority = pir[1];
                thPirority = pir[2];
                int fExc = 0;
                int sExc = 0;
                int thExc = 0;
                Stack s = new Stack();
                Text fName = new Text(fPirority.name);
                fName.setX(5);
                fName.setY(180);
                Text sName = new Text(sPirority.name);
                sName.setX(5);
                sName.setY(280);
                Text thName = new Text(thPirority.name);
                thName.setX(5);
                thName.setY(380);
                int i = 0;
                int fTime = fPirority.time;
                int sTime = sPirority.time;
                int thTime = thPirority.time;
                root.getChildren().addAll(fName, sName, thName);
                Line l1;
                while (i < lcm) {
                    l1 = new Line(start, 495, start, 505);
                    Rectangle r = new Rectangle();
                    Text txt = new Text(Integer.toString(i + 1));
                    txt.setX(start);
                    txt.setY(520);
                    start += sector;
                    if (i % fPirority.period == 0) {
                        fTime--;
                        if (fTime < fPirority.time && fTime > 0) {
                            s.push(fPirority);
                        }
                        if (i % sPirority.period == 0 && fExc != 0) {
                            s.push(sPirority);
                        } else if (i % thPirority.period == 0 && fExc != 0) {
                            s.push(thPirority);
                        }
                        root.getChildren().addAll(fPir(sector, i));
                        fExc++;
                        i++;

                    } else if (!s.empty() && s.peek() == fPirority) {
                        Process p = new Process();
                        p = (Process) s.pop();

                        root.getChildren().add(fPir(sector, i));
                        if (i % sPirority.period == 0) {
                            s.push(sPirority);
                        } else if (i % thPirority.period == 0) {
                            s.push(thPirority);
                        } else;
                        fTime--;
                        if (fTime == 0) {
                            fTime = fPirority.time;
                        } else if (fTime < fPirority.time) {
                            s.push(fPirority);
                        }
                        i++;
                    } else if (i % sPirority.period == 0 || sExc == 0) {
                        sExc++;
                        sTime--;
                        root.getChildren().addAll(sPir(sector, i));
                        if (sTime < sPirority.time && sTime > 0) {
                            s.push(sPirority);
                        }
                        i++;
                    } else if (!s.empty() && s.peek() == sPirority) {
                        Process p = new Process();
                        p = (Process) s.pop();
                        root.getChildren().add(sPir(sector, i));
                        sTime--;
                        if (sTime == 0) {
                            sTime = sPirority.time;
                        } else if (sTime < sPirority.time && sTime > 0) {
                            s.push(sPirority);
                        }
                        i++;
                    } else if (i % thPirority.period == 0 || thExc == 0) {
                        root.getChildren().add(thPir(sector, i));
                        thExc++;
                        thTime--;
                        if (thTime < thPirority.time && thTime > 0) {
                            s.push(thPirority);
                        }
                        i++;
                    } else if (!s.empty() && s.peek() == thPirority) {
                        Process p = new Process();
                        p = (Process) s.pop();
                        root.getChildren().add(thPir(sector, i));
                        thTime--;
                        if (thTime == 0) {
                            thTime = thPirority.time;
                        } else if (thTime < thPirority.time && thTime > 0) {
                            s.push(thPirority);
                        }
                        i++;
                    } else {
                        i++;
                    }
                    root.getChildren().addAll(l1, txt);
                }
            }
        });

        root.getChildren().addAll(lP3);
        borderPane.setCenter(root);
        root.setPadding(new Insets(50, 350, 0, 50));
        borderPane.setMargin(root, new Insets(0, 400, 0, 0));
        borderPane.setRight(btn);
        btn.setAlignment(Pos.TOP_RIGHT);
        borderPane.setMargin(btn, new Insets(90, 120, 0, 0));
        btn.setTextFill(Color.BLACK);
        btn.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 20));
        borderPane.setLeft(t.drawTable());

        StackPane sp = new StackPane();

        Image img = new Image("29834347-millimeter-paper.jpg");

        ImageView imgView = new ImageView(img);

        sp.getChildren().addAll(imgView, borderPane);

        Scene scene = new Scene(sp, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Rate Monotonic scheduling");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Rectangle fPir(int sector, int i) {
        Rectangle r = new Rectangle();
        r.setX(20 + sector * i);
        r.setY(170);
        r.setWidth(sector);
        r.setHeight(30);
        r.setFill(Color.YELLOW);
        return r;
    }

    public Rectangle sPir(int sector, int i) {
        Rectangle r = new Rectangle();
        r.setX(20 + sector * i);
        r.setY(270);
        r.setWidth(sector);
        r.setHeight(30);
        r.setFill(Color.RED);
        return r;
    }

    public Rectangle thPir(int sector, int i) {
        Rectangle r = new Rectangle();
        r.setX(20 + sector * i);
        r.setY(370);
        r.setWidth(sector);
        r.setHeight(30);
        r.setFill(Color.YELLOWGREEN);
        return r;
    }

    // RMS technique
    public int calRms(int i, int j, int k) {
        int rms = 0;
        if (i % j == 0 && i % k == 0) {
            rms = i;
        } else if (j % i == 0 && j % k == 0) {
            rms = j;
        } else if (k % j == 0 && k % i == 0) {
            rms = k;
        } else if ((i * j) % k == 0) {
            rms = i * j;
        } else if ((j * k) % i == 0) {
            rms = j * k;
        } else if ((i * k) % j == 0) {
            rms = i * k;
        } else {
            rms = j * k * i;
        }
        return rms;
    }

    public Process[] calPir(int p4, int p5, int p6, Process proc1, Process proc2, Process proc3) {
        Process[] pir = new Process[3];
        if (p4 < p5 && p5 < p6) {
            pir[0] = proc1;
            pir[1] = proc2;
            pir[2] = proc3;
        } else if (p4 < p6 && p6 < p5) {
            pir[0] = proc1;
            pir[1] = proc3;
            pir[2] = proc2;
        } else if (p5 < p4 && p4 < p6) {
            pir[0] = proc2;
            pir[1] = proc1;
            pir[2] = proc3;
        } else if (p5 < p6 && p6 < p4) {
            pir[0] = proc2;
            pir[1] = proc3;
            pir[2] = proc1;
        } else if (p6 < p4 && p4 < p5) {
            pir[0] = proc3;
            pir[1] = proc1;
            pir[2] = proc2;
        } else if (p6 < p5 && p5 < p4) {
            pir[0] = proc3;
            pir[1] = proc2;
            pir[2] = proc1;
        }
        return pir;
    }
}
