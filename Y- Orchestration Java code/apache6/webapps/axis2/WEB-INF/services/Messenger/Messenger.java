public class Messenger {

	public void login(String nickname, String password) {
		// TODO - implement Messenger.login
		//throw new UnsupportedOperationException();
	}


	public void send(String message, String destination, boolean anonymicity) {
		// TODO - implement Messenger.send
		//throw new UnsupportedOperationException();
	}

	public String receive() {
		// TODO - implement Messenger.receive
		//throw new UnsupportedOperationException();
		double r = Math.random();
		if(r > 0.95) return "Error: service unavailable";
		return "From: ArcadianDressAndStyle; Message: Send me next customer nickname";
	}


	public String retrieveStatistics(String preferences) {
		// TODO - implement Messenger.retrieveStatistics
		//throw new UnsupportedOperationException();
		double r = Math.random();
		if(r > 0.8) return "Data missing: impossible to provide statistics";
		return "Result1: nickname=Winery_shop_01 avgCheckingInterval=271,89s \n Result2: nickname=BestSportShop avgCheckingInterval=390,61s";
	}

}