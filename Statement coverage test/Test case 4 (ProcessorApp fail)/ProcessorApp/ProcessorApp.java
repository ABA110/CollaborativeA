public class ProcessorApp {


	public String calculateAggregatedData(String aOperand) {		
		return "Error: service unavailable";
	}

	public String configureObfuscation(String aConfigurationFile) {
		//97% service availability
		double r=Math.random();
		if (r<=0.03)	return "ERROR: service unavailable";
		return "OK:configured";	
	}
}