package com.product.affiliation.repositories;

import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ScreenSize;
import junit.framework.TestCase;
import org.junit.Test;

public class MonitorRepositoryImplTest extends AbstractRepository {

    MonitorRepositoryImpl SUT;

    @Test
    public void testCreateMonitor() {
        //Given
        Monitor temp = new Monitor(null, "CK1098");
        temp.setScreenSize();

        //When
        SUT = new MonitorRepositoryImpl(this.client);
        SUT.createMonitor()

        //Then
    }
}