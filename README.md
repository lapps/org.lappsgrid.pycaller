org.lappsgrid.pycaller
======================

Python Function Caller from Java

### Usage

```xml
        <dependency>
            <groupId>org.lappsgrid</groupId>
            <artifactId>pycaller</artifactId>
            <version>0.1.0</version>
        </dependency>
```

### API

```java
    PyCaller.call(pythonPath, methodName, param1, param2, ...);
```


### Example

#### Python File (helloworld.py)
```python
" " " helloworld.py " " "
def hi(arr):
    res = "hello, "
    for item in arr:
        res += item + " "
    return res
```
#### Java File HelloWorld.java
```java
public class HelloWorld {
	public static void main(String [] args) {
		String ret = (String)PyCaller.call(pyFile, "hi", (Object)new String[]{"all", "my", "friend", "!"})
	}
}
```

The result will be `"hello, all my friend ! "`;

### Python to Java Mapping

```
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
```
