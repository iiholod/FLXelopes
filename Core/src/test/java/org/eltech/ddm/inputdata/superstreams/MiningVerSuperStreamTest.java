package org.eltech.ddm.inputdata.superstreams;

import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.inputdata.file.csv.CsvParsingSettings;
import org.eltech.ddm.inputdata.file.csv.MiningCsvStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningdata.ELogicalData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tested VerMultiStream class.
 * A class containing tests for checking the health of methods of the VerMultiStream class.
 * @author Maxim Kolpaschikov
 */
public class MiningVerSuperStreamTest extends MiningSuperStreamTest {
    @Before
    public void setup() throws MiningException {

        CsvParsingSettings settings = new CsvParsingSettings();
        settings.setSeparator(',');
        settings.setHeaderAvailability(true);

        MiningInputStream[] streams = new MiningCsvStream[]{
                new MiningCsvStream("../data/csv/iris_ver1.csv", settings),
                new MiningCsvStream("../data/csv/iris_ver2.csv", settings),
                new MiningCsvStream("../data/csv/iris_ver3.csv", settings)};

        setup(new MiningVerSuperStream(streams));
    }


    @Test
    public void vectorsTest() throws MiningException {
        vecTest();
    }

    @Test
    public void attributeNumberTest() throws MiningException {
        attrNumberTest();
    }

    @Test
    public void logicalDataTest() throws MiningException {
        ELogicalData logicalData = super.logicalData;
        assertEquals("sepallength", logicalData.getAttribute(0).getName());
        assertEquals("sepalwidth", logicalData.getAttribute(1).getName());
        assertEquals("petallength", logicalData.getAttribute(2).getName());
        assertEquals("petalwidth", logicalData.getAttribute(3).getName());
        assertEquals("iris-class", logicalData.getAttribute(4).getName());
    }

    @Test
    public void physicalDataTest()  {
        assertEquals("sepallength numerical", physicalData.getAttribute(0).toString());
        assertEquals("sepalwidth numerical", physicalData.getAttribute(1).toString());
        assertEquals("petallength numerical", physicalData.getAttribute(2).toString());
        assertEquals("petalwidth numerical", physicalData.getAttribute(3).toString());
        assertEquals("iris-class numerical", physicalData.getAttribute(4).toString());
    }

    @After
    public void closeStream() throws MiningException {
        close();
    }
}
