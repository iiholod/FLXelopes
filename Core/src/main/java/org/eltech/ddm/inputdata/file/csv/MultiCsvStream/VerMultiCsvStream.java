package org.eltech.ddm.inputdata.file.csv.MultiCsvStream;

import com.opencsv.exceptions.CsvException;
import org.eltech.ddm.inputdata.MiningVector;
import org.eltech.ddm.inputdata.file.csv.CsvParsingSettings;
import org.eltech.ddm.inputdata.file.csv.MiningCsvStream;
import org.eltech.ddm.inputdata.file.csv.ParsingValues;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningdata.ELogicalAttribute;
import org.eltech.ddm.miningcore.miningdata.ELogicalData;
import org.eltech.ddm.miningcore.miningdata.EPhysicalData;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

/**
 * VerMultiCsvStream class.
 * A class that allows you to read multiple csv-files 'vertically'.
 *
 * @author Maxim Kolpashikov
 */

public class VerMultiCsvStream extends MiningMultiCsvStream {

    private ArrayList parsingValues;

    // -----------------------------------------------------------------------
    //  Constructors
    // -----------------------------------------------------------------------

    /**
     * Accepts an array of csv file names without settings.
     * At this stage, an array of threads with standard settings is created.
     * @param files - array of csv-file names
     */
    public VerMultiCsvStream(String[] files) throws MiningException, IOException, CsvException {
        if (files == null) throw  new NullPointerException("The file array is empty.");
        init(getStreams(files));
    }

    /**
     * Accepts an array of csv file names with settings.
     * At this stage, an array of threads with custom settings is created.
     * @param files    - array of csv-file names
     * @param settings - settings for reading files
     */
    public VerMultiCsvStream(String[] files, CsvParsingSettings settings)
            throws MiningException, IOException, CsvException {
        if (files == null) throw  new NullPointerException("The file array is empty.");
        if(settings == null) settings = new CsvParsingSettings();
        init(getStreams(files, settings));
    }

    /**
     * Constructor that accepts an array of csv-file streams.
     * At this stage, the logical data of the csv-files that must match is checked.
     * @param streams - array of streams
     */
    public VerMultiCsvStream(MiningCsvStream[] streams) throws MiningException, IOException, CsvException {
        if (streams == null) throw  new NullPointerException("The stream array is empty.");
        init(streams);
    }

    /**
     * Initializes the class with input data, if the logical data is correct.
     * @param streams - array of streams
     */
    private void init(MiningCsvStream[] streams) throws MiningException, IOException, CsvException {
        if (vectorsNumberChecked(streams)) {
            super.streams = streams;
            super.logicalData = collectLogicalData();
            super.physicalData = collectPhysicalData();
            super.vectorsNumber = streams[0].getVectorsNumber();
            this.parsingValues = new ArrayList();
        } else {
            throw new InvalidObjectException("There are different number of vectors in the files.");
        }
    }

    /**
     * checks that the number of vectors in the files is the same
     * @return <b>true</b> if the number of vectors matches, <b>false</b> if the number of vectors does not match
     */
    private boolean vectorsNumberChecked(MiningCsvStream[] streams) throws MiningException, IOException, CsvException {
        int number = streams[0].getVectorsNumber();
        for (int i = 1; i < streams.length; i++) {
            if (streams[i].getVectorsNumber() != number) return false;
        }
        return true;
    }

    // -----------------------------------------------------------------------
    //  Methods that collect logical and physical data together
    // -----------------------------------------------------------------------

    /**
     * Collects logical data together.
     * @return ELogicalData
     */
    private ELogicalData collectLogicalData() throws MiningException, IOException, CsvException {
        ELogicalData logicalData = new ELogicalData();
        for (MiningCsvStream stream : streams) {
            ELogicalData ld = stream.getLogicalData();
            for(ELogicalAttribute la : ld.getAttributes()) {
                logicalData.addAttribute(la);
            }
        }
        return logicalData;
    }

    /**
     * Collects physical data together.
     * @return EPhysicalData
     */
    private EPhysicalData collectPhysicalData() throws MiningException, IOException, CsvException {
        EPhysicalData physicalData = new EPhysicalData();
        for (MiningCsvStream stream : streams) {
            EPhysicalData pa = stream.getPhysicalData();
            for(int i = 0; i < pa.getAttributeCount(); i++) {
                physicalData.addAttribute(pa.getAttribute(i));
            }
        }
        return physicalData;
    }

    // -----------------------------------------------------------------------
    //  Methods for getting vectors
    // -----------------------------------------------------------------------

    /**
     * Returns the current vector and moves the cursor to the next one.
     * @return MiningVector
     */
    @Override
    public MiningVector next() throws IOException {

        open();
        int pos = -1;
        int valuesNumber = 0;
        List<double[]> values = new ArrayList<>();

        try {
            for (MiningCsvStream stream : streams) {

                MiningVector mv = stream.next();
                if (pos == -1) pos = mv.getIndex();
                double[] vectorValues = mv.getValues();

                valuesNumber += vectorValues.length;
                values.add(vectorValues);
            }
        } catch (Exception e) {
            throw new OutOfMemoryError("Vectors are out.");
        }

        double[] allValues = collectValues(values, valuesNumber);
        MiningVector miningVector = new MiningVector(allValues);
        miningVector.setLogicalData(logicalData);
        miningVector.setIndex(pos);
        return miningVector;
    }

    /**
     * Returns a vector based on the specified index.
     * @param pos - index of the vector
     * @return MiningVector
     */
    @Override
    public MiningVector getVector(int pos) throws IOException {

        open();
        if (pos < 0) throw new OutOfMemoryError("Invalid index.");

        int valuesNumber = 0;
        List<double[]> values = new ArrayList<>();
        try {
            for (MiningCsvStream stream: streams) {
                MiningVector mv = stream.getVector(pos);
                double[] vectorValues = mv.getValues();

                valuesNumber += vectorValues.length;
                values.add(vectorValues);
            }
        } catch (Exception e) {
            throw new OutOfMemoryError("Invalid index.");
        }

        double[] allValues = collectValues(values, valuesNumber);
        MiningVector miningVector =  new MiningVector(allValues);
        miningVector.setLogicalData(logicalData);
        miningVector.setIndex(pos);
        return miningVector;
    }

    /**
     * Combines values from all received vectors.
     * @param values - a list containing arrays with the values of individual vectors
     * @param valuesNumber - number of all values
     * @return an array with all the values
     */
    private double[] collectValues(List<double[]> values, int valuesNumber) {
        int i = 0;
        double[] allValues = new double[valuesNumber];
        for (double[] val : values) {
            for (double v : val) {
                allValues[i] = v;
                i++;
            }
        }
        return allValues;
    }

    // -----------------------------------------------------------------------
    //  Methods for changing the stream state
    // -----------------------------------------------------------------------

    /**
     * Opens the stream.
     */
    @Override
    public void open() throws IOException {
        if (isOpen) return;

        isOpen = true;
        for (MiningCsvStream stream : streams) {
            stream.open();
            stream.setParsingValues(parsingValues);
        }
    }

    /**
     * Closes the stream.
     */
    @Override
    public void close() throws IOException {
        if (!isOpen) return;

        for (MiningCsvStream stream : streams) {
            stream.close();
        }
        isOpen = false;
    }

    /**
     * Updates all streams.
     */
    @Override
    public void reset() throws IOException {
        open();

        for (MiningCsvStream stream : streams) {
            stream.reset();
        }
    }

    @Override
    public MiningVector readPhysicalRecord() {
        return null;
    }

    @Override
    protected MiningVector movePhysicalRecord(int position) {
        return null;
    }

    /**
     * returns a copy of the current stream
     * @return HorMultiCsvStream
     */
    @Override
    public MiningMultiCsvStream getCopy() throws MiningException, IOException, CsvException {
        return new VerMultiCsvStream(streams);
    }

}
