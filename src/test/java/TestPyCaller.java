import junit.framework.Assert;
import org.junit.Test;
import org.lappsgrid.pycaller.PyCaller;
import org.lappsgrid.pycaller.PyCallerException;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by shi on 5/15/14.
 */
public class TestPyCaller {

    @Test
    public void testCall() throws URISyntaxException, PyCallerException {
        File pyFile = new File(this.getClass().getResource("/helloworld.py").toURI());
        String ret = (String)PyCaller.call(pyFile, "hi", (Object)new String[]{"all", "my", "friend", "!"});
        System.out.println(ret);
        Assert.assertEquals("hello, all my friend ! ",ret);

//        pyFile = new File(this.getClass().getResource("/nltk_tagger.py").toURI());
//        List list = (List)PyCaller.call(pyFile, "tagger", "How");
//        Object [] pair = (Object [])list.get(0);
//        Assert.assertEquals("How",pair[0]);
//        Assert.assertEquals("WRB",pair[1]);
    }

}
