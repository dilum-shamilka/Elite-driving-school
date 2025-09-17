module lk.ijse {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires java.desktop;
    requires java.logging;
    requires java.xml;
    requires jdk.accessibility;
    requires java.management;
    requires jdk.jlink;

    // Hibernate & Jakarta
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires jbcrypt;   // ✅ Fix for javax.naming.Referenceable

    // Open packages
    opens lk.ijse.controller to javafx.fxml;
    opens lk.ijse.dto.tm to javafx.base;
    opens lk.ijse.entity to org.hibernate.orm.core; // ✅ Needed for entity scanning

    exports lk.ijse;
}
