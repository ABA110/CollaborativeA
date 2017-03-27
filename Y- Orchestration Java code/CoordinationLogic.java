import java.util.*;

public class CoordinationLogic{
	
	public static void main(String[] args) throws Exception{
		
		int successes = 0;

		String services = "http://localhost:8080/axis2/services/";
		String processorURL = services + "ProcessorApp";
		String maskURL = services + "MaskApp";
		String messengerURL = services + "Messenger";

		//header of all messages
		String header = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ws.apache.org/axis2\">"+
   			"<soapenv:Header/>"+
   				"<soapenv:Body>";

		String extractNextCustomerMess = header + 
							"<axis:ExtractNextCustomer/>"+
	   					"</soapenv:Body></soapenv:Envelope>";
				
		String receiveMess = header + 
							"<axis:receive/>"+
						"</soapenv:Body></soapenv:Envelope>";
							
		for (int i=0; i<1000; i++){
			if (i%100 == 0) System.out.println(i/10+"% completed...");
			boolean failed = false;

			/*Customer list used to store the nickname of the customers involved in order to send the final result at the end of the protocol*/
			List<String> listCustomer = new ArrayList<String>(); 
			String source = "";
			
			/*receive the start request (receive method is blocking)*/
			String protocolStartRequestResp= SOAPClient.sendMess(messengerURL, receiveMess);  
			if (protocolStartRequestResp.contains("Error")) {
				//Error: Messenger App unavailable! Protocol fail
				continue;
			}
			source = extractSourceFromPayload(protocolStartRequestResp);


			String nextCustomerResp = SOAPClient.sendMess(maskURL, extractNextCustomerMess);   //this customer will be the first contacted in the chain
			if (nextCustomerResp.contains("Error")) {
				//Error: Mask App unavailable! Protocol fail
				continue;
			}
			String nextCustomer = nextCustomerResp.split("<ns:return>")[1].split("</ns:return>")[0]; //extract the 'return' element containing the nickname
			listCustomer.add(nextCustomer);
			//System.out.println("First customer: "+nextCustomer);


			String fictitiousData = createFictitousData(protocolStartRequestResp);	//HumanCoordinator generate a fictitious data to start the protocol
				

			/*send the fictitious data to the first customer (anonymous transmission because HumanCoordinator acts as a Customer)*/
		 	String sendFictitiousDataMess = header +
												"<axis:send>"+
													"<!--Message:-->"+
													"<axis:args0>" + fictitiousData + "</axis:args0>"+
													"<!--Destination:-->"+
													"<axis:args1>" + nextCustomer +"</axis:args1>"+
													"<!--Anonymicity:-->"+
													"<axis:args2>true</axis:args2>"+
												"</axis:send>"+
											"</soapenv:Body>"+
											"</soapenv:Envelope>";

			SOAPClient.sendMess(messengerURL, sendFictitiousDataMess);


			while(true){
				String whoIsNextRequestResp = SOAPClient.sendMess(messengerURL, receiveMess);
				if (whoIsNextRequestResp.contains("Error")) {
					//Error: Messenger App unavailable! Protocol fail
					failed=true;
					break;
				}

				source = extractSourceFromPayload(whoIsNextRequestResp);
				nextCustomerResp = SOAPClient.sendMess(maskURL, extractNextCustomerMess);
				if (nextCustomerResp.contains("Error")) {
					//Error: Mask App unavailable! Protocol fail.
					failed=true;
					break;
				}
				nextCustomer = nextCustomerResp.split("<ns:return>")[1].split("</ns:return>")[0]; //extract the 'return' element containing the nickname
				listCustomer.add(nextCustomer);
				//System.out.println("Next customer: "+nextCustomer);
				
				/*send the nickname of next customer to the source*/
				String sendNextCustomerMess = header+
												"<axis:send>"+
													"<!--Message:-->"+
													"<axis:args0>"+ nextCustomer + "</axis:args0>"+
													"<!--Destination:-->"+
													"<axis:args1>" + source +"</axis:args1>"+
													"<!--Anonymicity:-->"+
													"<axis:args2>false</axis:args2>"+
												"</axis:send>"+
											"</soapenv:Body>"+
											"</soapenv:Envelope>";
				SOAPClient.sendMess(messengerURL, sendNextCustomerMess);
				
				/*if the nextCustomer is the CoordinationLogic itself, the customers chain is empty and the protocol finish*/
				if (nextCustomer.equals("coordinator") ) 
					break;
			}

			if (failed) 
				continue;


			String collectiveDataResp = SOAPClient.sendMess(messengerURL, receiveMess);
			if (collectiveDataResp.contains("Error")) {
					//Error: Messenger App unavailable! Protocol fail.
					continue;
				}
			String collectiveData = collectiveDataResp.split("<ns:return>")[1].split("</ns:return>")[0];

			/* The calculateAggregatedData method take in input a ProcessorApp.CalculatorInput object (one for each operation requested) */
			String calculateAggregatedDataMess = header +
	  								"<axis:calculateAggregatedData>" +
	  									"<axis:args0>" + collectiveData + "</axis:args0>" +
	  								"</axis:calculateAggregatedData>" +
	  							"</soapenv:Body></soapenv:Envelope>";
	  		int operationNumber = (int) (Math.random() * (4));   //returns a value in the range [0, 4)
			String result = "";

			for(int j=0; j <= operationNumber; j++){
					String aggregationResultResp = SOAPClient.sendMess(processorURL, calculateAggregatedDataMess);
					if (aggregationResultResp.contains("Error") || aggregationResultResp.contains("Problem")) {
						//Error: Processor App unavailable! Protocol fail.
						continue;
					}
					String aggregationResult = aggregationResultResp.split("<ns:return>")[1].split("</ns:return>")[0];
					result = result + "Result "+ j +": "+ aggregationResult +"\n";
				}

			
			/*send the result to all customer contacted during the protocol*/
			for (String temp : listCustomer){
				String sendResultMess = header +
										"<axis:send>"+
											"<!--Message:-->"+
											"<axis:args0>"+ result +"</axis:args0>"+
											"<!--Destination:-->"+
											"<axis:args1>" + temp +"</axis:args1>"+
											"<!--Anonymicity:-->"+
											"<axis:args2>false</axis:args2>"+
										"</axis:send>"+
									"</soapenv:Body>"+
									"</soapenv:Envelope>";
				SOAPClient.sendMess(messengerURL, sendResultMess);
				}
				
			successes++;
				
		} //for

	System.out.println ("Success "+ successes/10 + "%");
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