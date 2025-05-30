package ui;

import dao.MessageDAO;
import model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FakeMessageDetectorUI extends JFrame {

    private JTextArea messageInput;
    private JLabel resultLabel;

    public FakeMessageDetectorUI() {
        setTitle("Fake Message Detector");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 250, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Fake Message Detector", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        messageInput = new JTextArea(5, 40);
        messageInput.setFont(new Font("Monospaced", Font.PLAIN, 16));
        messageInput.setWrapStyleWord(true);
        messageInput.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageInput);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton detectBtn = new JButton("Detect Fake Message");
        detectBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        detectBtn.setBackground(new Color(0, 123, 255));
        detectBtn.setForeground(Color.WHITE);
        detectBtn.setFocusPainted(false);
        detectBtn.addActionListener(this::onDetect);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(245, 250, 255));
        bottomPanel.add(detectBtn, BorderLayout.NORTH);

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        bottomPanel.add(resultLabel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void onDetect(ActionEvent e) {
        String input = messageInput.getText().trim();
        if (input.isEmpty()) {
            resultLabel.setText("Please enter a message.");
            resultLabel.setForeground(Color.RED);
            return;
        }

        MessageDAO dao = new MessageDAO();
        boolean isFake = dao.isFakeMessage(input);
        Message message = new Message(input, isFake);

        if (message.isFake()) {
            resultLabel.setText("⚠️ Fake Message Detected!");
            resultLabel.setForeground(Color.RED);
        } else {
            resultLabel.setText("✅ Message Seems Genuine.");
            resultLabel.setForeground(new Color(0, 153, 51));
        }
    }
}
