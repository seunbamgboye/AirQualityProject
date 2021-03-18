
package testingcsparqlAir;

import eu.larkc.csparql.cep.api.RdfQuadruple;
import eu.larkc.csparql.cep.api.RdfStream;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NOXStreamGenerator extends RdfStream implements Runnable {
    
    protected final Logger logger = LoggerFactory.getLogger(NOXStreamGenerator.class);	
    private int c = 1;
    private boolean keepRunning = false;
     
    private RdfQuadruple q=null;
     
    int generatedNOX = 0;
    long generatedTime = 0L;
  
      
    public NOXStreamGenerator(final String iri){
        super(iri);
    }
    
    @Override
    public void run() {
	keepRunning = true;
        Random rnd = new Random();
       
        while (keepRunning) {
            //Instant instant=Instant.now();
            generatedTime=System.currentTimeMillis();
            generatedNOX = 322 + (int)(Math.random()*(2683 - 322));
           
            q = new RdfQuadruple("http://localhost:8080/smartSpace#NOXReadings" + this.c,
			"http://localhost:8080/smartSpace#hasNOxValue", 
                        //"http://www.semanticweb.org/40011133/ontologies/2017/10/untitled-ontology-21#temperatureValue" + this.c,
                        String.valueOf(generatedNOX) ,
                        generatedTime);
                this.put(q);
                
            q = new RdfQuadruple("http://localhost:8080/smartSpace#NOXReadings" + this.c,
			"http://localhost:8080/smartSpace#NOxHasTimestamp", 
                        //"http://www.semanticweb.org/40011133/ontologies/2017/10/untitled-ontology-21#temperatureValue" + this.c,
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
