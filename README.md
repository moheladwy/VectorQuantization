# Vector Quantization Image Compression

## Overview
This project implements a Vector Quantization Image Compression Algorithm with a Java Swing GUI. The algorithm aims to compress images using vector quantization, and the graphical user interface provides a convenient way to interact with the compression and decompression processes.

## Project Structure

### Models Package
The `Models` package contains the models of the Vector Quantization algorithm.

### Logic Package
The `Logic` package contains the core implementation of the Vector Quantization algorithm.

### GUI Package
The `GUI` package contains the graphical user interface components.

## How to Use
1. Run the program using the following command in the CLI, and a GUI window will appear:
    ```bash
      git clone https://github.com/moheladwy/VectorQuantization.git VectorQuantization
      cd VectorQuantization
      javac Program.java 
      java Program.class
    ```
2. Click the "Browse" button to choose an image for compression.
3. Press the "Compress" button to initiate the compression process.
4. The compressed image will be displayed along with relevant information.
5. Click the "Clear Image" button to reset and choose a new image.

## Dependencies
- Java Swing
- BufferedImage
- File I/O
- Java Collections Framework

## Note
- Ensure that the chosen image has one of the following extensions: `jpg`, `jpeg`, `png`, `bmp`, `gif`.
- The compressed image will be saved in the same directory as the original with the filename appended "_compressed".

## Examples

#### First Example:
![Original Image](/src/Images/always.jpeg) ![Compressed Image](/src/DecompressedImages/always_decompressed.jpeg)

#### Second Example:
![Original Image](/src/Images/Dean%20Winchester.jpg) ![Compressed Image](/src/DecompressedImages/Dean%20Winchester_decompressed.jpg)

#### Third Example:
![Original Image](/src/Images/400x400GrayImage.jpg) ![Compressed Image](/src/DecompressedImages/400x400GrayImage_decompressed.jpg)

## Contributors
- [Mohamed Al-Adawy](https://github.com/moheladwy)
- [Ali Al-Deen](https://github.com/Alialdin99)
  
Feel free to explore and modify the code to suit your needs. If you have any questions or suggestions, please contact me on my email: [mohamed.h.eladwy@gmail.com](mailto:mohamed.h.eladwy@gmail.com)
