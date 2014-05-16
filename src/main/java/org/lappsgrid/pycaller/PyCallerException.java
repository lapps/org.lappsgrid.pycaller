package org.lappsgrid.pycaller;


/**
 * @brief Exception handler for Python Runner 
 * @author shicq@cs.brandeis.edu
 * 
 *
 */
public class PyCallerException extends Exception {
    private static final long serialVersionUID = 5519418141743137712L;

	public PyCallerException(){
		super();
	}
	
	public PyCallerException(String msg){
		super(msg);
	}
	
	public PyCallerException(String msg, Throwable th){
		super(msg, th);
	}
	
	public PyCallerException(Throwable th) {
		super(th);
	}

}
