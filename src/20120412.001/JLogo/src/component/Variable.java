package component;

public class Variable extends Term {

	public Integer intValue = 0;

	public Variable(String strArgTerm, Integer intArgValue) {
		super(strArgTerm);
		this.intValue = intArgValue;
	}

}
