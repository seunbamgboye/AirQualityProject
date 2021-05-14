/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testingcsparqlAir;


import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.sparql.graph.GraphFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import eu.larkc.csparql.cep.api.RdfStream;
import eu.larkc.csparql.common.RDFTable;
import eu.larkc.csparql.common.RDFTuple;
import eu.larkc.csparql.core.engine.ConsoleFormatter;
import eu.larkc.csparql.core.engine.CsparqlEngine;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import eu.larkc.csparql.core.engine.RDFStreamFormatter;
import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;
import java.util.LinkedHashMap;

import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openrdf.rio.RDFParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;


public class TestingCsparqlAir {

    private static Logger logger = LoggerFactory.getLogger(TestingCsparqlAir.class);
    
    public static InfModel infModel;
    private static final String BASE="http://localhost:8080/smartSpace#";
    private static final String BASE1="http://localhost:8080/smartSpace/";
    static String analysisText="",CSparqlQueryAnalysisText="",precisonAnalysisText="",processingAnalysisText="",latencyAnalysisText="", inferredText = "";
    static String CSparqlAnalysis = "", latencyAnalysis = "", processingAnalysis = "";
    static String precisionAnalysisInconsistency = "",precisionAnalysisConsistency = "", precisionAnalysisPlausibility = "";
    static String recallAnalysisInconsistency = "", recallAnalysisConsistency = "", recallAnalysisPlausibility = "";
    static String saveRuleAnalysisText,inferenceRule;
    static String winterAnalysis="",springAnalysis="",summerAnalysis="",autumAnalysis="",heatAnalysis="",coolantAnalysis="",heatcoolantAnalysis="",consistencyAnalysis="",humidityAnalysis="";
    static String inconsistencyValues = "", plausibilityValues = "", nullValues = "", consistencyValues = "";
    
    static OntModel ontModelHistorical = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
    
    static String currentPrecisionInconsistency, currentPrecisionConsistency, currentPrecisionPlausibility;
    static String  currentRecallInconsistency, currentRecallConsistency, currentRecallPlausibility;
    
    static String currentPrecision = "", currentRecall = "", currentAccuracy = "";
    static String precisionAnalysis, recallAnalysis, accuracyAnalysis;
   
    
    static String currentInconsistencyValues, currentPlausibityValues, currentNullValues, currentConsistencyValues;
    
    static InputStream inputStream=null;
    static String language_RDF="RDF/XML",language_TURTLE="TURTLE",language_NTriple="N-TRIPLES",language_N3="N3",language_JSONLD="JSON-LD";
//    static String smartSpaceRDF= "C:\\Users\\user\\Documents\\SmartSUM\\dataset\\smartSpace.rdf";
    static String smartSpaceRDF= "C:\\Users\\user\\Documents\\SmartSUM\\ontologies\\smartSpace.rdf";
    
    static OntClass ontClassHistoricalCOValue;
    static OntClass ontClassHistoricalNOXValue;
    static OntClass ontClassHistoricalNO2Value;
    static OntClass ontClassHistoricalMethaneValue;
    static OntClass ontClassHistoricalBenzeneValue;
   
    static Individual individualHistoricalCOReadings;
    static Individual individualHistoricalNOXReadings;
    static Individual individualHistoricalNO2Reading;
    static Individual individualHistoricalMethaneReading;
    static Individual individualHistoricalBenzeneReading;
   
    
    static  Instant beforeQuery=null;
    static int cycleCount = 0;
    static int totalMeanAccuracyCountRDF = 0, totalMeanPrecisionCountRDF = 0;
    static int totalMeanAccuracyCountN3 = 0, totalMeanPrecisionCountN3 = 0;
    static int totalMeanAccuracyCountTurtle = 0, totalMeanPrecisionCountTurtle = 0;
    static int totalMeanAccuracyCountNTripple = 0, totalMeanPrecisionCountNTripple = 0;
    
    static float doorDistanceFromTempS,fluoroDistanceFromTempS,radDistanceFromTempS,coolantDistanceFromTemps,windowDistanceFromTempS;
    
    static Random rand;
    static int totalQuadruple=0,previousQuadrupleInferred=0;
    static long latencyTime=0;
    static boolean inferredBefore=false;
    static Instant initialInstant;
    
    static float precisionInconsistency, precisionConsistency, precisionPlausibilty;
    
    static float precision = 0.0F, recall = 0.0F, accuracy = 0.0F;
    static float totalPrecisionRDF = 0.0F ,totalAccuracyRDF = 0.0F;
    static float totalPrecisionN3 = 0.0F ,totalAccuracyN3 = 0.0F;
    static float totalPrecisionNTripple = 0.0F ,totalAccuracyNTripple = 0.0F;
    static float totalPrecisionTurtle = 0.0F ,totalAccuracyTurtle = 0.0F;
    
    static float recallInconsistency, recallConsistency, recallPlausibilty;
    static int inconsistencyCount=0,consistencyCount=0,nullCount=0,plausibiltyCounts=0;
    static float meanHumidity, meanPressure;
    static long currentTimestamp = 0;
    static String meanTime = "";
    static int tempSensorDelay = 0;
    static JSONObject server_data = null;
    static boolean actuatorTriggered = false;
    
    static String csvAnalysis_RDF = "Window,Precision,Accuracy,Mean_Precision,Mean_Accuracy,Reasoning_Time,Latency,Processing_Time";
    static String csvAnalysis_N3 = "Window,Precision,Accuracy,Mean_Precision,Mean_Accuracy,Reasoning_Time,Latency,Processing_Time";
    static String csvAnalysis_Turtle = "Window,Precision,Accuracy,Mean_Precision,Mean_Accuracy,Reasoning_Time,Latency,Processing_Time";
    static String csvAnalysis_NTripple = "Window,Precision,Accuracy,Mean_Precision,Mean_Accuracy,Reasoning_Time,Latency,Processing_Time";
    
    public static void main(String[] args) {
        // TODO code application logic here
       
       
        try {
            inputStream = new FileInputStream(smartSpaceRDF);
            ontModelHistorical.read(inputStream, language_RDF);
//            System.out.println(historicaldModel);
                     //streamingModel.write(System.out, "RDF/XML");
            ontClassHistoricalCOValue = ontModelHistorical.getOntClass(BASE1+"COReadings");
            ontClassHistoricalNOXValue = ontModelHistorical.getOntClass(BASE1+"NOXReadings");
            ontClassHistoricalNO2Value = ontModelHistorical.getOntClass(BASE1+"NO2Readings");
             ontClassHistoricalMethaneValue = ontModelHistorical.getOntClass(BASE1+"MethaneReadings");
            ontClassHistoricalBenzeneValue = ontModelHistorical.getOntClass(BASE1+"benzeneReadings");
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
	}
        
        String query = null;
       
	RdfStream tg = null;
        RdfStream tg2 = null;
        RdfStream tg3 = null;
        RdfStream tg4 = null;
        RdfStream tg5 = null;
        
         Hashtable inferencingModels = new Hashtable();
         inferencingModels.put(language_RDF, "rdf");
         inferencingModels.put(language_N3, "n3");
         inferencingModels.put(language_TURTLE, "ttl");
         inferencingModels.put(language_NTriple, "nt");
         
        
        JSONObject JSONAnalysis = new JSONObject();
       
        // Initialize C-SPARQL Engine
	CsparqlEngine engine = new CsparqlEngineImpl();
        
        engine.initialize(true);
        
        query = "REGISTER QUERY sensorValueOf AS "
        + "PREFIX smartSpace:<http://localhost:8080/smartSpace#> "
        + "SELECT * "        
	+ "FROM STREAM <http://localhost:8080/smartSpace/streamCO> [RANGE 10s STEP 10s] "
        + "FROM STREAM <http://localhost:8080/smartSpace/streamNOX> [RANGE 10s STEP 10s] "
        + "FROM STREAM <http://localhost:8080/smartSpace/streamNO2> [RANGE 10s STEP 10s] "
        + "FROM STREAM <http://localhost:8080/smartSpace/streamBenzene> [RANGE 10s STEP 10s] "
	+ "WHERE {"
        + "?COReadings smartSpace:hasCOValue ?COVal."
        + "?COReadings smartSpace:COHasTimestamp ?COTime."  
        + "?NOXReadings smartSpace:hasNOxValue ?NOXVal."
        + "?NOXReadings smartSpace:NOxHasTimestamp ?NOXTime."
        + "?NO2Readings smartSpace:hasNO2Value ?NO2Val."
        + "?NO2Readings smartSpace:NO2HasTimestamp ?NO2Time."
        + "?benzeneReadings smartSpace:hasBenzeneValue ?benzeneVal."
        + "?benzeneReadings smartSpace:benzeneHasTimestamp ?benzeneTime."
        + "}"
        +"ORDER BY ASC(?COTime)";
      

	tg = new NO2StreamGenerator("http://localhost:8080/smartSpace/streamNO2");
        tg2 = new NOXStreamGenerator("http://localhost:8080/smartSpace/streamNOX");
        tg3= new COStreamGenerator("http://localhost:8080/smartSpace/streamCO");
        tg4 = new BenzeneStreamGenerator("http://localhost:8080/smartSpace/streamBenzene");
        tg5 = new MethaneStreamGenerator("http://localhost:8080/smartSpace/streamBenzene");

        // Register an RDF Stream
        engine.registerStream(tg);
        engine.registerStream(tg2);
        engine.registerStream(tg3);
        engine.registerStream(tg4);
        engine.registerStream(tg5);
        
        final Thread t = new Thread((Runnable) tg);
	t.start();
        final Thread t2 = new Thread((Runnable) tg2);
	t2.start();
        final Thread t3 = new Thread((Runnable) tg3);
	t3.start();
        final Thread t4 = new Thread((Runnable) tg4);
	t4.start();
        final Thread t5 = new Thread((Runnable) tg5);
	t5.start();

        CsparqlQueryResultProxy c1 = null;
        
        try {
            c1 = engine.registerQuery(query, false);
            //logger.debug("Query: {}", query);
            logger.debug("Query Start Time : {}", System.currentTimeMillis());
        } catch (final ParseException ex) {
            logger.error(ex.getMessage(), ex);
	}
        
        beforeQuery = Instant.now();
        if (c1 != null) {
            c1.addObserver( new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    actuatorTriggered = false;
                    cycleCount+=1;
                    tempSensorDelay+=1;
                    try{
                        
                    }catch(Exception ex){
                        System.out.println(ex.toString());
                    }
                    
                    float totalValue = 0.0f;
                    Instant afterQuery = Instant.now();
                    final Duration csparqlQueryTimeElapsed = Duration.between(beforeQuery, afterQuery);
                    System.out.println("Total Window Cycle: "+ cycleCount);
                    System.out.println("CSPARQL Query Time: "+(csparqlQueryTimeElapsed.toMillis()/1000)+"."+String.format("%04d",(csparqlQueryTimeElapsed.toMillis()%1000)));
                    String durationCsparqlQuery = "" + (csparqlQueryTimeElapsed.toMillis()/1000)+"."+String.format("%04d",(csparqlQueryTimeElapsed.toMillis()%1000));
                    
                    beforeQuery = afterQuery;
                    
                     final RDFTable rdfTable = (RDFTable) arg;
                     System.out.println("");
                     final String[] vars = rdfTable.getNames().toArray(new String[]{});
                     Date date = new Date(System.currentTimeMillis()); //Input your time in milliseconds
                     String dateString = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a").format(date);
                     System.out.println("Raw quadruples with duplicated readings: " + rdfTable.size());
                  
                     Hashtable COData = new Hashtable();
                     Hashtable NOXData = new Hashtable();
                     Hashtable NO2Data = new Hashtable();
                     Hashtable MethaneData = new Hashtable();
                     Hashtable benzeneData = new Hashtable();
                     
                     Hashtable COTime = new Hashtable();
                     Hashtable NOXTime = new Hashtable();
                     Hashtable NO2Time = new Hashtable();
                     Hashtable MethaneTime = new Hashtable();
                     Hashtable benzeneTime = new Hashtable();
                     
                     String JSONData = rdfTable.getJsonSerialization();
                     
                     try{
                            JSONObject JSONObj = new JSONObject(JSONData);
                            JSONObject JSONResults = JSONObj.getJSONObject("results");
                            JSONArray JSONBindings = JSONResults.getJSONArray("bindings");
                            for(int i=0; i<JSONBindings.length();i++){
                                JSONObject JSONDataset=JSONBindings.getJSONObject(i);
                             
                                String strCOReadings = JSONDataset.getJSONObject("COReadings").getString("value");
                                String strCOValue = JSONDataset.getJSONObject("COVal").getString("value");
                                String strCOTime = JSONDataset.getJSONObject("COTime").getString("value");
                                
                                String strNOXReadings = JSONDataset.getJSONObject("NOXReadings").getString("value");
                                String strNOXValue = JSONDataset.getJSONObject("NOXVal").getString("value");
                                String strNOXTime = JSONDataset.getJSONObject("NOXTime").getString("value");
                                
                                String strNO2Readings = JSONDataset.getJSONObject("NO2Readings").getString("value");
                                String strNO2Value = JSONDataset.getJSONObject("NO2Val").getString("value");
                                String strNO2Time = JSONDataset.getJSONObject("NO2Time").getString("value");
                                
                                String strMethaneReadings = JSONDataset.getJSONObject("MethaneReadings").getString("value");
                                String strMethaneValue = JSONDataset.getJSONObject("MethaneVal").getString("value");
                                String strMethaneTime = JSONDataset.getJSONObject("MethaneTime").getString("value");
                                
                                String strBenzeneReadings = JSONDataset.getJSONObject("benzeneReadings").getString("value");
                                String strBenzeneValue=JSONDataset.getJSONObject("benzeneVal").getString("value");
                                String strBenzeneTime = JSONDataset.getJSONObject("benzeneTime").getString("value");
                               
                                if(!COData.containsKey(strCOReadings)){
                                    COData.put(strCOReadings, strCOValue);
                                    COTime.put(strCOReadings,strCOTime);
                                }
                                
                                if(!NOXData.containsKey(strNOXReadings)){
                                    NOXData.put(strNOXReadings, strNOXValue);
                                    NOXTime.put(strNOXReadings,strNOXTime);
                                }
                                
                                if(!NO2Data.containsKey(strNO2Readings)){
                                    NO2Data.put(strNO2Readings, strNO2Value);
                                    NO2Time.put(strNO2Readings,strNO2Time);
                                }
                                
                                if(!MethaneData.containsKey(strMethaneReadings)){
                                    MethaneData.put(strMethaneReadings, strMethaneValue);
                                    MethaneTime.put(strMethaneReadings,strMethaneTime);
                                }
                                
                                if(!benzeneData.containsKey(strBenzeneReadings)){
                                    benzeneData.put(strBenzeneReadings, strBenzeneValue);
                                    benzeneTime.put(strBenzeneReadings,strBenzeneTime);
                                } 
 
                            }
                            int nonredundantData = COData.size()+ NOXData.size() + NO2Data.size() + benzeneData.size();
                            System.out.println("----"+nonredundantData+" result at SystemTime = ["+dateString+"]-----");
                            
                            OntModel ontModelStreaming = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
                           
                            OntClass ontClassStreamingCOReading = ontModelStreaming.createClass(BASE+"COReadings");
                            OntClass ontClassStreamingNOXReading = ontModelStreaming.createClass(BASE+"NOXReadings");
                            OntClass ontClassStreamingNO2Reading = ontModelStreaming.createClass(BASE+"NO2Readings");
                            OntClass ontClassStreamingBenzeneReading = ontModelStreaming.createClass(BASE+"benzeneReadings");
                           
                            Individual individualStreamingCOValue;
                            Individual individualStreamingNOXValue;
                            Individual individualStreamingNO2Value;
                            Individual individualStreamingBenzeneValue;
                            
                            Instant instant = null;
                            
                            Enumeration dataEnum = COData.keys();
                            while (dataEnum.hasMoreElements()) {
                              String key = (String) dataEnum.nextElement();
                              individualStreamingCOValue = ontClassStreamingCOReading.createIndividual(key);
                              individualStreamingCOValue.addProperty(p("hasCOValue"),l1(String.valueOf(COData.get(key)),XSDDatatype.XSDfloat));
                              instant = Instant.ofEpochMilli(Long.valueOf(String.valueOf(COTime.get(key))));
                              individualStreamingCOValue.addProperty(p("COHasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                              
                              individualHistoricalCOReadings = ontClassHistoricalCOValue.createIndividual(key);
                              individualHistoricalCOReadings.addProperty(p("hasCOValue"),l1(String.valueOf(COData.get(key)),XSDDatatype.XSDfloat));
                              individualHistoricalCOReadings.addProperty(p("COHasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                            }
                            
                            dataEnum = NOXData.keys();
                            while (dataEnum.hasMoreElements()) {
                              String key = (String) dataEnum.nextElement();
                              individualStreamingNOXValue = ontClassStreamingNOXReading.createIndividual(key);
                              individualStreamingNOXValue.addProperty(p("hasNOxValue"),l1(String.valueOf(NOXData.get(key)),XSDDatatype.XSDinteger));
                              instant = Instant.ofEpochMilli(Long.valueOf(String.valueOf(NOXTime.get(key))));
                              
                              individualStreamingNOXValue.addProperty(p("NOxHasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                              
                              individualHistoricalNOXReadings = ontClassHistoricalNOXValue.createIndividual(key);
                              individualHistoricalNOXReadings.addProperty(p("hasNOxValue"),l1(String.valueOf(NOXData.get(key)),XSDDatatype.XSDinteger));
                              individualHistoricalNOXReadings.addProperty(p("NOxHasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                            }
                           
                            dataEnum = NO2Data.keys();
                            while (dataEnum.hasMoreElements()) {
                              String key = (String) dataEnum.nextElement();
                              individualStreamingNO2Value = ontClassStreamingNO2Reading.createIndividual(key);
                              individualStreamingNO2Value.addProperty(p("hasNO2Value"),l1(String.valueOf(NO2Data.get(key)),XSDDatatype.XSDinteger));
                              instant = Instant.ofEpochMilli(Long.valueOf(String.valueOf(NO2Time.get(key))));
                              individualStreamingNO2Value.addProperty(p("NO2HasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                               
                              individualHistoricalNO2Reading = ontClassHistoricalNO2Value.createIndividual(key);
                              individualHistoricalNO2Reading.addProperty(p("hasNO2Value"),l1(String.valueOf(NO2Data.get(key)),XSDDatatype.XSDinteger));
                              individualHistoricalNO2Reading.addProperty(p("NO2HasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                            }
                            
                            dataEnum = MethaneData.keys();
                            while (dataEnum.hasMoreElements()) {
                              String key = (String) dataEnum.nextElement();
                              individualStreamingMethaneValue = ontClassStreamingMethaneReading.createIndividual(key);
                              individualStreamingMethaneValue.addProperty(p("hasMethaneValue"),l1(String.valueOf(NO2Data.get(key)),XSDDatatype.XSDinteger));
                              instant = Instant.ofEpochMilli(Long.valueOf(String.valueOf(MethaneTime.get(key))));
                              individualStreamingMethaneValue.addProperty(p("MethaneHasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                               
                              individualHistoricalMethaneReading = ontClassHistoricalMethaneValue.createIndividual(key);
                              individualHistoricalMethaneReading.addProperty(p("hasMethaneValue"),l1(String.valueOf(MethaneData.get(key)),XSDDatatype.XSDinteger));
                              individualHistoricalMethaneReading.addProperty(p("MethaneHasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                            }
                            
                            dataEnum = benzeneData.keys();
                            
                            while (dataEnum.hasMoreElements()) {
                              String key = (String) dataEnum.nextElement();
                              individualStreamingBenzeneValue = ontClassStreamingBenzeneReading.createIndividual(key);
                              individualStreamingBenzeneValue.addProperty(p("hasBenzeneValue"),l1(String.valueOf(benzeneData.get(key)),XSDDatatype.XSDfloat));
                             
                              instant = Instant.ofEpochMilli(Long.valueOf(String.valueOf(benzeneTime.get(key))));
                              individualStreamingBenzeneValue.addProperty(p("benzeneHasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                              
                              individualHistoricalBenzeneReading = ontClassHistoricalBenzeneValue.createIndividual(key);
                              individualHistoricalBenzeneReading.addProperty(p("hasBenzeneValue"),l1(String.valueOf(benzeneData.get(key)),XSDDatatype.XSDfloat));
                              individualHistoricalBenzeneReading.addProperty(p("benzeneHasTimestamp"),l1(String.valueOf(instant),XSDDatatype.XSDdateTime));
                            }

                            ontModelStreaming.setNsPrefix("smartSpace", BASE);
                            String saveStreamRDFFile="C:\\Users\\user\\Documents\\SmartSUM\\dataset\\streamData.rdf";
                            String saveStreamTURTLEFile="C:\\Users\\user\\Documents\\SmartSUM\\dataset\\streamDataTurtle.ttl";
                            String saveStreamNTRIPLEFile="C:\\Users\\user\\Documents\\SmartSUM\\dataset\\streamDataNTriple.nt";
                            String saveStreamN3File="C:\\Users\\user\\Documents\\SmartSUM\\dataset\\streamDataN3.n3";
                            String saveStreamJSONLDFile="C:\\Users\\user\\Documents\\SmartSUM\\dataset\\streamDataJsonld.jsonld";
                            
                            ontModelHistorical.setNsPrefix("smartSpace",BASE);
                            String saveHistoricalRDFFile = "C:\\Users\\user\\Documents\\SmartSUM\\dataset\\smartSpace.rdf";
                            //writeout onyology as in RDF format
                            OutputStream output = new FileOutputStream(saveStreamRDFFile);
                            RDFDataMgr.write(output, ontModelStreaming, RDFFormat.RDFXML_ABBREV);
                            
                            //writeout onyology as in TUTRLE format
                            output = new FileOutputStream(saveStreamTURTLEFile);
                            RDFDataMgr.write(output, ontModelStreaming,RDFFormat.TURTLE);
                            
                            
                            //writeout onyology as in RDF format
                            //output=new FileOutputStream(saveStreamNTRIPLEFile);
                            //RDFDataMgr.write(output, streamingModel,RDFFormat.NTRIPLES);
                            
                            output = new FileOutputStream(saveStreamNTRIPLEFile);
                            ontModelStreaming.write(output,"N-TRIPLES");
                            
                            output = new FileOutputStream(saveStreamN3File);
                            ontModelStreaming.write(output,"N3");
                            
                            output = new FileOutputStream(saveHistoricalRDFFile);
                            RDFDataMgr.write(output,ontModelHistorical,RDFFormat.RDFXML_ABBREV);
                            
                            String latencyAnalysisText = "";
                            
                            Enumeration enumInferencinModel = inferencingModels.keys();
                            final int individualsInferred = COData.size() + NOXData.size() + NO2Data.size() + benzeneData.size();
                            totalQuadruple += individualsInferred;
                            
                            while(enumInferencinModel.hasMoreElements()){
                                String language = (String) enumInferencinModel.nextElement();
                                String extension = String.valueOf(inferencingModels.get(language));
                                System.out.println("---------Reasoning " + language + " with file extension: " + extension + " ------------");
                                
                                Instant beforeInferencing = Instant.now();
                            
                                if(!inferredBefore){
    //                                latencyAnalysisText="0:0.0";
                                    latencyAnalysisText = "0.0";
                                    inferredBefore = true;
                                }else{
                                    final Duration latencyElapsed = Duration.between(initialInstant, beforeInferencing);
    //                                latencyAnalysisText = previousQuadrupleInferred+":"+(latencyElapsed.toMillis()/1000)+"."+String.format("%03d",(latencyElapsed.toMillis()%1000));
                                    latencyAnalysisText = (latencyElapsed.toMillis()/1000)+"."+String.format("%04d",(latencyElapsed.toMillis()%1000));
                                }
                            
                                String temporaryModelToBeinfered = "C:\\Users\\user\\Documents\\SmartSUM\\rules\\air\\tempmodel." + extension;
//                                String inferencingLanguage = language;
                                reasonOnOntology(ontModelStreaming, language,temporaryModelToBeinfered);

                                Instant afterInferencing = Instant.now();
                           
                                final Duration timeElapsed = Duration.between(beforeInferencing, afterInferencing);
                               

                                initialInstant = afterInferencing;
                                previousQuadrupleInferred = individualsInferred;

                                String durationReasoning = ""+(timeElapsed.toMillis()/1000)+"."+String.format("%04d",(timeElapsed.toMillis()%1000));

                                Duration processingElapsedTime = csparqlQueryTimeElapsed.plusMillis(timeElapsed.toMillis());
                                String durationProcessing = (processingElapsedTime.toMillis()/1000)+"."+String.format("%04d",(processingElapsedTime.toMillis()%1000));

                                System.out.println("Quadruple: " +individualsInferred);
                                System.out.println("Latency: " + latencyAnalysisText);
                                System.out.println("Reasoning Time: " + durationReasoning);
                                System.out.println("Processing Time: " + durationProcessing);
                                System.out.println("Total Quadruple inferred: " + totalQuadruple);
                                System.out.println("");
                                float meanPrecision = 0.0F;
                                float meanAccuracy = 0.0F;
                                
                                switch(language){
                                    case "RDF/XML":
                                        if(Float.valueOf(precision).isNaN() || precision == 0.0){
                                            precision = 0.00F;
                                            meanPrecision = 0.00F;
                                        }else{
                                            totalMeanPrecisionCountRDF += 1;
                                            totalPrecisionRDF += precision;
                                            meanPrecision = (float)(totalPrecisionRDF/totalMeanPrecisionCountRDF);
                                        }

                                        if(Float.valueOf(accuracy).isNaN()|| accuracy == 0.0){
                                            accuracy = 0.00F;
                                            meanAccuracy = 0.00F;
                                         }else{
                                            totalMeanAccuracyCountRDF += 1;
                                            totalAccuracyRDF += accuracy;
                                            meanAccuracy = (float)(totalAccuracyRDF/totalMeanAccuracyCountRDF);
                                        }
                                    break;
                                        
                                    case "TURTLE":
                                        if(Float.valueOf(precision).isNaN() || precision == 0.0){
                                            precision = 0.00F;
                                            meanPrecision = 0.00F;
                                        }else{
                                            totalMeanPrecisionCountTurtle += 1;
                                            totalPrecisionTurtle += precision;
                                            meanPrecision = (float)(totalPrecisionTurtle/totalMeanPrecisionCountTurtle);
                                        }

                                        if(Float.valueOf(accuracy).isNaN()|| accuracy == 0.0){
                                            accuracy = 0.00F;
                                            meanAccuracy = 0.00F;
                                         }else{
                                            totalMeanAccuracyCountTurtle += 1;
                                            totalAccuracyTurtle += accuracy;
                                            meanAccuracy = (float)(totalAccuracyTurtle/totalMeanAccuracyCountTurtle);
                                        }
                                    break;
                                    
                                    case "N-TRIPLES":
                                        if(Float.valueOf(precision).isNaN() || precision == 0.0){
                                            precision = 0.00F;
                                            meanPrecision = 0.00F;
                                        }else{
                                            totalMeanPrecisionCountNTripple += 1;
                                            totalPrecisionNTripple += precision;
                                            meanPrecision = (float)(totalPrecisionNTripple/totalMeanPrecisionCountNTripple);
                                        }

                                        if(Float.valueOf(accuracy).isNaN()|| accuracy == 0.0){
                                            accuracy = 0.00F;
                                            meanAccuracy = 0.00F;
                                        }else{
                                            totalMeanAccuracyCountNTripple += 1;
                                            totalAccuracyNTripple += accuracy;
                                            meanAccuracy = (float)(totalAccuracyNTripple/totalMeanAccuracyCountNTripple);
                                        }
                                    break;
                                    
                                    case "N3":
                                        if(Float.valueOf(precision).isNaN() || precision == 0.0){
                                            precision = 0.00F;
                                            meanPrecision = 0.00F;
                                        }else{
                                            totalMeanPrecisionCountN3 += 1;
                                            totalPrecisionN3 += precision;
                                            meanPrecision = (float)(totalPrecisionN3/totalMeanPrecisionCountN3);
                                        }

                                        if(Float.valueOf(accuracy).isNaN()|| accuracy == 0.0){
                                            accuracy = 0.00F;
                                            meanAccuracy = 0.00F;
                                        }else{
                                            totalMeanAccuracyCountN3 += 1;
                                            totalAccuracyN3 += accuracy;
                                            meanAccuracy = (float)(totalAccuracyN3/totalMeanAccuracyCountN3);
                                        }
                                    break;
                                }
                                String csvAnalysisText = cycleCount+","+ String.format("%.2f", precision)+","+ String.format("%.2f", accuracy)+"," + String.format("%.2f", meanPrecision)+ "," + String.format("%.2f", meanAccuracy) + "," + durationReasoning + "," + latencyAnalysisText+ "," + durationProcessing;
                                String  csvAnalysisPath= "C:\\Users\\user\\Documents\\SmartSUM\\rules\\air\\analysis_"+ extension + ".csv";
                                saveAnalysisCSV(csvAnalysisText, csvAnalysisPath, extension);
                                System.out.println(csvAnalysisText + "\n");
                            }
                            
                            System.out.println("*********************************************************************\n");
                            
                        }catch(Exception ex){
                         System.out.println(ex.toString());
                        }
                       System.out.println("");
                     //System.out.println("\nInferencing...");
                     //displayInferencing(getInfModel());
                     
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }
                    
            );
            
            //c1.addObserver(new ConsoleFormatter());
	}
        
        try {
            Thread.sleep(3*60*60*1000);
	} catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
	}
        
        // clean up (i.e., unregister query and stream)
	engine.unregisterQuery(c1.getId());
//	((LBSMARDFStreamTestGenerator) tg).pleaseStop();
	engine.unregisterStream(tg.getIRI());
        
        System.exit(0);
    }
    
    private static Property p ( String localname ){
        return ResourceFactory.createProperty ( BASE, localname );
    }

    private static Literal l1 ( String lexicalform, RDFDatatype datatype ) {
        return ResourceFactory.createTypedLiteral(lexicalform, datatype);
    }
    
    private static void reasonOnOntology(OntModel modelStreamed, String inferencelanguage, String temporaryModelToBeInferred){
        ExtendedIterator COInstances = modelStreamed
            .getOntClass("http://localhost:8080/smartSpace#COReadings")
            .listInstances();
        
        ExtendedIterator NOxInstances = modelStreamed
            .getOntClass("http://localhost:8080/smartSpace#NOXReadings")
            .listInstances();
         
        ExtendedIterator benzeneInstances = modelStreamed
            .getOntClass("http://localhost:8080/smartSpace#benzeneReadings")
            .listInstances();
          
        boolean proceedToNextCheck = false;
         
        List listCOInstances = COInstances.toList();
        List listNOxInstances = NOxInstances.toList();
        List listBenzeneInstances = benzeneInstances.toList();
         
        int COInstancesCount = listCOInstances.size();
        int NOxInstancesCount = listNOxInstances.size();
        int benzeneInstancesCount = listBenzeneInstances.size();
        
        int nullCount = 0;
        int plausibilityCount = 0;
        int inconsistencyCount = 0;
        int consistencyCount = 0;
        
        //System.out.println("CO: "+COInstancesCount + "\nNox: "+ NOxInstancesCount + "\nBeneze: "+ benzeneInstancesCount);
        
        int datasetCount = COInstancesCount;
        if(datasetCount > NOxInstancesCount)
            datasetCount = NOxInstancesCount;
        if(datasetCount > benzeneInstancesCount)
            datasetCount = benzeneInstancesCount;
                    
        //System.out.println("Dataset Count: "+ datasetCount);
        
        for(int index = 0; index < datasetCount; index++){
            OntModel tempModel = ModelFactory.createOntologyModel();
            tempModel.setNsPrefix("smartSpace", BASE);
            Individual individualCO = (Individual) listCOInstances.get(index);
            Individual  individualNOx = (Individual) listNOxInstances.get(index );
            Individual  individualBenzene = (Individual) listBenzeneInstances.get(index );
           
            String streamingTime = "";
            
            //Inconsistency check
            OntClass tempClass = tempModel.createClass(BASE+"COReadings");
            Individual tempIndividual = tempClass.createIndividual(individualCO.toString());
            
            StmtIterator iterator = individualCO.listProperties();
            while(iterator.hasNext()){
                Statement s = (Statement)iterator.next();
                 if(s.getObject().isLiteral()){
                     if(s.getLiteral().toString().contains("float")){
                         //System.out.println("CO value: "+ s.getLiteral().getLexicalForm());
                         tempIndividual.addProperty(p(s.getPredicate().getLocalName()), s.getLiteral().getLexicalForm(), XSDDatatype.XSDfloat);
                     }
                     else if(s.getLiteral().toString().contains("dateTime")){
                         streamingTime = s.getLiteral().getLexicalForm();
                         //System.out.println("CO Time: "+ streamingTime);
                         tempIndividual.addProperty(p(s.getPredicate().getLocalName()), streamingTime, XSDDatatype.XSDdateTime);
                     }
                 }
            }
            
            tempClass = tempModel.createClass(BASE+"NOXReadings");
            tempIndividual = tempClass.createIndividual(individualNOx.toString());
            
            iterator = individualNOx.listProperties();
            while(iterator.hasNext()){
                Statement s = (Statement)iterator.next();
                 if(s.getObject().isLiteral()){
                     if(s.getLiteral().toString().contains("integer")){
                         //System.out.println("Nox value: "+ s.getLiteral().getLexicalForm());
                         tempIndividual.addProperty(p(s.getPredicate().getLocalName()), s.getLiteral().getLexicalForm(), XSDDatatype.XSDfloat);
                     }
                     else if(s.getLiteral().toString().contains("dateTime")){
                         //System.out.println("Nox Time: "+ streamingTime);
                         tempIndividual.addProperty(p(s.getPredicate().getLocalName()), streamingTime, XSDDatatype.XSDdateTime);
                     }
                 }
            }
            
              tempClass = tempModel.createClass(BASE+"benzeneReadings");
              tempIndividual = tempClass.createIndividual(individualBenzene.toString());
               
              iterator = individualBenzene.listProperties();
                while(iterator.hasNext()){
                    Statement s = (Statement)iterator.next();
                    if(s.getObject().isLiteral()){
                        if(s.getLiteral().toString().contains("float")){
                            //System.out.println("Beneze value: "+ s.getLiteral().getLexicalForm());
                            tempIndividual.addProperty(p(s.getPredicate().getLocalName()), s.getLiteral().getLexicalForm(), XSDDatatype.XSDfloat);
                        }
                        else if(s.getLiteral().toString().contains("dateTime")){
                            //System.out.println("Benzene Time: "+ streamingTime);
                            tempIndividual.addProperty(p(s.getPredicate().getLocalName()), streamingTime, XSDDatatype.XSDdateTime);
                        }
                    }
                }
            
                Model reasoningModel = ModelFactory.createDefaultModel();
                try{
                    FileOutputStream outputStream = new FileOutputStream(temporaryModelToBeInferred);
                    //RDFDataMgr.write(outputStream, tempModel, RDFFormat.RDFXML_ABBREV);
                    tempModel.write(outputStream, inferencelanguage);  
                    
                   InputStream inschema = new FileInputStream(temporaryModelToBeInferred);
                   //System.out.println("I got here");
                   reasoningModel.read(inschema, null, inferencelanguage);
                  
                }catch(Exception ex){
                    System.err.println(ex.toString());
                }
               
            String ruleFile = "C:\\Users\\user\\Documents\\SmartSUM\\rules\\air\\airinconsistentrule.txt";
            List rules = Rule.rulesFromURL(ruleFile);
            
            Reasoner reasoner = new GenericRuleReasoner(rules);
            InfModel infmodel = ModelFactory.createInfModel(reasoner, reasoningModel);
             
            Resource inferenceSearch = ResourceFactory.createProperty(BASE + "isInconsistent");
            if(infmodel.containsResource(inferenceSearch)){
                //System.out.println("Inconsistent found");
                inconsistencyCount++;
            }else{
                proceedToNextCheck = true;
            }
            
            //Plausible check
            if(proceedToNextCheck){
                ruleFile = "C:\\Users\\user\\Documents\\SmartSUM\\rules\\air\\airplausibilityrule.txt";
                rules = Rule.rulesFromURL(ruleFile);
                reasoner = new GenericRuleReasoner(rules);
                infmodel = ModelFactory.createInfModel(reasoner, reasoningModel);

                inferenceSearch = ResourceFactory.createProperty(BASE + "isPlausible");
                if(infmodel.containsResource(inferenceSearch)){
                    //System.out.println("Plausible found");
                    plausibilityCount++;
                    proceedToNextCheck = false;
                }else{
                    proceedToNextCheck = true;
                }
            }
            
            //Null Check
            if(proceedToNextCheck){
                ruleFile = "C:\\Users\\user\\Documents\\SmartSUM\\rules\\air\\airnullrule.txt";
                rules = Rule.rulesFromURL(ruleFile);
                reasoner = new GenericRuleReasoner(rules);
                infmodel = ModelFactory.createInfModel(reasoner, reasoningModel);

                inferenceSearch = ResourceFactory.createProperty(BASE + "isNull");
                if(infmodel.containsResource(inferenceSearch)){
                    //System.out.println("Null found");
                    nullCount++;
                    proceedToNextCheck = false;
                }else{
                    proceedToNextCheck = true;
                }
            }
            
            //Consistency check
            if(proceedToNextCheck){
                consistencyCount++;
                //System.out.println("Consistent found");
            }
            
//            infmodel.write(System.out);
            //tempModel.write(System.out);
        }
        
        precisionInconsistency = (float)(inconsistencyCount)/(inconsistencyCount + plausibilityCount);
        recallInconsistency = (float)(inconsistencyCount)/(inconsistencyCount + nullCount);
        
        precisionConsistency = (float) (consistencyCount)/(consistencyCount + plausibilityCount);
        recallConsistency = (float)(consistencyCount)/(consistencyCount + nullCount);
        
        precisionPlausibilty = (float) (plausibilityCount)/(consistencyCount + plausibilityCount);
        recallPlausibilty = (float)(plausibilityCount)/(plausibilityCount + nullCount);
        
        precision = (float) (inconsistencyCount)/(inconsistencyCount + plausibilityCount);
        recall = (float)(inconsistencyCount)/(inconsistencyCount + nullCount);
        accuracy = (float) (inconsistencyCount + consistencyCount)/(inconsistencyCount + consistencyCount + plausibilityCount + nullCount); 
       float total = accuracy;
         
        System.out.println("PlausibiltyCheck count: " + plausibilityCount);
        System.out.println("ConsistencyCheck count: " + consistencyCount);
        System.out.println("InconsistencyCheck count: " + inconsistencyCount);
        System.out.println("nullCheck count: "+nullCount);
        
        System.out.println("Precision : "+String.format("%.2f",precision));
        System.out.println("Recall : " + String.format("%.2f",recall));
        System.out.println("Accuracy: " + String.format("%.2f",accuracy));
        
    }
    
    private static void saveAnalysisCSV(String text,String savePath, String fileExtension) throws Exception{
        FileWriter fileWriter = new FileWriter(savePath);
        switch(fileExtension){
            case "rdf":
                csvAnalysis_RDF+="\n"+text;
                fileWriter.write(csvAnalysis_RDF);
                fileWriter.flush();
                break;
            case "n3":
                csvAnalysis_N3+="\n"+text;
                fileWriter.write(csvAnalysis_N3);
                fileWriter.flush();
                break;
            case "ttl":
                csvAnalysis_Turtle+="\n"+text;
                fileWriter.write(csvAnalysis_Turtle);
                fileWriter.flush();
                break;
            case "nt":
                csvAnalysis_NTripple+="\n"+text;
                fileWriter.write(csvAnalysis_NTripple);
                fileWriter.flush();
                break;
        }
    }
}
