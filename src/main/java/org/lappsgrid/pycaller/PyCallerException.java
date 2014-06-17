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
