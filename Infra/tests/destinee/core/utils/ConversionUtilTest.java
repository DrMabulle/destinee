/**
 * 
 */
package destinee.core.utils;

import java.math.BigDecimal;

import junit.framework.TestCase;

/**
 * @author Bubulle et No-one
 * 
 */
public class ConversionUtilTest extends TestCase
{
	public void testBigDecimalVersString()
	{
		String result = null;
		BigDecimal decimal = null;

		// Test grosse valeur
		decimal = new BigDecimal("258963.35785");
		result = ConversionUtil.bigDecimalVersString(decimal, 3);
		assertEquals("258 963,358", result);

		// Valeur proche de 0
		decimal = new BigDecimal("0.00000357844867486");
		result = ConversionUtil.bigDecimalVersString(decimal, 10);
		assertEquals("0,0000035784", result);
	}
}
