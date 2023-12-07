package GUI;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import Logic.VectorQuantization;

public class Form {
    private JFrame frame;
    private JLabel imageLabel;
    private File selectedFile;
    private File compressedImage;
    private final Dimension MIN_FRAME_DIMENSION;
    private final Dimension IMAGE_DIMENSION;
    private final int FONT_SIZE;

    public Form() {
        MIN_FRAME_DIMENSION = new Dimension(800, 240);
        IMAGE_DIMENSION = new Dimension(1200, 800);
        FONT_SIZE = 16;
        InitializeFrame();
    }

    private void InitializeFrame() {
        frame = new JFrame("Vector Quantization Compression Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(MIN_FRAME_DIMENSION);
        frame.setMinimumSize(MIN_FRAME_DIMENSION);

        frame.setLayout(new BorderLayout());

        JPanel welcomeMessagePanel = initializeWelcomeMessagePanel();
        JPanel chooseImagePanel = initializeChooseImagePanel();
        JPanel compressionButtonPanel = initializeCompressionButtonPanel();

        frame.add(welcomeMessagePanel, BorderLayout.NORTH);
        frame.add(chooseImagePanel, BorderLayout.CENTER);
        frame.add(compressionButtonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel initializeWelcomeMessagePanel(){
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(30, 30, 30));
        frame.add(panel, BorderLayout.NORTH);

        JLabel message = new JLabel("Welcome to The Vector Quantization Compression Program!");
        panel.add(message);
        frame.setLayout(new BorderLayout());
        message.setForeground(Color.WHITE);

        message.setFont(new Font("Sans-serif", Font.BOLD, 24));

        return panel;
    }

    private JPanel initializeCompressionButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton compressButton = initializeCompressionButton();

        // Add a rigid area to increase the distance between buttons to 30 pixels
        panel.add(Box.createRigidArea(new Dimension(30, 0)));
        panel.add(compressButton);

        return panel;
    }

    private JButton initializeCompressionButton() {
        JButton compressButton = new JButton("Compress");

        compressButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
        compressButton.setMargin(new Insets(10, 10, 10, 10));
        compressButton.setPreferredSize(new Dimension(150, 50));

        compressButton.addActionListener(e -> {
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(frame, "You have to choose an Image first!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                compressedImage = VectorQuantization.compress(selectedFile);
                if (compressedImage == null) {
                    JOptionPane.showMessageDialog(frame, "Error occurred while compressing the image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Image compressed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    displayImage(compressedImage);
                }
            }
        });
        return compressButton;
    }

    private JButton initializeBrowseButton() {
        JButton browseButton = new JButton("Browse");
        browseButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
        browseButton.setMargin(new Insets(10, 20, 10, 20));
        browseButton.setMinimumSize(new Dimension(100, 50));
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File Image) {
                    return Image.isDirectory() || isImage(Image);
                }
                public String getDescription() {
                    return "Images with (*.jpg, *.jpeg, *.png, *.bmp, *.gif) extension";
                }
            });
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                browseButton.setText(selectedFile.getName());
                displayImage(selectedFile);
            }
        });
        return browseButton;
    }

    private JPanel initializeChooseImagePanel() {
        JPanel chooseImagePanel = new JPanel();
        chooseImagePanel.setLayout(new BoxLayout(chooseImagePanel, BoxLayout.Y_AXIS));

        JPanel browsePanel = initializeBrowsePanel();
        JPanel imagePanel = initializeImagePanel();

        chooseImagePanel.add(browsePanel);
        chooseImagePanel.add(imagePanel);

        return chooseImagePanel;
    }

    private JPanel initializeBrowsePanel() {
        JPanel browsePanel = new JPanel();
        browsePanel.setMinimumSize(MIN_FRAME_DIMENSION);

        JLabel messageLabel = new JLabel("Choose Image: ");
        JButton browseButton = initializeBrowseButton();
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

        browsePanel.add(messageLabel);
        browsePanel.add(browseButton);

        return browsePanel;
    }

    private JPanel initializeImagePanel() {
        JPanel imagePanel = new JPanel();
        imagePanel.setMaximumSize(new Dimension(1200, 800));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setMinimumSize(new Dimension(1, 1));
        imageLabel.setMaximumSize(IMAGE_DIMENSION);

        imagePanel.add(imageLabel);
        return imagePanel;
    }

    private static Image convertFileToImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    private static boolean isImage(File image) {
        String extension = image.getName()
                                .toLowerCase()
                                .substring(image.getName().lastIndexOf(".") + 1, image.getName().length());
        return (extension.equals("jpg") || extension.equals("jpeg")
                || extension.equals("png") || extension.equals("bmp")
                || extension.equals("gif"));
    }

    private void displayImage(File file) {
        try {
            Image image = ImageIO.read(file);
            imageLabel.setIcon(new ImageIcon(image));
            imageLabel.setPreferredSize(new Dimension(imageLabel.getIcon().getIconHeight(),
                    imageLabel.getIcon().getIconHeight()));
            frame.pack();
        } catch (IOException e) {
            Dialog dialog = new Dialog(frame, "Error", Boolean.parseBoolean(e.getMessage()));
            dialog.setVisible(true);
        }
    }

    public File getImage() {
        return selectedFile;
    }
}
