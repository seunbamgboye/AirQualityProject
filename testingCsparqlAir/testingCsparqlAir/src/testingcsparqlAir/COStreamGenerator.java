/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testingcsparqlAir;

import eu.larkc.csparql.cep.api.RdfQuadruple;
import eu.larkc.csparql.cep.api.RdfStream;
import java.util.Calendar;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class COStreamGenerator extends RdfStream implements Runnable {
    
    protected final Logger logger = LoggerFactory.getLogger(COStreamGenerator.class);	
    private int c = 1;
    private boolean keepRunning = false;
     
    private RdfQuadruple q=null;
     
    float generatedCO = 0.0F;
    long generatedTime = 0L;
    
    int windowsCount = 0;
  
      
    public COStreamGenerator(final String iri){
        super(iri);
    }
   
    @Override
    public void run() {
	keepRunning = true;
        Random rnd = new Random();
        
        while (keepRunning) {
            //Instant instant=Instant.now();
            generatedTime = System.currentTimeMillis();
            generatedCO = 0.1F + new Random().nextFloat() * (11.9F - (0.1F));
            windowsCount+=1;
            
            //window 5 produces plausible CO value
            if(windowsCount == 5){
                 generatedCO = BenzeneStreamGenerator.generatedBenzene;
                 generatedTime = BenzeneStreamGenerator.generatedTime;
                 windowsCount = 0;
            }
            else if(windowsCount == 2)  //window 2 produces null/missing CO value
                generatedCO = -200.00F;
            else if(windowsCount == 3) //window 3 produces inconsistent CO value
                 generatedCO = 12.0F + new Random().nextFloat() * (50.0F - (12.0F));
                
            q = new RdfQuadruple("http://localhost:8080/smartSpace#COReadings" + this.c,
			"http://localhost:8080/smartSpace#hasCOValue", 
                         String.format("%.2f", generatedCO),
                        generatedTime);
                this.put(q);
                
            q = new RdfQuadruple("http://localhost:8080/smartSpace#COReadings" + this.c,
			"http://localhost:8080/smartSpace#COHasTimestamp",
                        String.valueOf(generatedTime) ,
                        generatedTime);
                this.put(q);
            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.c++; 
            
        }
    }
}
