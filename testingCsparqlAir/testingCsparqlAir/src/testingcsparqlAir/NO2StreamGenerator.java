/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testingcsparqlAir;

import eu.larkc.csparql.cep.api.RdfQuadruple;
import eu.larkc.csparql.cep.api.RdfStream;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NO2StreamGenerator extends RdfStream implements Runnable  {
    protected final Logger logger = LoggerFactory.getLogger(NO2StreamGenerator.class);	
    private int c = 1;
    private boolean keepRunning = false;
     
    private RdfQuadruple q=null;
     
    int generatedNO2 = 0;
    long generatedTime = 0L;
    int timeCount = 0;
    
    public NO2StreamGenerator(final String iri){
        super(iri);
    }
        
    @Override
    public void run() {
	keepRunning = true;
        Random rnd = new Random();
       
        while (keepRunning) {
            //Instant instant=Instant.now();
            timeCount+=1;
            
             generatedTime = System.currentTimeMillis();
             generatedNO2 = 2 + (int)(Math.random()*(340 - 2));
                q = new RdfQuadruple("http://localhost:8080/smartSpace#NO2Readings" + this.c,
			"http://localhost:8080/smartSpace#hasNO2Value", 
                        String.valueOf(generatedNO2),
                       generatedTime);
                this.put(q);
                
               q = new RdfQuadruple("http://localhost:8080/smartSpace#NO2Readings" + this.c,
			"http://localhost:8080/smartSpace#NO2HasTimestamp",
                        String.valueOf(generatedTime) ,
                        generatedTime);
                this.put(q);
            try {
                //Thread.sleep(15*60*1000);
                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.c++;
        }
    }
}