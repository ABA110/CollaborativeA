public class Messenger {

	/**
	 * 
	 * @param nickname
	 * @param password
	 */
	public void login(String nickname, String password) {
		// TODO - implement Messenger.login
		//throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param message
	 * @param destination
	 * @param anonymicity
	 */
	public void send(String message, String destination, boolean anonymicity) {
		// TODO - implement Messenger.send
		//throw new UnsupportedOperationException();
	}

	public String receive() {
		// TODO - implement Messenger.receive
		//throw new UnsupportedOperationException();
		return "From: ArcadianDressAndStyle; Message: Send me next customer nickname";
	}

	/**
	 * 
	 * @param preferences
	 */
	public String retrieveStatistics(String preferences) {
		// TODO - implement Messenger.retrieveStatistics
		//throw new UnsupportedOperationException();
		return "Result1: nickname=Winery_shop_01 avgCheckingInterval=271,89s \n Result2: nickname=BestSportShop avgCheckingInterval=390,61s";
	}

}