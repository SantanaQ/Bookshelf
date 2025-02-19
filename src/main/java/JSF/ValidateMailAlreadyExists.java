package JSF;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import database.DatabaseStatements;
import objects.Kunde;

@FacesValidator(value="validateAlreadyExists")
public class ValidateMailAlreadyExists implements Validator<Object>{

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object email) throws ValidatorException {
    	DatabaseStatements stmt = new DatabaseStatements();
    	Kunde k = stmt.getKunde((String) email);
    	
        if (email == null) {
            return;
        }

        if (k != null) {
            throw new ValidatorException(new FacesMessage("Es besteht bereits ein Account mit der angegebenen E-Mail Adresse."));
        }
    }
		

}
