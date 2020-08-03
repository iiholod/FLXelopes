package org.eltech.ddm.inputdata.db;

import com.opencsv.exceptions.CsvException;
import org.eltech.ddm.inputdata.MiningVector;
import org.eltech.ddm.miningcore.MiningException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MiningDBVectorTest {
    private String url;
    private String user;
    private String password;
    private String tableName;

double[][] values = {
        {0.2,5.1,3.5,1.4},
        {0.2,4.9,3.0,1.4},
        {0.2,4.7,3.2,1.3},
        {0.2,4.6,3.1,1.5},
        {0.2,5.0,3.6,1.4},
        {0.4,5.4,3.9,1.7},
        {0.3,4.6,3.4,1.4},
        {0.2,5.0,3.4,1.5},
        {0.2,4.4,2.9,1.4},
        {0.1,4.9,3.1,1.5},
        {0.2,5.4,3.7,1.5},
        {0.2,4.8,3.4,1.6},
        {0.1,4.8,3.0,1.4},
        {0.1,4.3,3.0,1.1},
        {0.2,5.8,4.0,1.2},
        {0.4,5.7,4.4,1.5},
        {0.4,5.4,3.9,1.3},
        {0.3,5.1,3.5,1.4},
        {0.3,5.7,3.8,1.7},
        {0.3,5.1,3.8,1.5},
        {0.2,5.4,3.4,1.7},
        {0.4,5.1,3.7,1.5},
        {0.2,4.6,3.6,1.0},
        {0.5,5.1,3.3,1.7},
        {0.2,4.8,3.4,1.9},
        {0.2,5.0,3.0,1.6},
        {0.4,5.0,3.4,1.6},
        {0.2,5.2,3.5,1.5},
        {0.2,5.2,3.4,1.4},
        {0.2,4.7,3.2,1.6},
        {0.2,4.8,3.1,1.6},
        {0.4,5.4,3.4,1.5},
        {0.1,5.2,4.1,1.5},
        {0.2,5.5,4.2,1.4},
        {0.1,4.9,3.1,1.5},
        {0.2,5.0,3.2,1.2},
        {0.2,5.5,3.5,1.3},
        {0.1,4.9,3.1,1.5},
        {0.2,4.4,3.0,1.3},
        {0.2,5.1,3.4,1.5},
        {0.3,5.0,3.5,1.3},
        {0.3,4.5,2.3,1.3},
        {0.2,4.4,3.2,1.3},
        {0.6,5.0,3.5,1.6},
        {0.4,5.1,3.8,1.9},
        {0.3,4.8,3.0,1.4},
        {0.2,5.1,3.8,1.6},
        {0.2,4.6,3.2,1.4},
        {0.2,5.3,3.7,1.5},
        {0.2,5.0,3.3,1.4},
        {1.4,7.0,3.2,4.7},
        {1.5,6.4,3.2,4.5},
        {1.5,6.9,3.1,4.9},
        {1.3,5.5,2.3,4.0},
        {1.5,6.5,2.8,4.6},
        {1.3,5.7,2.8,4.5},
        {1.6,6.3,3.3,4.7},
        {1.0,4.9,2.4,3.3},
        {1.3,6.6,2.9,4.6},
        {1.4,5.2,2.7,3.9},
        {1.0,5.0,2.0,3.5},
        {1.5,5.9,3.0,4.2},
        {1.0,6.0,2.2,4.0},
        {1.4,6.1,2.9,4.7},
        {1.3,5.6,2.9,3.6},
        {1.4,6.7,3.1,4.4},
        {1.5,5.6,3.0,4.5},
        {1.0,5.8,2.7,4.1},
        {1.5,6.2,2.2,4.5},
        {1.1,5.6,2.5,3.9},
        {1.8,5.9,3.2,4.8},
        {1.3,6.1,2.8,4.0},
        {1.5,6.3,2.5,4.9},
        {1.2,6.1,2.8,4.7},
        {1.3,6.4,2.9,4.3},
        {1.4,6.6,3.0,4.4},
        {1.4,6.8,2.8,4.8},
        {1.7,6.7,3.0,5.0},
        {1.5,6.0,2.9,4.5},
        {1.0,5.7,2.6,3.5},
        {1.1,5.5,2.4,3.8},
        {1.0,5.5,2.4,3.7},
        {1.2,5.8,2.7,3.9},
        {1.6,6.0,2.7,5.1},
        {1.5,5.4,3.0,4.5},
        {1.6,6.0,3.4,4.5},
        {1.5,6.7,3.1,4.7},
        {1.3,6.3,2.3,4.4},
        {1.3,5.6,3.0,4.1},
        {1.3,5.5,2.5,4.0},
        {1.2,5.5,2.6,4.4},
        {1.4,6.1,3.0,4.6},
        {1.2,5.8,2.6,4.0},
        {1.0,5.0,2.3,3.3},
        {1.3,5.6,2.7,4.2},
        {1.2,5.7,3.0,4.2},
        {1.3,5.7,2.9,4.2},
        {1.3,6.2,2.9,4.3},
        {1.1,5.1,2.5,3.0},
        {1.3,5.7,2.8,4.1},
        {2.5,6.3,3.3,6.0},
        {1.9,5.8,2.7,5.1},
        {2.1,7.1,3.0,5.9},
        {1.8,6.3,2.9,5.6},
        {2.2,6.5,3.0,5.8},
        {2.1,7.6,3.0,6.6},
        {1.7,4.9,2.5,4.5},
        {1.8,7.3,2.9,6.3},
        {1.8,6.7,2.5,5.8},
        {2.5,7.2,3.6,6.1},
        {2.0,6.5,3.2,5.1},
        {1.9,6.4,2.7,5.3},
        {2.1,6.8,3.0,5.5},
        {2.0,5.7,2.5,5.0},
        {2.4,5.8,2.8,5.1},
        {2.3,6.4,3.2,5.3},
        {1.8,6.5,3.0,5.5},
        {2.2,7.7,3.8,6.7},
        {2.3,7.7,2.6,6.9},
        {1.5,6.0,2.2,5.0},
        {2.3,6.9,3.2,5.7},
        {2.0,5.6,2.8,4.9},
        {2.0,7.7,2.8,6.7},
        {1.8,6.3,2.7,4.9},
        {2.1,6.7,3.3,5.7},
        {1.8,7.2,3.2,6.0},
        {1.8,6.2,2.8,4.8},
        {1.8,6.1,3.0,4.9},
        {2.1,6.4,2.8,5.6},
        {1.6,7.2,3.0,5.8},
        {1.9,7.4,2.8,6.1},
        {2.0,7.9,3.8,6.4},
        {2.2,6.4,2.8,5.6},
        {1.5,6.3,2.8,5.1},
        {1.4,6.1,2.6,5.6},
        {2.3,7.7,3.0,6.1},
        {2.4,6.3,3.4,5.6},
        {1.8,6.4,3.1,5.5},
        {1.8,6.0,3.0,4.8},
        {2.1,6.9,3.1,5.4},
        {2.4,6.7,3.1,5.6},
        {2.3,6.9,3.1,5.1},
        {1.9,5.8,2.7,5.1},
        {2.3,6.8,3.2,5.9},
        {2.5,6.7,3.3,5.7},
        {2.3,6.7,3.0,5.2},
        {1.9,6.3,2.5,5.0},
        {2.0,6.5,3.0,5.2},
        {2.3,6.2,3.4,5.4},
        {1.8,5.9,3.0,5.1}
};
    final double threshold = 0.1;

    @Before
    public void setUp() throws Exception {
        url = "jdbc:postgresql://localhost:5432/iris";
        user = "postgres";
        password = "qwerty";
        tableName = "iris";

    }

    @Test
    public void vectorTest() throws MiningException, InterruptedException, IOException, CsvException {
        MiningDBStream miningDBStream = new MiningDBStream(url, user, password, tableName);
        int i,j;
        boolean fra[] = new boolean[150];
        double mbStream, valuesStream;
        /*
        *   Проверка полученых значений
         */
        for (i=0;i<145;i++){
           MiningVector mining = miningDBStream.getVector(i);
           System.out.println(mining);
            for (j=0;j<=3;j++) {
                mbStream= miningDBStream.getVector(i).getValue(j);
                valuesStream = values[i][j];
              fra[i] = Math.abs(values[i][j]-mbStream)<threshold;

             assertEquals(true,Math.abs(values[i][j]-miningDBStream.getVector(i).getValue(j))<threshold);
            }
        }

    }
}