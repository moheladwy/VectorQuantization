# Vector Quantization Image Compression

## Overview
This project implements a Vector Quantization Image Compression Algorithm with a Java Swing GUI. The algorithm aims to compress images using vector quantization, and the graphical user interface provides a convenient way to interact with the compression and decompression processes.

## Project Structure

### Models Package
The `Models` package contains the models of the Vector Quatiaztion Algorithm.

### Logic Package
The `Logic` package contains the core implementation of the Vector Quantization algorithm.

### GUI Package
The `GUI` package contains the graphical user interface components.

## How to Use
1. Run the program using the following command in the CLI, and a GUI window will appear:

    javac Program.java #make sure to navigate in the project directory to run the command correct.
    java Program.class
   
3. Click the "Browse" button to choose an image for compression.
4. Press the "Compress" button to initiate the compression process.
5. The compressed image will be displayed along with relevant information.
6. Click the "Clear Image" button to reset and choose a new image.

## Dependencies
- Java Swing
- BufferedImage
- File I/O
- Java Collections Framework

## Note
- Ensure that the chosen image has one of the following extensions: `jpg`, `jpeg`, `png`, `bmp`, `gif`.
- The compressed image will be saved in the same directory as the original with the filename appended "_compressed".

## Contributors
- [moheladwy].
- [Alialdin99].
  
Feel free to explore and modify the code to suit your needs. If you have any questions or suggestions, please contact me on my email: [mohamed.h.eladwy@gmail.com].
