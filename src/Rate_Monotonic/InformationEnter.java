package Rate_Monotonic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author Amir Zamoura
 */
class InformationEnter extends GridPane {

    Integer[] a = new Integer[6];
    TextField tf1 = new TextField();
    TextField tf2 = new TextField();
    TextField tf3 = new TextField();
    TextField tf4 = new TextField();
    TextField tf5 = new TextField();
    TextField tf6 = new TextField();

    public GridPane drawTable() {
        this.setAlignment(Pos.TOP_LEFT);

        this.setPadding(new Insets(20, 0, 0, 120));
        this.add(new Text("Task time"), 5, 3);
        this.add(new Text("Period"), 10, 3);
        this.add(new Text("Process 1"), 0, 6);

        this.add(new Text("Process 2"), 0, 9);

        this.add(new Text("Process 3"), 0, 12);

        this.add(tf1, 5, 6);
        this.add(tf2, 5, 9);
        this.add(tf3, 5, 12);
        this.add(tf4, 10, 6);
        this.add(tf5, 10, 9);
        this.add(tf6, 10, 12);
        return this;
    }

    public Integer[] returnValues() {

        a[0] = Integer.parseInt(tf1.getText());
        a[1] = Integer.parseInt(tf2.getText());
        a[2] = Integer.parseInt(tf3.getText());
        a[3] = Integer.parseInt(tf4.getText());
        a[4] = Integer.parseInt(tf5.getText());
        a[5] = Integer.parseInt(tf6.getText());

        return a;
    }
}
