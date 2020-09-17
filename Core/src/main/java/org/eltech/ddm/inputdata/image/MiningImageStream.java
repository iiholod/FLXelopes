package org.eltech.ddm.inputdata.image;

import org.eltech.ddm.inputdata.MiningVector;
import org.eltech.ddm.inputdata.file.MiningFileStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningdata.*;
import org.omg.java.cwm.analysis.datamining.miningcore.miningdata.AttributeType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MiningImageStream extends MiningFileStream {

    /** Image files. */
    private File[] imageFiles;

    // -----------------------------------------------------------------------
    //  Constructor
    // -----------------------------------------------------------------------

    /**
     * Accepts a directory path.
     * @param dir - path to directory
     */
    public MiningImageStream(String dir) {
        super(dir);
        open = false;
    }

    // -----------------------------------------------------------------------
    // ?????????????????????????????????????
    // -----------------------------------------------------------------------

    /**
     * Gets an array of files from a directory and ?????????????????????????????????????.
     */
    @Override
    public void open() throws MiningException {

        if(open) return;

        File dir = new File(path);
        imageFiles = dir.listFiles(File::isFile);

        recognize();
        open = true;
    }

    @Override
    public void close() {

        if(!open) return;
        imageFiles = null;
        open = false;
    }

    /**
     * ?????????????????????????????????????
     * @return physical data
     */
    @Override
    public EPhysicalData recognize() throws MiningException {

        if (logicalData == null && physicalData == null) {
            initData();
        }
        return physicalData;
    }

    /**
     * Initialization of meta data.
     */
    private void initData() throws MiningException {

        logicalData = new ELogicalData();
        physicalData = new EPhysicalData();
        attributeAssignmentSet = new EAttributeAssignmentSet();

        for (int i = 1; i <= imageFiles.length; i++) {

            ELogicalAttribute la = new ELogicalAttribute("Image " + i, AttributeType.image);
            PhysicalAttribute pa = new PhysicalAttribute("Image " + i, AttributeType.image, AttributeDataType.integerType);
            EDirectAttributeAssignment da = new EDirectAttributeAssignment();
            logicalData.addAttribute(la);
            physicalData.addAttribute(pa);
            da.addLogicalAttribute(la);
            da.setAttribute(pa);
            attributeAssignmentSet.addAssignment(da);
        }
    }

    /**
     * Get next vector of pixels.
     * @return vector of pixels
     */
    @Override
    public MiningVector readPhysicalRecord() {

        cursorPosition++;
        BufferedImage img = getImage(cursorPosition);
        double[] pixelsVec = getArrayOfPixels(img);

        MiningVector vector = new MiningVector(pixelsVec);
        vector.setLogicalData(logicalData);
        vector.setIndex(cursorPosition);
        return vector;
    }

    /**
     * Get vector of pixels by index.
     * @param position - new cursor position
     * @return vector of pixels
     */
    @Override
    protected MiningVector movePhysicalRecord(int position) {

        BufferedImage img = getImage(position);
        double[] pixelsVec = getArrayOfPixels(img);

        MiningVector vector = new MiningVector(pixelsVec);
        vector.setLogicalData(logicalData);
        vector.setIndex(position);
        cursorPosition = position;
        return vector;
    }

    /**
     * ?????????????????????????????????????
     * @param position - number of image
     * @return image
     */
    private BufferedImage getImage(int position) {

        try {
            return ImageIO.read(imageFiles[position]);
        } catch (IOException ex) {
            throw new NullPointerException(ex.getMessage());
        }
    }

    /**
     * ?????????????????????????????????????
     * @param img - image
     * @return array of pixels
     */
    private double[] getArrayOfPixels(BufferedImage img) {

        int width = img.getWidth();
        int height = img.getHeight();
        double[] pixelsVec = new double[width * height];

        int vecIndex = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixelsVec[vecIndex++] = img.getRGB(x, y);
            }
        }
        return pixelsVec;
    }

    public int getImageNumber() {
        return imageFiles.length;
    }
}