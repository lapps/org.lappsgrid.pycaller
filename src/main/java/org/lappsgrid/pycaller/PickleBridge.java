/**********************************************************************************************************************
 Copyright [2014] [Chunqi SHI (chunqi.shi@hotmail.com)]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **********************************************************************************************************************/
package org.lappsgrid.pycaller;

import net.razorvine.pickle.Pickler;
import net.razorvine.pickle.Unpickler;

import java.io.*;
import java.util.HashMap;

/**
 * Created by shi on 5/15/14.
 */
public class PickleBridge extends HashMap<String, Object>{
    /**
     * Parameters can be objects.
     * 3. TYPE MAPPINGS
     *<p>   ---------------------
     *<p>
     *<p>   Pyrolite does the following type mappings:
     *<p>
     *<p>   PYTHON    ---->     JAVA		[[[[ RETURN VALUES ]]]
     *<p>   ------              ----
     *<p>   None                null
     *<p>   bool                boolean
     *<p>   int                 int
     *<p>   long                long or BigInteger  (depending on size)
     *<p>   string              String
     *<p>   unicode             String
     *<p>   complex             net.razorvine.pickle.objects.ComplexNumber
     *<p>   datetime.date       java.util.Calendar
     *<p>   datetime.datetime   java.util.Calendar
     *<p>   datetime.time       java.util.Calendar
     *<p>   datetime.timedelta  net.razorvine.pickle.objects.TimeDelta
     *<p>   float               double   (float isn't used)
     *<p>   array.array         array of appropriate primitive type (char, int, short, long, float, double)
     *<p>   list                java.util.List<Object>
     *<p>   tuple               Object[]
     *<p>   set                 java.util.Set
     *<p>   dict                java.util.Map
     *<p>   bytes               byte[]
     *<p>   bytearray           byte[]
     *<p>   decimal             BigDecimal
     *<p>   custom class        Map<String, Object>  (dict with class attributes including its name in "__class__")
     *<p>   Pyro4.core.URI      net.razorvine.pyro.PyroURI
     *<p>   Pyro4.core.Proxy    net.razorvine.pyro.PyroProxy
     *<p>   Pyro4.errors.*      net.razorvine.pyro.PyroException
     *<p>   Pyro4.utils.flame.FlameBuiltin     net.razorvine.pyro.FlameBuiltin
     *<p>   Pyro4.utils.flame.FlameModule      net.razorvine.pyro.FlameModule
     *<p>   Pyro4.utils.flame.RemoteInteractiveConsole    net.razorvine.pyro.FlameRemoteConsole
     *<p>
     *<p>   The unpickler simply returns an Object. Because Java is a statically typed
     *<p>   language you will have to cast that to the appropriate type. Refer to this
     *<p>   table to see what you can expect to receive.
     *<p>
     *<p>
     *<p>   JAVA     ---->      PYTHON  		[[[[ PARAMETERS ]]]
     *<p>   -----               ------
     *<p>   null                None
     *<p>   boolean             bool
     *<p>   byte                int
     *<p>   char                str/unicode (length 1)
     *<p>   String              str/unicode
     *<p>   double              float
     *<p>   float               float
     *<p>   int                 int
     *<p>   short               int
     *<p>   BigDecimal          decimal
     *<p>   BigInteger          long
     *<p>   any array           array if elements are primitive type (else tuple)
     *<p>   Object[]            tuple (cannot contain self-references)
     *<p>   byte[]              bytearray
     *<p>   java.util.Date      datetime.datetime
     *<p>   java.util.Calendar  datetime.datetime
     *<p>   Enum                the enum value as string
     *<p>   java.util.Set       set
     *<p>   Map, Hashtable      dict
     *<p>   Vector, Collection  list
     *<p>   Serializable        treated as a JavaBean, see below.
     *<p>   JavaBean            dict of the bean's public properties + __class__ for the bean's type.
     *<p>   net.razorvine.pyro.PyroURI      Pyro4.core.URI
     *<p>   net.razorvine.pyro.PyroProxy    cannot be pickled.
     *
     **/


//    public static Object[] fromPickleParams(File pickleFile) throws IOException {
//        HashMap map = (HashMap)fromPickleObj(pickleFile);
//        Object [] params = (Object []) map.get("params");
//        return params;
//    }
//

    public File toPickleFile()throws IOException {
        return toPickleFile(this);
    }

    public static Object fromPickleFile(File pickleFile) throws IOException {
        Unpickler unpkl = new Unpickler ();
        InputStream in = new FileInputStream(pickleFile);
        Object res = null;
        try {
            res = unpkl.load(in);
        }finally{
            in.close();
        }
        return res;
    }

    public static File toPickleDirectory(Object obj, File directory) throws IOException {
        File file = new File(directory, PickleBridge.class.getName() + "." + System.currentTimeMillis()+".pickle");
        toPickleFile(obj,file);
        return file;
    }

    public static File toPickleFile(Object obj) throws IOException {
        return toPickleDirectory(obj, new File("."));
    }

    public static File toPickleFile(Object obj, File pickleFile) throws IOException {
        Pickler pkl = new Pickler();
        FileOutputStream out = new FileOutputStream(pickleFile);
        try {
            pkl.dump(obj, out);
        }finally{
            out.close();
        }
        return pickleFile;
    }

}
