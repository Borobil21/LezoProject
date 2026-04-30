module eus.ehu {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;

    opens eus.ehu.ui to javafx.fxml;
    opens eus.ehu.domain to org.hibernate.orm.core;
    exports eus.ehu.data_access;
    exports eus.ehu.businesslogic;
    exports eus.ehu.ui;
}