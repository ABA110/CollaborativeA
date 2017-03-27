public class ProcessorApp {


	public String calculateAggregatedData(String aOperand) {		
		//95% service availability
		double r=Math.random();
		if (r<=0.05)	return "Error: service unavailable";

		r=Math.random();
		if (r>=0.03) return "anAggregation, value= 500";		
		else return "Problem: computational exception";
	}

	public String configureObfuscation(String aConfigurationFile) {
		//97% service availability
		double r=Math.random();
		if (r<=0.03)	return "ERROR: service unavailable";
		return "OK:configured";	
	}
}