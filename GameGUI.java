import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import java.awt.geom.*;

public class GameGUI extends JFrame {
    private JPanel gamePanel;
    private JLabel scoreLabel;
    private JButton startButton;
    private JButton acceptButton;
    private JButton rejectButton;
    private JLabel currentShapeLabel;
    private JTextArea stackArea;
    private JScrollPane stackScrollPane;
    
    private ShapeGenerator generator;
    private Player player;
    private Shape currentShape;
    private boolean gameStarted = false;
    private int numberPerShape = 5;
    private int playerStackSize = 10;
    
    // Animation variables
    private Timer animationTimer;
    private float shapeScale = 0.0f;
    private float shapeRotation = 0.0f;
    private Point2D shapePosition;
    private Color backgroundColor = new Color(245, 245, 250);
    private Color accentColor = new Color(70, 130, 180);
    private Color buttonColor = new Color(100, 149, 237);
    private Color buttonHoverColor = new Color(65, 105, 225);

    public GameGUI() {
        // Set up the main window
        setTitle("Shapes Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(backgroundColor);

        // Create the main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create the game panel with custom painting
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Draw background with gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 240, 245),
                    getWidth(), getHeight(), new Color(230, 230, 235)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                
                if (gameStarted && currentShape != null && shapePosition != null) {
                    
                    AffineTransform oldTransform = g2d.getTransform();
                    
                    
                    g2d.translate(shapePosition.getX(), shapePosition.getY());
                    g2d.rotate(shapeRotation);
                    g2d.scale(shapeScale, shapeScale);
                    
                    
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.translate(5, 5);
                    drawShape(g2d, currentShape, 0, 0);
                    g2d.translate(-5, -5);
                    
                    
                    drawShape(g2d, currentShape, 0, 0);
                    
                   
                    g2d.setTransform(oldTransform);
                }
            }
        };
        gamePanel.setPreferredSize(new Dimension(800, 500));
        gamePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        
        JPanel rightPanel = new JPanel(new BorderLayout(15, 15));
        rightPanel.setBackground(backgroundColor);

        stackArea = new JTextArea(15, 25);
        stackArea.setEditable(false);
        stackArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stackArea.setBackground(new Color(250, 250, 255));
        stackArea.setForeground(new Color(50, 50, 50));
        stackArea.setMargin(new Insets(10, 10, 10, 10));
        
        stackScrollPane = new JScrollPane(stackArea);
        stackScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(accentColor, 1, true),
                "Shape Stack",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                accentColor
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        stackScrollPane.setBackground(backgroundColor);

       
        currentShapeLabel = new JLabel("No shape", SwingConstants.CENTER);
        currentShapeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        currentShapeLabel.setForeground(accentColor);
        currentShapeLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        
        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        controlPanel.setBackground(backgroundColor);

        
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        scoreLabel.setForeground(accentColor);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(backgroundColor);
        
        startButton = createStyledButton("Start Game");
        acceptButton = createStyledButton("Accept Shape");
        rejectButton = createStyledButton("Reject Shape");
        
        
        acceptButton.setEnabled(false);
        rejectButton.setEnabled(false);

        
        startButton.addActionListener(e -> startGame());
        acceptButton.addActionListener(e -> acceptShape());
        rejectButton.addActionListener(e -> rejectShape());

        buttonPanel.add(startButton);
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

       
        controlPanel.add(currentShapeLabel);
        controlPanel.add(scoreLabel);
        controlPanel.add(buttonPanel);

        
        rightPanel.add(stackScrollPane, BorderLayout.CENTER);
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

       
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        
        add(mainPanel);

        
        animationTimer = new Timer(16, e -> {
            if (gameStarted && currentShape != null) {
               
                shapeScale = Math.min(1.0f, shapeScale + 0.05f);
                shapeRotation += 0.02f;
                gamePanel.repaint();
            }
        });
        animationTimer.setRepeats(true);

        
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(buttonHoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(buttonHoverColor);
                } else {
                    g2d.setColor(buttonColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.setColor(Color.WHITE);
                FontMetrics metrics = g2d.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(getText())) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(120, 40));
        
        return button;
    }

    private void drawShape(Graphics2D g2d, Shape shape, int x, int y) {
        g2d.setColor(shape.getColor());
        g2d.setStroke(new BasicStroke(3));
        
        int size = 120; 
        
        switch (shape.getClass().getSimpleName()) {
            case "Circle":
                g2d.drawOval(x - size/2, y - size/2, size, size);
                break;
            case "Square":
                g2d.drawRoundRect(x - size/2, y - size/2, size, size, 10, 10);
                break;
            case "Triangle":
                int[] xPoints = {x, x - size/2, x + size/2};
                int[] yPoints = {y - size/2, y + size/2, y + size/2};
                g2d.drawPolygon(xPoints, yPoints, 3);
                break;
            case "Pentagon":
                int sides = 5;
                int[] xPent = new int[sides];
                int[] yPent = new int[sides];
                for (int i = 0; i < sides; i++) {
                    double angle = 2 * Math.PI * i / sides - Math.PI / 2;
                    xPent[i] = x + (int)(size/2 * Math.cos(angle));
                    yPent[i] = y + (int)(size/2 * Math.sin(angle));
                }
                g2d.drawPolygon(xPent, yPent, sides);
                break;
            case "BonusCircle":
                g2d.setColor(new Color(255, 215, 0)); 
                g2d.drawOval(x - size/2, y - size/2, size, size);
                g2d.drawOval(x - size/4, y - size/4, size/2, size/2);
                
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.drawLine(x - size/3, y - size/3, x - size/4, y - size/4);
                g2d.drawLine(x + size/3, y - size/3, x + size/4, y - size/4);
                break;
        }
    }

    private void startGame() {
        
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", accentColor);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        
        String numberPerShapeStr = JOptionPane.showInputDialog(this, 
            "Enter number of shapes per type:", "5");
        String stackSizeStr = JOptionPane.showInputDialog(this, 
            "Enter player stack size:", "10");

        try {
            numberPerShape = Integer.parseInt(numberPerShapeStr);
            playerStackSize = Integer.parseInt(stackSizeStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid input. Using default values.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        generator = new ShapeGenerator(numberPerShape);
        player = new Player(playerStackSize);
        gameStarted = true;
        
        shapeScale = 0.0f;
        shapeRotation = 0.0f;
        shapePosition = new Point2D.Double(gamePanel.getWidth() / 2, gamePanel.getHeight() / 2);
        
        animationTimer.start();
        
        startButton.setText("Restart Game");
        acceptButton.setEnabled(true);
        rejectButton.setEnabled(true);
        
        nextShape();
        updateStack();
    }

    private void nextShape() {
        if (generator.hasShapes() && !player.isStackFull()) {
            currentShape = generator.nextShape();
            currentShapeLabel.setText("Current Shape: " + currentShape.toString());
            
            shapeScale = 0.0f;
            shapeRotation = 0.0f;
            
            gamePanel.repaint();
        } else {
            endGame();
        }
    }

    private void acceptShape() {
        if (currentShape != null) {
            if (player.acceptShape(currentShape)) {
                shapeScale = 1.2f;
                gamePanel.repaint();
                
                updateScore();
                updateStack();
                nextShape();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Stack is full!",
                    "Cannot Accept Shape",
                    JOptionPane.WARNING_MESSAGE);
                endGame();
            }
        }
    }

    private void rejectShape() {
        if (currentShape != null) {
            shapeScale = 0.8f;
            gamePanel.repaint();
            
            nextShape();
        }
    }

    private void updateScore() {
        scoreLabel.setText(String.format("Score: %.1f", player.getPoints()));
    }

    private void updateStack() {
        stackArea.setText("");
        Stack<Shape> shapes = player.getPlayerShapes();
        for (int i = shapes.size() - 1; i >= 0; i--) {
            stackArea.append(String.format("%d: %s\n", 
                shapes.size() - i, shapes.get(i).toString()));
        }
    }

    private void endGame() {
        gameStarted = false;
        currentShape = null;
        acceptButton.setEnabled(false);
        rejectButton.setEnabled(false);
        currentShapeLabel.setText("Game Over!");
        
        animationTimer.stop();
        
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", accentColor);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        
        JOptionPane.showMessageDialog(this,
            String.format("Game Over!\nFinal Score: %.1f", player.getPoints()),
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new GameGUI();
        });
    }
} 