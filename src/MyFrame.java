import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class MyFrame extends JFrame {

    Connection conn = null;
    PreparedStatement state = null;
    ResultSet result = null;
    int id;



    JTabbedPane tab = new JTabbedPane();

    //FONT

    Font font = new Font("Arial", Font.PLAIN, 32); // Create a font with size 24


    //welcome panel
    Panel welcomePanel =  new Panel();
    Panel welcomeUpPanel =  new Panel();
    Panel welcomeDownPanel =  new Panel();
    JLabel welcomeLabel = new JLabel("Voting 2023 ");
    JTextField inputEgnTextField = new JTextField("Please input your EGN to generate voting key", 30);



    JButton generateKeyBtn = new JButton("Generate key");

    //vote panel
    Panel votePanel = new Panel();
    Panel voteRightPanel = new Panel();
    Panel voteLeftPanel = new Panel();

    JTextField inputKey = new JTextField("Please input your key");
    JButton voteBtn = new JButton("Vote");
    String[] candidates ={"1.Faraona", "2.Dickata" , "3.Dankata", "4.Tsareveca"};
    JComboBox<String> comboBox = new JComboBox<String>(candidates);

    public MyFrame(){

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(400,400);

        tab.add(welcomePanel, "Welcome");
        tab.add(votePanel, "Vote");

        this.add(tab);

        //welcome panel

        welcomePanel.setLayout(new GridLayout(2,1));
        generateKeyBtn.addActionListener(new generateKey());

        //upPanel
        welcomePanel.add(welcomeUpPanel);
        welcomeUpPanel.setLayout(new GridLayout(1,1));

        welcomeUpPanel.add(welcomeLabel);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(font);

        //downPanel

        welcomePanel.add(welcomeDownPanel);

        welcomeDownPanel.add(inputEgnTextField);

        welcomeDownPanel.add(generateKeyBtn);

        //vote panel

        // left panel
        votePanel.add(voteLeftPanel);
        voteLeftPanel.setLayout(new GridLayout(2,1));
        voteLeftPanel.add(inputKey);
        voteLeftPanel.add(voteBtn);

        //right panel
        votePanel.add(voteRightPanel);
        voteRightPanel.add(comboBox);

        this.setResizable(false);
        this.setVisible(true);
    }



    class generateKey implements ActionListener{


        @Override
        public void actionPerformed(ActionEvent e) {
            Generator g = new Generator();
            g.RandomKeyGenerator();

            //store egn
            String EGN = inputEgnTextField.getText();
            System.out.println(EGN);
        }
    }
}

