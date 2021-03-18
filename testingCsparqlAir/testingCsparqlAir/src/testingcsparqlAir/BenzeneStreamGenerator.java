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


public class BenzeneStreamGenerator extends RdfStream implements Runnable {
    
    protected final Logger logger = LoggerFactory.getLogger(COStreamGenerator.class);	
    private int c = 1;
    private boolean keepRunning = false;
     
    private RdfQuadruple q=null;
     
    static float generatedBenzene = 0.0F;
    static long generatedTime = 0L;
    
      
    public BenzeneStreamGenerator(final String iri){
        super(iri);
    }
    
    
    @Override
    public void run() {
	keepRunning = true;
        Random rnd = new Random();
       
        while (keepRunning) {
            //Instant instant=Instant.now();
            generatedTime = System.currentTimeMillis();
            generatedBenzene =  0.1F + new Random().nextFloat() * (63.7F - (0.1F));
            
            q = new RdfQuadruple("http://localhost:8080/smartSpace#benzeneReadings" + this.c,
			"http://localhost:8080/smartSpace#hasBenzeneValue", 
                        //"http://www.semanticweb.org/40011133/ontologies/2017/10/untitled-ontology-21#temperatureValue" + this.c,
                        String.format("%.2f", generatedBenzene) ,
                        generatedTime);
                this.put(q);
            
            q = new RdfQuadruple("http://localhost:8080/smartSpace#benzeneReadings" + this.c,
			"http://localhost:8080/smartSpace#benzeneHasTimestamp", 
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
