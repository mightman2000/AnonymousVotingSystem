import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyFrame extends JFrame {

    Connection conn = null;
    PreparedStatement state = null;
    ResultSet result = null;
    int id;

    Encryption encryption = new Encryption();

    JTabbedPane tab = new JTabbedPane();

    //FONT

    Font font = new Font("Arial", Font.PLAIN, 32); // Create a font with size 24

    //welcome panel
    Panel welcomePanel =  new Panel();
    Panel welcomeUpPanel =  new Panel();
    Panel welcomeDownPanel =  new Panel();
    JLabel welcomeLabel = new JLabel("Voting 2023 ");
    JTextField inputEgnTextField = new JTextField("Please input your EGN to generate voting key");

    JButton generateKeyBtn = new JButton("Generate key");

    //vote panel
    Panel votePanel = new Panel();
    Panel voteRightPanel = new Panel();
    Panel voteLeftPanel = new Panel();

    JTextField inputKey = new JTextField("Please input your voting key");
    JTextField secretKeyTextField = new JTextField("Please create your secret key");
    JButton voteBtn = new JButton("Vote");
    String[] candidates ={"1.Faraona", "2.Dickata" , "3.Dankata", "4.Tsareveca"};
    JComboBox<String> comboBox = new JComboBox<String>(candidates);

    //reference panel
    Panel referencePanel = new Panel();
    Panel referenceUpPanel = new Panel();
    Panel referenceDownPanel = new Panel();

    JTextField referenceTextField = new JTextField("Please insert your encrypted key");

    JButton referenceBtn = new JButton("reference");

    // method to check if egn is used
    boolean isEgnUsed(String EGN, PreparedStatement state) throws SQLException {
        String sql = "SELECT COUNT(*) FROM keyCreation WHERE egn = ?";
        state = conn.prepareStatement(sql);
        state.setString(1, EGN);
        result = state.executeQuery();
        result.next();
        int count = result.getInt(1);
        return count > 0;
    }

    boolean isVotingKeyValid(String votingKey, PreparedStatement state) throws SQLException {
            String sql ="SELECT COUNT(*) FROM keyCreation WHERE votingkey = ?";
            state = conn.prepareStatement(sql);
            state.setString(1, votingKey);
            result = state.executeQuery();
            result.next();
            int count = result.getInt(1);
            return count > 0;
    }

    boolean isEncryptedKeyValid(String encryptedKey, PreparedStatement state) throws SQLException {
            String sql ="SELECT COUNT(*) FROM reference WHERE encryptedkey = ?";
            state = conn.prepareStatement(sql);
            state.setString(1, encryptedKey);
            result = state.executeQuery();
            result.next();
            int count = result.getInt(1);
            return count > 0;
    }

    boolean isVoted(String getKey, PreparedStatement state) throws SQLException {
        String sql = "SELECT is_used from keycreation where votingkey = ?";
        state = conn.prepareStatement(sql);
        state.setString(1, getKey);
        result = state.executeQuery();
        result.next();
        boolean isTrueOrFalse = result.getBoolean(1);
        return isTrueOrFalse;
    }


    public MyFrame(){

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(400,400);

        tab.add(welcomePanel, "Welcome");
        tab.add(votePanel, "Vote");
        tab.add(referencePanel, "Reference");

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
        voteLeftPanel.setLayout(new GridLayout(3,1));
        voteLeftPanel.add(inputKey);
        voteLeftPanel.add(secretKeyTextField);
        voteLeftPanel.add(voteBtn);
        voteBtn.addActionListener(new vote());

        //right panel
        votePanel.add(voteRightPanel);
        voteRightPanel.add(comboBox);

        //reference panel

        //up panel
        referencePanel.add(referenceUpPanel);
        referenceUpPanel.add(referenceTextField);
        //referenceUpPanel.add(secretKeyTextField);

        //down panel
        referencePanel.add(referenceDownPanel);
        referencePanel.add(referenceBtn);
        referenceBtn.addActionListener(new reference());

        this.setResizable(false);
        this.setVisible(true);
    }
    class generateKey implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            //store egn
            String EGN = inputEgnTextField.getText();
            //create key
            Generator g = new Generator();

            conn = DBConnection.getConnection();

            try {
                if(isEgnUsed(EGN,state)){
                    JOptionPane.showMessageDialog(null, "Egn is already used", "Error", JOptionPane.INFORMATION_MESSAGE);

                }
                else {
                    String sql = "insert into keyCreation(egn,votingkey) values(?,?)";
                    try {
                        state = conn.prepareStatement(sql);
                        state.setString(1, EGN);
                        state.setString(2, g.RandomKeyGenerator());

                        state.execute();

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    class vote implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {


            String secretKey;

            conn = DBConnection.getConnection();
            String candidateChoice = comboBox.getSelectedItem().toString();

            String votingKey = inputKey.getText();


            try {
                if (isVoted(votingKey, state)) {

                    JOptionPane.showMessageDialog(null, "You already voted", "Error", JOptionPane.INFORMATION_MESSAGE);

                } else {

                    try {
                        if (isVotingKeyValid(votingKey, state)) {

                            secretKey = secretKeyTextField.getText(); // todo proverka dali e 16 simvola
                            String getKey = inputKey.getText();
                            String encryptedKey = encryption.encrypt(getKey, secretKey);

                            if (isEncryptedKeyValid(encryptedKey, state)) {
                                JOptionPane.showMessageDialog(null, "You already voted", "Error", JOptionPane.INFORMATION_MESSAGE);
                            }

                            String sql = "insert into reference(encryptedKey,candidateChoice) values(?,?)";
                            try {
                                state = conn.prepareStatement(sql);
                                state.setString(1, encryptedKey);
                                state.setString(2, candidateChoice);

                                state.execute();

                                String sql2 = "update keycreation set is_used = ? where votingkey = ?";
                                state = conn.prepareStatement(sql2);
                                state.setBoolean(1, true);
                                state.setString(2, getKey);

                                state.execute();

                                JOptionPane.showMessageDialog(null, "Your encrypted key is: " + encryptedKey, "Your encrypted key", JOptionPane.INFORMATION_MESSAGE);

                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid voting key", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    class reference implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            conn = DBConnection.getConnection();

           String referenceVote = referenceTextField.getText();

            String sql = "Select candidatechoice from reference where encryptedkey = ? ";

            try {
                state = conn.prepareStatement(sql);
                state.setString(1, referenceVote);

                result = state.executeQuery();

                if (result.next()) {
                    String candidateChoice = result.getString("candidatechoice");
                    JOptionPane.showMessageDialog(null, "You voted for: " + candidateChoice, "Reference", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No vote found for the provided reference.", "Reference", JOptionPane.WARNING_MESSAGE);
                }

            }catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}

