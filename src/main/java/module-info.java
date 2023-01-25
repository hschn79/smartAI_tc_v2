module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires ij;
	requires java.desktop;
    requires org.jfree.jfreechart;

    opens org.Controller to javafx.fxml;
    exports org.Controller;
}
