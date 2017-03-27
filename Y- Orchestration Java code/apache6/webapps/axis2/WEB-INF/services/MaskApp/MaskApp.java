public class MaskApp {
	int count = -1;		//count the residual customer in the list (internal state to avoid that first customer extracted is 'coordinator')

	public String ExtractNextCustomer() {
		if (count == -1) 			//protocol restart
			count= (int) (1 + (int)(Math.random() * 5));		//  --> extract a random integer in the range [1,5]  (at least one customer)
			
		double r = Math.random();
		if (r<0.01) {						//99% service availability
			count = -1;	//reset the list
			return "Error: service unavailable";  
		}

		//In case of available service...
		String nickname;
		if (count!=0) nickname="Pippo"+count;  
		else nickname="coordinator";	//if count is 0 we have to contact the last customer: 'coordinator'
		count--;
		return nickname;
	}

	
	public void UpdateListCustomers(String CustomerChain) {
		return;
	}

}