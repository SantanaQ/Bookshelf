package JSF;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import database.DatabaseStatements;
import objects.Kunde;

@FacesValidator(value="validatePassword")
public class ValidatePassword  implements Validator<Object>  {

	@Override
	public void validate(FacesContext context, UIComponent component, Object pw) throws ValidatorException {
		Object mail = component.getAttributes().get("mail");
		DatabaseStatements stmt = new DatabaseStatements();
    	Kunde k = stmt.getKunde((String) mail);
    	String password = k.getPasswort();
		
		if(pw == null || mail == null) {
			return;
		}
		
		if(!pw.equals(password)) {
			 throw new ValidatorException(new FacesMessage("values are not equal."));
		}
	}

}
