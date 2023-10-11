import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static Connection conn = null;

    static Connection getConnection(){
        try {
            Class.forName("org.h2.Driver");
            conn= DriverManager.getConnection("jdbc:h2:tcp://localhost/C:\\Users\\George\\Desktop\\Spring boot\\BlockChainVoting","sa","1234");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return conn;
    }


    class MouseAction implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}