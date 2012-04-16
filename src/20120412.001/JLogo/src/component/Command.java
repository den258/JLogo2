package component;

public class Command {

	public String strCommand = new String();
	public Expression objExpression = null;

	public Command(String strArgCommand, Expression objArgExpression) {
		this.strCommand = strArgCommand;
		this.objExpression = objArgExpression;
	}

}
