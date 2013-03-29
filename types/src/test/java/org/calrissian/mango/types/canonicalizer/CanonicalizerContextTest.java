package org.calrissian.mango.types.canonicalizer;

import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

public class CanonicalizerContextTest {

    @Test
    public void testFromStringRegularType() throws TypeNormalizationException {

        CanonicalizerContext context = CanonicalizerContext.getInstance();
        System.out.println(context.canonicalizeValueFromString("destinationIp", "1.2.3.4"));
        System.out.println(context.canonicalizeValueFromString("sourcePort", "8080"));
    }

    @Test
    public void testRange() throws Exception {
        CanonicalizerContext context = CanonicalizerContext.getInstance();
        assertNotNull(context.canonicalizeValueFromString("sourcePort", "8080"));
        try {
            context.canonicalizeValueFromString("sourcePort", "hello");
            fail();
        } catch (Exception e) {
        }
        try {
            context.canonicalizeValueFromString("sourcePort", "111111");
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testRegex() throws Exception {
        CanonicalizerContext context = CanonicalizerContext.getInstance();
        assertNotNull(context.canonicalizeValueFromString("sourceIp", "1.2.3.4"));
        try {
            context.canonicalizeValueFromString("sourceIp", "hello");
            fail();
        } catch (Exception e) {
        }
        try {
            context.canonicalizeValueFromString("sourceIp", "111111");
            fail();
        } catch (Exception e) {
        }
    }
}
