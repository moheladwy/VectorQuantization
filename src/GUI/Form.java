package GUI;

import Logic.ImageHelper;
import Logic.VectorQuantization;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Form {
    private JFrame frame;
    private JLabel imageLabel;
    private JLabel imageLabelType;
    JButton browseButton;
    JButton compressButton;
    JButton clearImageButton;
    private File selectedFile;
    private File decompressedFile;
    private BufferedImage decompressedImage;
    private final Dimension MIN_FRAME_DIMENSION;
    private final Dimension IMAGE_DIMENSION;
    private final int FONT_SIZE;

    public Form() {
        MIN_FRAME_DIMENSION = new Dimension(800, 240);
        IMAGE_DIMENSION = new Dimension(1200, 800);
        FONT_SIZE = 20;
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

        compressButton = initializeCompressionButton();

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
            if (selectedFile == null)
                JOptionPane.showMessageDialog(frame, "You have to choose an Image first!", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                decompressedImage = VectorQuantization.compress(selectedFile);
                if (decompressedImage == null)
                    JOptionPane.showMessageDialog(frame, "Error occurred while compressing the image!", "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    JOptionPane.showMessageDialog(frame, "Image compressed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    imageLabelType.setText("Compressed Image: ");
                    decompressedFile = new File(ImageHelper.getDecompressedImageName(selectedFile));
                    ImageHelper.saveImage(decompressedImage, decompressedFile);
                    displayImage(decompressedFile);
                    if (imageLabel.getIcon() != null) {
                        compressButton.setVisible(false);
                    }
                }
            }
        });
        compressButton.setVisible(false);
        return compressButton;
    }

    private JButton initializeBrowseButton() {
        browseButton = new JButton("Browse");
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
                if (isImage(selectedFile)) {
                    displayImage(selectedFile);
                    browseButton.setText(selectedFile.getName());
                    clearImageButton.setVisible(true);
                    imageLabelType.setText("Original Image: ");
                    compressButton.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "You have to choose an Image with (*.jpg, *.jpeg, *.png, *.bmp, *.gif) extensions only!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return browseButton;
    }

    private JPanel initializeChooseImagePanel() {
        JPanel chooseImagePanel = new JPanel();
        chooseImagePanel.setLayout(new BoxLayout(chooseImagePanel, BoxLayout.Y_AXIS));

        JPanel browsePanel = initializeBrowsePanel();
        JPanel clearImagePanel = initializeClearImagePanel();
        JPanel imagePanel = initializeImagePanel();

        browsePanel.add(clearImagePanel);
        chooseImagePanel.add(browsePanel);
        chooseImagePanel.add(imagePanel);

        return chooseImagePanel;
    }

    private JPanel initializeClearImagePanel() {
        JPanel clearImagePanel = new JPanel();

        JButton clearImageButton = initializeClearImageButton();
        clearImagePanel.add(clearImageButton);

        return clearImagePanel;
    }

    private JButton initializeClearImageButton() {
        clearImageButton = new JButton("Clear Image");
        clearImageButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));
        clearImageButton.setMargin(new Insets(10, 10, 10, 10));
        clearImageButton.setPreferredSize(new Dimension(150, 50));
        clearImageButton.addActionListener(e -> {
            imageLabel.setIcon(null);
            browseButton.setText("Browse Image");
            imageLabelType.setText("No Image Selected");
            compressButton.setVisible(false);
            clearImageButton.setVisible(false);
            frame.pack();
        });
        clearImageButton.setVisible(false);
        return clearImageButton;
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

        imageLabelType = new JLabel();
        imageLabelType.setText("No Image Selected");
        imageLabelType.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setMinimumSize(new Dimension(1, 1));
        imageLabel.setMaximumSize(IMAGE_DIMENSION);

        imagePanel.add(imageLabelType);
        imagePanel.add(imageLabel);
        return imagePanel;
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
            // "Error occurred while displaying the image!"
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public File getImage() {
        return selectedFile;
    }
}
