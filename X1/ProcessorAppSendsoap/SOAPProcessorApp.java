public class SOAPProcessorApp {

 public static void main(String args[]) throws Exception {
 
  String calculateAggregatedDataMess = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns"
  +":axis=\"http://ws.apache.org/axis2\">"
  +"<soapenv:Header/><soapenv:Body>"
  +"<axis:calculateAggregatedData><axis:args0>Average, 1005, 5, 2</axis:args0></axis:calculateAggregatedData>"
  +"</soapenv:Body></soapenv:Envelope>";

  String processorUrl="http://localhost:8080/axis2/services/ProcessorApp";
  
  System.out.println("\n************* Request SOAP Message: *************\n" + calculateAggregatedDataMess);
  
  
  int problemCount=0;
  int errorCount=0;

  for(int i=0;i<1000;i++){
    String resp = SOAPClient.sendMess(processorUrl, calculateAggregatedDataMess);
    //System.out.println("\n************* Response SOAP Message: *************\n" + resp);
    if(resp.contains("Error")){
      ++errorCount;
      //System.out.println("\n************* Response SOAP Message: *************\n" + resp);
    } 
    else if (resp.contains("Problem")){
      ++problemCount;
      //System.out.println("\n************* Response SOAP Message: *************\n" + resp);
    } 

  }  

  System.out.println("\n errors :"+errorCount + "\n problems:" +problemCount);
  
 }
}