package logic.gateways;

import java.util.List;
import java.util.Map;

import logic.rules.LogicRulesDBImpl;


/**
 * @author Benoit Kessler
 *
 */
public class DestineeToLogicGatewayImpl implements DestineeToLogicGateway 
{
	private LogicToPrologGateway itsGate;
    
    private static DestineeToLogicGatewayImpl INSTANCE = new DestineeToLogicGatewayImpl();

    public static DestineeToLogicGatewayImpl getDefaultInstance()
    {
        return INSTANCE;
    }
    
	/**
	 * Creates and initializes a gateway for communication between Reflex and the Logic engine.
	 */
	private DestineeToLogicGatewayImpl()
	{
		super();
		itsGate = LogicToPrologGatewayImpl.getDefaultGateway();
		for (String rule : LogicRulesDBImpl.getDefaultInstance().getRulesList())
		{
			itsGate.addRule(rule);
		}
	}

	public List<Map<String, String>> query(String aQuery)
	{
		try 
		{
			return itsGate.queryAll(aQuery);
		}
		catch (RuntimeException e)
		{
            e.printStackTrace();
			return null;
		}
	}


	@Override
	public void ajouterPerso(String aNomPerso, int aNbDesAtt, int aBonusAtt, int aNbDesDeg, int aBonusDeg, int aNbPaCyc1, int aNbPaCyc2)
	{
		itsGate.addFact("perso('" + aNomPerso + "', " + aNbDesAtt + ", " + aBonusAtt + ", " + aNbDesDeg + ", " + aBonusDeg + ", " + aNbPaCyc1 + ", " + aNbPaCyc2 + ").\n");
	}
    

	@Override
	public void ajouterAttaquePerso(String aNomPerso, String aNomAttaque)
	{
		itsGate.addFact("attaque('" + aNomPerso + "', '" + aNomAttaque + "')\n.");
	}

    public void flush()
    {
        itsGate.flushFacts();
    }
    
    /**
     * Dispose the gateway.
     */
    public void reinitialize()
    {
        itsGate.dispose();
        itsGate = LogicToPrologGatewayImpl.getDefaultGateway();
        for (String rule : LogicRulesDBImpl.getDefaultInstance().getRulesList())
        {
            itsGate.addRule(rule);
        }
    }
    
    
    // Tools

//    private String generatePrologListFromSringList(List<String> aList) 
//    {
//        if (aList.size() == 0)
//            return "[]";
//        
//        String theResult = "['";
//        for (int i=0; i<aList.size()-1; i++)
//        {
//            theResult += aList.get(i) + "','";
//        }
//        theResult += aList.get(aList.size()-1) + "']";
//        return theResult;
//    }
}