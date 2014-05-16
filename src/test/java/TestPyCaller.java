import junit.framework.Assert;
import org.junit.Test;
import org.lappsgrid.pycaller.PyCaller;
import org.lappsgrid.pycaller.PyCallerException;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by shi on 5/15/14.
 */
public class TestPyCaller {

    @Test
    public void testCall() throws URISyntaxException, PyCallerException {
        File pyFile = new File(this.getClass().getResource("/helloworld.py").toURI());
        String ret = (String)PyCaller.call(pyFile.getAbsolutePath(), "hi", new String[]{"all", "my", "friend", "!"});
        System.out.println(ret);
        Assert.assertEquals("hello, all my friend ! ",ret);
    }

}
