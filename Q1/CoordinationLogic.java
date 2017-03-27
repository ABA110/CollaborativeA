import java.util.*;

public class CoordinationLogic {
	
	public static void main(String[] args){
		
		Messenger messenger = new Messenger();
		MaskApp maskApp = new MaskApp();
		ProcessorApp processorApp = new ProcessorApp();

		
		/*Customer list used to store the nickname of the customers involved in order to send the final result at the end of the protocol*/
		List<String> listCustomer = new ArrayList<String>(); 
		String source = "";
		String protocolStartRequest= messenger.receive();  //receive the start request (receive method is blocking)
		source = extractSourceFromPayload(protocolStartRequest);

		String nextCustomer = maskApp.ExtractNextCustomer();   //this customer will be the first contacted in the chain
		listCustomer.add(nextCustomer);
		System.out.println("First customer: "+nextCustomer);

		String fictitiousData = createFictitousData(protocolStartRequest);	//HumanCoordinator generate a fictitious data to start the protocol
		/*send the fictitious data to the first customer (anonymous transmission because HumanCoordinator acts as a Customer)*/
		messenger.send(fictitiousData, nextCustomer, true);

		while(true){
			String whoIsNextRequest = messenger.receive();
			source = extractSourceFromPayload(whoIsNextRequest);
			nextCustomer = maskApp.ExtractNextCustomer();
			listCustomer.add(nextCustomer);
			System.out.println("Next customer: "+nextCustomer);
			messenger.send(nextCustomer, source, false);    //send the nickname of next customer to the source

			/*if the nextCustomer is the CoordinationLogic itself, the customers chain is empty and the protocol finish*/
			if (nextCustomer.equals("coordinator"))  
				break;	
		}

		String collectiveData = messenger.receive();


		/* The calculateAggregatedData method take in input a ProcessorApp.CalculatorInput object (one for each operation requested) */
		String calculatorInputItem = "Operation,500,5,2";
		int operationNumber = (int) (Math.random() * (4));   //returns a value in the range [0, 4)
		String result = "";

		for(int i=0; i < operationNumber; i++){
			String aggregationResult = processorApp.calculateAggregatedData (calculatorInputItem);
			result = result + aggregationResult;
		}

		for (String temp : listCustomer)
			messenger.send(result, temp, false);	//send the result to all customer contacted during the protocol
		
		System.out.println("Protocol finished");
	}


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