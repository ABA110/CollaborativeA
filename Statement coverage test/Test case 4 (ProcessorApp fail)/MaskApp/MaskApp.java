public class MaskApp {
    private int count=0;

	public String ExtractNextCustomer() {
		String nickname;
		if (count==0) nickname="Pippo";
		else if (count==1) nickname="Pluto";
		else nickname="coordinator";
		count= (count+1) % 3;
		
		return nickname;
	}

	public void UpdateListCustomers(String CustomerChain) {
		// TODO - implement MaskApp.UpdateListCustomers
		throw new UnsupportedOperationException();
	}

}