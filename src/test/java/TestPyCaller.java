/*
 * Copyright 2014 The Language Application Grid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import junit.framework.Assert;
import org.junit.Test;
import org.lappsgrid.pycaller.PyCaller;
import org.lappsgrid.pycaller.PyCallerException;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Chunqi SHI (shicq@cs.brandeis.edu)
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
