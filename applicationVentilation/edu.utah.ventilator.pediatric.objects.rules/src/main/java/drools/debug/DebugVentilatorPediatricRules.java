package drools.debug;


import java.util.GregorianCalendar;

import ventilator.decision.object.VentilatorPediatricDecision;
import core.patient.object.Patient;
import drools.engine.KnowledgeEngine;

public class DebugVentilatorPediatricRules {

	public static KnowledgeEngine knowledgeEngine;
	private static VentilatorPediatricDecision decision;
	private static Patient patient = new Patient("TestLast","TestFirst","12-34-56","TESTTEST0001", new GregorianCalendar(1999,12,12),22.34, 25.34);

	private static GregorianCalendar decisionDate = new GregorianCalendar(2005,9,25,14,55,55);
	
	public static void main(String[] args) {
		
		decision= new VentilatorPediatricDecision(decisionDate);
		patient.addDecision(decision);
		
		try{
			knowledgeEngine = new KnowledgeEngine();
			System.out.println("Created knowledge engine.\nRules now firing:\n");
			knowledgeEngine.testFireRules(decision);
		} catch (Throwable t){
			t.printStackTrace();
		}

	}

}
