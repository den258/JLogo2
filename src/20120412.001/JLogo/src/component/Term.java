package component;

import utils.Utils;

public class Term {

	public String strTerm = new String();

	public Term(String strArgTerm) {
		this.strTerm = strArgTerm;
	}

	public String getType() {
		return Utils.getTermType(this.strTerm);
	}

}
