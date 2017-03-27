import java.util.*;

public class CoordinationLogic{
	
	public static void main(String[] args) throws Exception{
		
		String processorURL = "http://localhost:8080/axis2/services/ProcessorApp";
		String maskURL = "http://localhost:8080/axis2/services/MaskApp";
		String messengerURL = "http://localhost:8080/axis2/services/Messenger";

		String extractNextCustomerMess = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ws.apache.org/axis2\">"+
   			"<soapenv:Header/>"+
   				"<soapenv:Body>"+
    				"<axis:ExtractNextCustomer/>"+
   				"</soapenv:Body>"+
			"</soapenv:Envelope>";
				
		String receiveMess ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ws.apache.org/axis2\">"+
			"<soapenv:Header/>"+
				"<soapenv:Body>"+
					"<axis:receive/>"+
				"</soapenv:Body>"+
			"</soapenv:Envelope>";
							
		
		/*Customer list used to store the nickname of the customers involved in order to send the final result at the end of the protocol*/
		List<String> listCustomer = new ArrayList<String>(); 
		String source = "";
			
		/*receive the start request (receive method is blocking)*/
		String protocolStartRequest= SOAPClient.sendMess(messengerURL, receiveMess);  
		source = extractSourceFromPayload(protocolStartRequest);


		String nextCustomer = SOAPClient.sendMess(maskURL, extractNextCustomerMess);   //this customer will be the first contacted in the chain
		//String nextCustomer = nextCustomerResp.split("<ns:return>")[1].split("</ns:return>")[0];
		listCustomer.add(nextCustomer);
		//System.out.println("First customer: "+nextCustomer);


		String fictitiousData = createFictitousData(protocolStartRequest);	//HumanCoordinator generate a fictitious data to start the protocol
			
		/*send the fictitious data to the first customer (anonymous transmission because HumanCoordinator acts as a Customer)*/
		 String sendFictitiousDataMess ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ws.apache.org/axis2\">" +
										"<soapenv:Header/>"+
										"<soapenv:Body>"+
											"<axis:send>"+
												"<!--Message:-->"+
												"<axis:args0>" + fictitiousData + "</axis:args0>"+
												"<!--Destination:-->"+
												"<axis:args1>Pippo</axis:args1>"+
												"<!--Anonymicity:-->"+
												"<axis:args2>true</axis:args2>"+
											"</axis:send>"+
										"</soapenv:Body>"+
										"</soapenv:Envelope>";

		SOAPClient.sendMess(messengerURL, sendFictitiousDataMess);


		while(true){
			String whoIsNextRequest = SOAPClient.sendMess(messengerURL, receiveMess);
			source = extractSourceFromPayload(whoIsNextRequest);
			nextCustomer = SOAPClient.sendMess(maskURL, extractNextCustomerMess);
			listCustomer.add(nextCustomer);
				
			/*send the nickname of next customer to the source*/
			String sendNextCustomerMess = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ws.apache.org/axis2\">" +
										"<soapenv:Header/>"+
										"<soapenv:Body>"+
											"<axis:send>"+
												"<!--Message:-->"+
												"<axis:args0>Pippo</axis:args0>"+
												"<!--Destination:-->"+
												"<axis:args1>" + source +"</axis:args1>"+
												"<!--Anonymicity:-->"+
												"<axis:args2>" + false + "</axis:args2>"+
											"</axis:send>"+
										"</soapenv:Body>"+
										"</soapenv:Envelope>";
			SOAPClient.sendMess(messengerURL, sendNextCustomerMess);
			
			/*if the nextCustomer is the CoordinationLogic itself, the customers chain is empty and the protocol finish*/
			if (nextCustomer.contains("coordinator") ) 
				break;
			}

		String collectiveData = SOAPClient.sendMess(messengerURL, receiveMess);


		/* The calculateAggregatedData method take in input a ProcessorApp.CalculatorInput object (one for each operation requested) */
		String calculateAggregatedDataMess = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns" +
					":axis=\"http://ws.apache.org/axis2\">" +
  						"<soapenv:Header/><soapenv:Body>" +
  						"<axis:calculateAggregatedData><axis:args0> Operation, 1005,5,2" +   							 
  						"</axis:args0></axis:calculateAggregatedData>" +
  					"</soapenv:Body></soapenv:Envelope>";
  		int operationNumber = (int) (Math.random() * (4));   //returns a value in the range [0, 4)
		String result = "";

		for(int i=0; i < operationNumber; i++){
				String aggregationResult = SOAPClient.sendMess(processorURL, calculateAggregatedDataMess);
				result = result + aggregationResult;
			}

		
		/*send the result to all customer contacted during the protocol*/
		for (String temp : listCustomer){
			String sendResultMess = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ws.apache.org/axis2\">" +
								"<soapenv:Header/>"+
								"<soapenv:Body>"+
									"<axis:send>"+
										"<!--Message:-->"+
										"<axis:args0>Media 500</axis:args0>"+
										"<!--Destination:-->"+
										"<axis:args1>Pippo</axis:args1>"+
										"<!--Anonymicity:-->"+
										"<axis:args2>false</axis:args2>"+
									"</axis:send>"+
								"</soapenv:Body>"+
								"</soapenv:Envelope>";
			SOAPClient.sendMess(messengerURL, sendResultMess);
			}
			

		System.out.println("Protocol finished");
	}  //main





	private static String createFictitousData (String protocolStartRequest){
		return "fictitiousData";
	}
	

	private static String[] extractCalculatorInput(String collectiveData){
		String[] result = new String[2];
		result[0] = "Result0";
		result[0] = "Result0";
		
		return result;
	}


	private static String extractSourceFromPayload(String payload){
	return "the_source_of_the_message";	
	}

} 